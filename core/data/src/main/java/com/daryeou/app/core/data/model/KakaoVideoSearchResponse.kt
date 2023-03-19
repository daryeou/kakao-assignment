package com.daryeou.app.core.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class KakaoVideoSearchResponse(
    @SerializedName("documents")
    val documents: List<KakaoVideoSearchDocument>,
    @SerializedName("meta")
    val meta: KakaoVideoSearchMeta
)

data class KakaoVideoSearchDocument(
    @SerializedName("author")
    val author: String,
    @SerializedName("datetime")
    val datetime: Date,
    @SerializedName("play_time")
    val playTime: Int,
    @SerializedName("thumbnail")
    val thumbnailUrl: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)

data class KakaoVideoSearchMeta(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("total_count")
    val totalCount: Int
)
