package com.daryeou.app.core.data.repository

import com.daryeou.app.core.data.mapper.KakaoSearchMapper.toDomain
import com.daryeou.app.core.data.source.local.KakaoFavoriteMediaDataSource
import com.daryeou.app.core.data.source.remote.KakaoSearchDataSource
import com.daryeou.app.core.data.util.safeFlow
import com.daryeou.app.core.domain.entities.kakao.search.KakaoImageSearchEntity
import com.daryeou.app.core.domain.entities.kakao.search.KakaoVideoSearchEntity
import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.domain.repository.KakaoSearchSortType
import com.daryeou.app.core.model.kakao.KakaoSearchResultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KakaoSearchRepoImpl @Inject constructor(
    private val kakaoSearchDataSource: KakaoSearchDataSource,
    private val kakaoFavoriteMediaDataSource: KakaoFavoriteMediaDataSource,
) : KakaoSearchRepo {
    override fun getKakaoImageSearchList(
        query: String,
        sort: KakaoSearchSortType,
        page: Int,
        size: Int
    ): Flow<ApiResult<KakaoImageSearchEntity>> {
        return safeFlow {
            kakaoSearchDataSource.getKakaoImageSearch(query, sort.value, page, size).toDomain()
        }
    }

    override fun getKakaoVideoSearchList(
        query: String,
        sort: KakaoSearchSortType,
        page: Int,
        size: Int
    ): Flow<ApiResult<KakaoVideoSearchEntity>> {
        return safeFlow {
            kakaoSearchDataSource.getKakaoVideoSearch(query, sort.value, page, size).toDomain()
        }
    }

    override fun getKakaoFavoriteItemList(): Flow<List<KakaoSearchResultItem>> = flow {
        emit(kakaoFavoriteMediaDataSource.getFavoriteList())
    }

    override suspend fun addKakaoFavoriteItem(searchResultItem: KakaoSearchResultItem) {
        kakaoFavoriteMediaDataSource.addFavoriteItem(searchResultItem)
    }

    override suspend fun removeKakaoFavoriteItem(searchResultItem: KakaoSearchResultItem) {
        kakaoFavoriteMediaDataSource.removeFavoriteItem(searchResultItem)
    }
}