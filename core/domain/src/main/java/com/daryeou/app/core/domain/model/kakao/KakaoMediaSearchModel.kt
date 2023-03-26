package com.daryeou.app.core.domain.model.kakao

import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData

data class KakaoMediaSearchModel(
    val isEnd: Boolean,
    val itemList: List<KakaoSearchMediaItemData>,
)
