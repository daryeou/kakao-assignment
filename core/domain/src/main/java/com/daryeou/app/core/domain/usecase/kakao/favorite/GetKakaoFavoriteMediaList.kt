package com.daryeou.app.core.domain.usecase.kakao.favorite

import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.model.kakao.KakaoSearchResultItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetKakaoFavoriteMediaList @Inject constructor(
    private val kakaoSearchRepo: KakaoSearchRepo
) {
    operator fun invoke(): Flow<List<KakaoSearchResultItem>> =
        kakaoSearchRepo.getKakaoFavoriteItemList()
}