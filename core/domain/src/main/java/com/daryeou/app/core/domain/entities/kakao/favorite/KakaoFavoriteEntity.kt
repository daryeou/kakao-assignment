package com.daryeou.app.core.domain.entities.kakao.favorite

import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData

data class KakaoFavoriteEntity(
    val itemList: List<KakaoSearchMediaBasicData>,
)
