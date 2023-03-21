package com.daryeou.app.core.domain.repository

import com.daryeou.app.core.domain.entities.kakao.favorite.KakaoFavoriteEntity
import com.daryeou.app.core.domain.entities.kakao.search.KakaoSearchEntity
import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import kotlinx.coroutines.flow.Flow

interface KakaoSearchRepo {
    fun getKakaoImageSearchList(
        query: String,
        sort: KakaoSearchSortType,
        page: Int,
        size: Int
    ): Flow<ApiResult<KakaoSearchEntity>>

    fun getKakaoVideoSearchList(
        query: String,
        sort: KakaoSearchSortType,
        page: Int,
        size: Int
    ): Flow<ApiResult<KakaoSearchEntity>>

    fun getKakaoFavoriteItemList(): Flow<KakaoFavoriteEntity>

    suspend fun addKakaoFavoriteItem(searchResultItem: KakaoSearchMediaBasicData)

    suspend fun removeKakaoFavoriteItem(searchResultItem: KakaoSearchMediaBasicData)
}

enum class KakaoSearchSortType(val value: String) {
    ACCURACY("accuracy"),
    RECENCY("recency"),
}