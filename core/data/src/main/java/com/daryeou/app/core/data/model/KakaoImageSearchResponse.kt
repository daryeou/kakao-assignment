package com.daryeou.app.core.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class KakaoImageSearchResponse(
    @SerializedName("documents")
    val documents: List<KakaoImageSearchDocument>,
    @SerializedName("meta")
    val meta: KakaoImageSearchMeta
)

data class KakaoImageSearchDocument(
    @SerializedName("collection")
    val collection: String,
    @SerializedName("datetime")
    val datetime: Date,
    @SerializedName("display_sitename")
    val siteName: String,
    @SerializedName("doc_url")
    val url: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("width")
    val width: Int
)

data class KakaoImageSearchMeta(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("total_count")
    val totalCount: Int
)
