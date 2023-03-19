package com.daryeou.app.core.domain.usecase.kakao.search

import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.domain.repository.KakaoSearchSortType

class GetKakaoVideoSearchResult(
    private val kakaoSearchRepo: KakaoSearchRepo
) {
    operator fun invoke(
        query: String,
        sort: KakaoSearchSortType,
        page: Int,
        size: Int
    ) = kakaoSearchRepo.getKakaoVideoSearchList(query, sort, page, size)
}