package com.daryeou.app.core.domain.repository

import com.daryeou.app.core.domain.entities.kakao.search.KakaoImageSearchEntity
import com.daryeou.app.core.domain.entities.kakao.search.KakaoVideoSearchEntity
import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.model.kakao.KakaoSearchResultItem
import kotlinx.coroutines.flow.Flow

interface KakaoSearchRepo {
    fun getKakaoImageSearchList(
        query: String,
        sort: KakaoSearchSortType,
        page: Int,
        size: Int
    ): Flow<ApiResult<KakaoImageSearchEntity>>

    fun getKakaoVideoSearchList(
        query: String,
        sort: KakaoSearchSortType,
        page: Int,
        size: Int
    ): Flow<ApiResult<KakaoVideoSearchEntity>>

    fun getKakaoFavoriteItemList(): Flow<List<KakaoSearchResultItem>>

    suspend fun addKakaoFavoriteItem(searchResultItem: KakaoSearchResultItem)

    suspend fun removeKakaoFavoriteItem(searchResultItem: KakaoSearchResultItem)
}

enum class KakaoSearchSortType(val value: String) {
    ACCURACY("accuracy"),
    RECENCY("recency"),
}