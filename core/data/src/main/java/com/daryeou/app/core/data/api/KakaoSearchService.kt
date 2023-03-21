package com.daryeou.app.core.data.api

import com.daryeou.app.core.data.model.KakaoImageSearchResponse
import com.daryeou.app.core.data.model.KakaoVideoSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoSearchService {
    @GET("/v2/search/image")
    suspend fun getKakaoImageSearchResponse(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): KakaoImageSearchResponse

    @GET("/v2/search/vclip")
    suspend fun getKakaoVideoSearchResponse(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): KakaoVideoSearchResponse
}