package com.daryeou.app.core.domain.usecase.kakao.favorite

import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetKakaoFavoriteMediaList @Inject constructor(
    private val kakaoSearchRepo: KakaoSearchRepo
) {
    operator fun invoke(): Flow<List<KakaoSearchMediaItemData>> =
        kakaoSearchRepo.getKakaoFavoriteItemList().map { favoriteItem ->
            favoriteItem.itemList.map { item ->
                KakaoSearchMediaItemData(
                    isFavorite = true,
                    mediaInfo = item
                )
            }
        }
}