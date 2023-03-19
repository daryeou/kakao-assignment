package com.daryeou.app.core.domain.entities.kakao.search

import com.daryeou.app.core.model.kakao.KakaoSearchResultItem

data class KakaoVideoSearchEntity(
    val itemList: List<KakaoSearchResultItem>,
)