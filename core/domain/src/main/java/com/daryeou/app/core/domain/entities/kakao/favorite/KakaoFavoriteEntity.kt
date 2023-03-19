package com.daryeou.app.core.domain.entities.kakao.favorite

import com.daryeou.app.core.model.kakao.KakaoSearchResultItem

data class KakaoFavoriteEntity(
    val itemList: List<KakaoSearchResultItem>,
)
