package com.daryeou.app.core.data.repository

import com.daryeou.app.core.data.mapper.KakaoSearchMapper.toDomain
import com.daryeou.app.core.data.source.local.KakaoFavoriteMediaDataSource
import com.daryeou.app.core.data.source.remote.KakaoSearchDataSource
import com.daryeou.app.core.data.util.safeFlow
import com.daryeou.app.core.domain.entities.kakao.favorite.KakaoFavoriteEntity
import com.daryeou.app.core.domain.entities.kakao.search.KakaoSearchEntity
import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.domain.repository.KakaoSearchSortType
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
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
    ): Flow<ApiResult<KakaoSearchEntity>> {
        return safeFlow {
            kakaoSearchDataSource.getKakaoImageSearch(query, sort.value, page, size).toDomain()
        }
    }

    override fun getKakaoVideoSearchList(
        query: String,
        sort: KakaoSearchSortType,
        page: Int,
        size: Int
    ): Flow<ApiResult<KakaoSearchEntity>> {
        return safeFlow {
            kakaoSearchDataSource.getKakaoVideoSearch(query, sort.value, page, size).toDomain()
        }
    }

    override fun getKakaoFavoriteItemList(): Flow<KakaoFavoriteEntity> = flow {
        emit(kakaoFavoriteMediaDataSource.getFavoriteList().toDomain())
    }

    override suspend fun addKakaoFavoriteItem(searchResultItem: KakaoSearchMediaBasicData) {
        kakaoFavoriteMediaDataSource.addFavoriteItem(searchResultItem)
    }

    override suspend fun removeKakaoFavoriteItem(searchResultItem: KakaoSearchMediaBasicData) {
        kakaoFavoriteMediaDataSource.removeFavoriteItem(searchResultItem)
    }
}