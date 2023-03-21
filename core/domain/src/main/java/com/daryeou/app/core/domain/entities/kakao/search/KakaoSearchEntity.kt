package com.daryeou.app.core.domain.entities.kakao.search

import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData

data class KakaoSearchEntity(
    val isEnd: Boolean,
    val itemList: List<KakaoSearchMediaBasicData>,
)