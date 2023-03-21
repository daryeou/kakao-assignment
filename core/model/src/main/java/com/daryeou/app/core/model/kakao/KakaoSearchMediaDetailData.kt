package com.daryeou.app.core.model.kakao

import java.util.Date

data class KakaoSearchMediaDetailData(
    val isFavorite: Boolean,
    val mediaInfo: KakaoSearchMediaBasicData,
)

data class KakaoSearchMediaBasicData(
    val thumbnailUrl: String,
    val dateTime: Date,
)
