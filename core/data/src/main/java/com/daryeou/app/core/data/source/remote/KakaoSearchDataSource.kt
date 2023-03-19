package com.daryeou.app.core.data.source.remote

import com.daryeou.app.core.data.api.KakaoSearchService
import javax.inject.Inject

class KakaoSearchDataSource @Inject constructor(
    private val kakaoSearchService: KakaoSearchService
) {
    suspend fun getKakaoImageSearch(query: String, sort: String, page: Int, size: Int) =
        kakaoSearchService.getKakaoImageSearchResponse(query, sort, page, size)

    suspend fun getKakaoVideoSearch(query: String, sort: String, page: Int, size: Int) =
        kakaoSearchService.getKakaoVideoSearchResponse(query, sort, page, size)
}