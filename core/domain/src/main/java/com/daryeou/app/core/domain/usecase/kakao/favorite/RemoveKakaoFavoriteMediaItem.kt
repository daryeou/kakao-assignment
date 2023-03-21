package com.daryeou.app.core.domain.usecase.kakao.favorite

import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import javax.inject.Inject

class RemoveKakaoFavoriteMediaItem @Inject constructor(
    private val kakaoSearchRepo: KakaoSearchRepo
) {
    suspend operator fun invoke(searchResultItem: KakaoSearchMediaBasicData) =
        kakaoSearchRepo.removeKakaoFavoriteItem(searchResultItem)
}