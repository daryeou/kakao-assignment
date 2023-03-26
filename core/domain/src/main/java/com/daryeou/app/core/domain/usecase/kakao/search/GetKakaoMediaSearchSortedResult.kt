package com.daryeou.app.core.domain.usecase.kakao.search

import com.daryeou.app.core.domain.entities.kakao.search.KakaoSearchEntity
import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.domain.model.kakao.KakaoMediaSearchModel
import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.domain.repository.KakaoSearchSortType
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private data class KakaoMediaSearchApiState(
    var nextPage: Int = 1,
    var pageable: Boolean = true,
    var mediaList: MutableList<KakaoSearchMediaBasicData> = mutableListOf(),
    var apiFlow: Flow<ApiResult<KakaoSearchEntity>>? = null,
)

/**
 * Use case to get the image and video search result from Kakao API
 */
class GetKakaoMediaSearchSortedResult @Inject constructor(
    private val kakaoSearchRepo: KakaoSearchRepo,
) {
    private var currentPageSize = 30
    private var currentQuery: String = ""

    private var imageSearchApiState: KakaoMediaSearchApiState = KakaoMediaSearchApiState()
    private var videoSearchApiState: KakaoMediaSearchApiState = KakaoMediaSearchApiState()

    private val kakaoSearchMixedListSorted = mutableListOf<KakaoSearchMediaBasicData>()

    /**
     * Get sorted media search result
     * @param page The page number
     * @param pageSize The size of the page
     * @param query The query to search
     * @param refresh If true, refresh the search result
     */
    operator fun invoke(
        page: Int,
        pageSize: Int = currentPageSize,
        query: String = currentQuery,
        refresh: Boolean = false,
    ): Flow<ApiResult<KakaoMediaSearchModel>> = channelFlow {
        if (currentQuery != query || refresh) {
            currentPageSize = pageSize
            currentQuery = query
            imageSearchApiState = KakaoMediaSearchApiState()
            videoSearchApiState = KakaoMediaSearchApiState()
            kakaoSearchMixedListSorted.clear()
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
                ).forEach { apiState ->
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

            // Check api limit page
            if (imageSearchApiState.nextPage > ImagePageLimit) {
                imageSearchApiState.pageable = false
            }
            if (videoSearchApiState.nextPage > VideoPageLimit) {
                videoSearchApiState.pageable = false
            }

            val tempMixedList = mutableListOf<KakaoSearchMediaBasicData>()
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
            tempMixedList.addAll(hasLessDateType)
            hasLessDateType.clear()

            // Add more date type to mixed list, and remove it from original list
            hasMoreDateType.toList().forEach { basicData ->
                if (basicData.dateTime > lastDateTime) {
                    tempMixedList.add(basicData)
                    hasMoreDateType.remove(basicData)
                }
            }

            // Sort mixed list
            tempMixedList.sortByDescending { mediaBasicData ->
                mediaBasicData.dateTime
            }

            kakaoSearchMixedListSorted.addAll(tempMixedList)
        }

        // Get favorite list
        val favoriteList = getFavoriteList()

        // Send result
        if (kakaoSearchMixedListSorted.size >= (page - 1) * pageSize) {
            val itemSortedList: List<KakaoSearchMediaItemData> = kakaoSearchMixedListSorted
                .subList(0, (page * pageSize).coerceAtMost(kakaoSearchMixedListSorted.size))
                .map { mediaBasicData ->
                    KakaoSearchMediaItemData(
                        favoriteList.contains(mediaBasicData),
                        mediaBasicData
                    )
                }

            if (imageSearchApiState.pageable || videoSearchApiState.pageable) {
                send(
                    ApiResult.Success(
                        KakaoMediaSearchModel(
                            isEnd = false,
                            itemList = itemSortedList
                        )
                    )
                )
            } else {
                send(
                    ApiResult.Success(
                        KakaoMediaSearchModel(
                            isEnd = true,
                            itemList = itemSortedList
                        )
                    )
                )
            }
        } else {
            send(ApiResult.Exception(Exception("No more data")))
        }
    }

    private suspend fun getFavoriteList(): List<KakaoSearchMediaBasicData> {
        return kakaoSearchRepo.getKakaoFavoriteItemList().first().itemList
    }
}

const private val ImagePageLimit = 50
const private val VideoPageLimit = 15
