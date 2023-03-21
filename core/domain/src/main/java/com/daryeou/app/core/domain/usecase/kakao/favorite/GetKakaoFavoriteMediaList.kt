package com.daryeou.app.core.domain.usecase.kakao.favorite

import com.daryeou.app.core.domain.entities.kakao.favorite.KakaoFavoriteEntity
import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetKakaoFavoriteMediaList @Inject constructor(
    private val kakaoSearchRepo: KakaoSearchRepo
) {
    operator fun invoke(): Flow<KakaoFavoriteEntity> =
        kakaoSearchRepo.getKakaoFavoriteItemList()
}