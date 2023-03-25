package com.daryeou.app.core.model.kakao

import java.util.Date

data class KakaoSearchMediaBasicData(
    val title: String,
    val url: String,
    val originalUrl: String? = null,
    val thumbnailUrl: String,
    val dateTime: Date,
    val mediaType: KakaoSearchMediaType,
)

enum class KakaoSearchMediaType {
    IMAGE,
    VIDEO,
}