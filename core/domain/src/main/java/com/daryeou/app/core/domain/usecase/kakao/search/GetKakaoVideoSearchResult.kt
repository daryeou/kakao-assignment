package com.daryeou.app.core.domain.usecase.kakao.search

import com.daryeou.app.core.domain.entities.kakao.search.KakaoSearchEntity
import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.domain.repository.KakaoSearchSortType
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get the video search result from Kakao API
 */
class GetKakaoVideoSearchResult(
    private val kakaoSearchRepo: KakaoSearchRepo
) {
    /**
     * Invoke the use case
     * @param query The query to search
     * @param sort The sort type
     * @param page The page number, must be smaller than 30
     * @param size The size of the page
     */
    operator fun invoke(
        query: String,
        sort: KakaoSearchSortType = KakaoSearchSortType.RECENCY,
        page: Int = 1,
        size: Int = 30,
    ): Flow<ApiResult<KakaoSearchEntity>> {
        // Check size is not greater than 30
        if (size > 30) {
            throw IllegalArgumentException("Size must be less than or equal to 30")
        }
        return kakaoSearchRepo.getKakaoVideoSearchList(query, sort, page, size)
    }
}