package com.daryeou.app.core.domain.usecase.kakao.search

import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.domain.repository.KakaoSearchSortType
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaDetailData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetKakaoMediaSearchSortedResult @Inject constructor(
    private val kakaoSearchRepo: KakaoSearchRepo
) {
    private var currentPageSize = 30
    private var currentQuery: String = ""

    private lateinit var favoriteMediaList: List<KakaoSearchMediaBasicData>

    private var kakaoSearchImagePage = 1
    private var kakaoSearchVideoPage = 1

    private var kakaoSearchImagePageable = true
    private var kakaoSearchVideoPageable = true

    private val kakaoSearchImageList: MutableList<KakaoSearchMediaBasicData> = mutableListOf()
    private val kakaoSearchVideoList: MutableList<KakaoSearchMediaBasicData> = mutableListOf()

    private val kakaoSearchMixedListSorted = mutableListOf<KakaoSearchMediaDetailData>()

    operator fun invoke(
        page: Int,
        pageSize: Int = currentPageSize,
        query: String = currentQuery,
    ): Flow<ApiResult<List<KakaoSearchMediaDetailData>>> = channelFlow {
        if (currentQuery != query) {
            currentPageSize = pageSize
            currentQuery = query
            kakaoSearchImagePage = 1
            kakaoSearchVideoPage = 1
            kakaoSearchImagePageable = true
            kakaoSearchVideoPageable = true
            kakaoSearchImageList.clear()
            kakaoSearchVideoList.clear()
            kakaoSearchMixedListSorted.clear()

            // Get favorite media list
            kakaoSearchRepo.getKakaoFavoriteItemList().collectLatest { favoriteList ->
                favoriteMediaList = favoriteList.itemList
            }
        }

        // Combine two search result
        while (kakaoSearchMixedListSorted.size < page * pageSize) {
            // Check if both image and video are not pageable
            if (!kakaoSearchImagePageable && !kakaoSearchVideoPageable) {
                break
            }

            // Get search result
            val kakaoImageSearchFlow = kakaoSearchRepo.getKakaoImageSearchList(
                query,
                KakaoSearchSortType.RECENCY,
                kakaoSearchImagePage,
                80
            )
            val kakaoVideoSearchFlow = kakaoSearchRepo.getKakaoVideoSearchList(
                query,
                KakaoSearchSortType.RECENCY,
                kakaoSearchVideoPage,
                30
            )

            withContext(Dispatchers.IO) {
                launch {
                    // Get image search result
                    if (kakaoSearchImagePageable) {
                        if (kakaoSearchImageList.isNotEmpty()) {
                            return@launch
                        }
                        kakaoImageSearchFlow.collectLatest { apiResult ->
                            if (apiResult is ApiResult.Success) {
                                kakaoSearchImageList.addAll(apiResult.value.itemList)
                                kakaoSearchImagePage++
                                if (apiResult.value.isEnd) {
                                    kakaoSearchImagePageable = false
                                }
                            } else {
                                if (apiResult is ApiResult.Error) {
                                    send(apiResult)
                                    kakaoSearchImagePageable = false
                                } else if (apiResult is ApiResult.Exception) {
                                    send(apiResult)
                                }
                                this@channelFlow.cancel()
                            }
                        }
                    }
                }
                launch {
                    // Get video search result
                    if (kakaoSearchVideoPageable) {
                        if (kakaoSearchVideoList.isNotEmpty()) {
                            return@launch
                        }
                        kakaoVideoSearchFlow.collectLatest { apiResult ->
                            if (apiResult is ApiResult.Success) {
                                kakaoSearchVideoList.addAll(apiResult.value.itemList)
                                kakaoSearchVideoPage++
                                if (apiResult.value.isEnd) {
                                    kakaoSearchVideoPageable = false
                                }
                            } else {
                                if (apiResult is ApiResult.Error) {
                                    send(apiResult)
                                    kakaoSearchVideoPageable = false
                                } else if (apiResult is ApiResult.Exception) {
                                    send(apiResult)
                                }
                                this@channelFlow.cancel()
                            }
                        }
                    }
                }
            }

            val tempMixedList = mutableListOf<KakaoSearchMediaDetailData>()
            lateinit var hasLessDateType: MutableList<KakaoSearchMediaBasicData>
            lateinit var hasMoreDateType: MutableList<KakaoSearchMediaBasicData>

            if (kakaoSearchImageList.isEmpty() && kakaoSearchVideoList.isEmpty()) {
                break
            } else if (kakaoSearchImageList.isEmpty()) {
                hasLessDateType = kakaoSearchVideoList
                hasMoreDateType = arrayListOf()
            } else if (kakaoSearchVideoList.isEmpty()) {
                hasLessDateType = kakaoSearchImageList
                hasMoreDateType = arrayListOf()
            } else if (kakaoSearchImageList.last().dateTime > kakaoSearchVideoList.last().dateTime) {
                hasLessDateType = kakaoSearchImageList
                hasMoreDateType = kakaoSearchVideoList
            } else {
                hasLessDateType = kakaoSearchVideoList
                hasMoreDateType = kakaoSearchImageList
            }
            val lastDateTime = hasLessDateType.last().dateTime

            // Add last date type to mixed list, and remove it from original list
            hasLessDateType.forEach { basicData ->
                tempMixedList.add(
                    KakaoSearchMediaDetailData(
                        favoriteMediaList.contains(basicData),
                        KakaoSearchMediaBasicData(
                            basicData.thumbnailUrl,
                            basicData.dateTime,
                        )
                    )
                )
            }
            hasLessDateType.clear()

            // Add more date type to mixed list, and remove it from original list
            hasMoreDateType.toList().forEach { basicData ->
                if (basicData.dateTime > lastDateTime) {
                    tempMixedList.add(
                        KakaoSearchMediaDetailData(
                            favoriteMediaList.contains(basicData),
                            KakaoSearchMediaBasicData(
                                basicData.thumbnailUrl,
                                basicData.dateTime,
                            )
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
        send(
            ApiResult.Success(
                kakaoSearchMixedListSorted.subList(
                    (page - 1) * pageSize,
                    (page * pageSize).coerceAtMost(kakaoSearchMixedListSorted.size)
                )
            )
        )
    }
}
