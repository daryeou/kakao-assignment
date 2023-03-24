package com.daryeou.app.core.domain.usecase.kakao.search

import android.util.Log
import com.daryeou.app.core.domain.entities.kakao.search.KakaoSearchEntity
import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.domain.repository.KakaoSearchSortType
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private data class KakaoMediaSearchApiState(
    var nextPage: Int = 1,
    var pageable: Boolean = true,
    var mediaList: MutableList<KakaoSearchMediaBasicData> = mutableListOf(),
    var apiFlow: Flow<ApiResult<KakaoSearchEntity>>? = null,
)

class GetKakaoMediaSearchSortedResult @Inject constructor(
    private val kakaoSearchRepo: KakaoSearchRepo
) {
    private var currentPageSize = 30
    private var currentQuery: String = ""

    private lateinit var favoriteMediaList: List<KakaoSearchMediaBasicData>

    private var imageSearchApiState: KakaoMediaSearchApiState = KakaoMediaSearchApiState()
    private var videoSearchApiState: KakaoMediaSearchApiState = KakaoMediaSearchApiState()

    private val kakaoSearchMixedListSorted = mutableListOf<KakaoSearchMediaItemData>()

    operator fun invoke(
        page: Int,
        pageSize: Int = currentPageSize,
        query: String = currentQuery,
    ): Flow<ApiResult<List<KakaoSearchMediaItemData>>> = channelFlow {
        if (currentQuery != query) {
            currentPageSize = pageSize
            currentQuery = query
            imageSearchApiState = KakaoMediaSearchApiState()
            videoSearchApiState = KakaoMediaSearchApiState()
            kakaoSearchMixedListSorted.clear()

            // Get favorite media list
            kakaoSearchRepo.getKakaoFavoriteItemList().collectLatest { favoriteList ->
                favoriteMediaList = favoriteList.itemList
            }
        }

        // Combine two search result
        while (kakaoSearchMixedListSorted.size < page * pageSize) {
            // Check if both image and video are not pageable
            if (!imageSearchApiState.pageable && !videoSearchApiState.pageable) {
                break
            }

            imageSearchApiState.apiFlow = kakaoSearchRepo.getKakaoImageSearchList(
                query,
                KakaoSearchSortType.RECENCY,
                imageSearchApiState.nextPage,
                80
            )
            videoSearchApiState.apiFlow = kakaoSearchRepo.getKakaoVideoSearchList(
                query,
                KakaoSearchSortType.RECENCY,
                videoSearchApiState.nextPage,
                30
            )

            withContext(Dispatchers.IO) {
                listOf(
                    imageSearchApiState,
                    videoSearchApiState,
                ).forEach {  apiState ->
                    launch {
                        apiState.apply {
                            // Get image search result
                            if (pageable) {
                                if (mediaList.isNotEmpty()) {
                                    return@launch
                                }
                                apiFlow?.collectLatest { apiResult ->
                                    if (apiResult is ApiResult.Success) {
                                        mediaList.addAll(apiResult.value.itemList)
                                        nextPage++
                                        if (apiResult.value.isEnd) {
                                            pageable = false
                                        }
                                    } else {
                                        /**
                                         * If there is an error, cancel the flow
                                         * and return the error to the UI
                                         */
                                        if (apiResult is ApiResult.Error) {
                                            send(apiResult)
                                            pageable = false
                                        } else if (apiResult is ApiResult.Exception) {
                                            send(apiResult)
                                        }
                                        this@channelFlow.cancel()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            val tempMixedList = mutableListOf<KakaoSearchMediaItemData>()
            lateinit var hasLessDateType: MutableList<KakaoSearchMediaBasicData>
            lateinit var hasMoreDateType: MutableList<KakaoSearchMediaBasicData>

            val imageList = imageSearchApiState.mediaList
            val videoList = videoSearchApiState.mediaList

            if (imageList.isEmpty() && videoList.isEmpty()) {
                break
            } else if (imageList.isEmpty()) {
                hasLessDateType = videoList
                hasMoreDateType = arrayListOf()
            } else if (videoList.isEmpty()) {
                hasLessDateType = imageList
                hasMoreDateType = arrayListOf()
            } else if (imageList.last().dateTime > videoList.last().dateTime) {
                hasLessDateType = imageList
                hasMoreDateType = videoList
            } else {
                hasLessDateType = videoList
                hasMoreDateType = imageList
            }
            val lastDateTime = hasLessDateType.last().dateTime

            // Add last date type to mixed list, and remove it from original list
            hasLessDateType.forEach { basicData ->
                tempMixedList.add(
                    KakaoSearchMediaItemData(
                        favoriteMediaList.contains(basicData),
                        basicData,
                    )
                )
            }
            hasLessDateType.clear()

            // Add more date type to mixed list, and remove it from original list
            hasMoreDateType.toList().forEach { basicData ->
                if (basicData.dateTime > lastDateTime) {
                    tempMixedList.add(
                        KakaoSearchMediaItemData(
                            favoriteMediaList.contains(basicData),
                            basicData,
                        )
                    )
                    hasMoreDateType.remove(basicData)
                }
            }

            // Sort mixed list
            tempMixedList.sortByDescending { detailData ->
                detailData.mediaInfo.dateTime
            }

            kakaoSearchMixedListSorted.addAll(tempMixedList)
        }

        // Send result
        if (kakaoSearchMixedListSorted.size >= (page - 1) * pageSize) {
            send(
                ApiResult.Success(
                    kakaoSearchMixedListSorted.subList(
                        (page - 1) * pageSize,
                        (page * pageSize).coerceAtMost(kakaoSearchMixedListSorted.size)
                    ).toList()
                )
            )
        } else {
            send(
                ApiResult.Success(
                    emptyList()
                )
            )
        }

    }
}
