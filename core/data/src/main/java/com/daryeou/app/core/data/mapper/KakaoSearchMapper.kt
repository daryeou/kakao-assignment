package com.daryeou.app.core.data.mapper

import com.daryeou.app.core.data.model.KakaoImageSearchResponse
import com.daryeou.app.core.data.model.KakaoVideoSearchResponse
import com.daryeou.app.core.domain.entities.kakao.favorite.KakaoFavoriteEntity
import com.daryeou.app.core.domain.entities.kakao.search.KakaoSearchEntity
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaType

object KakaoSearchMapper {
    fun KakaoImageSearchResponse.toDomain() = KakaoSearchEntity(
        isEnd = this.meta.isEnd,
        itemList = this.documents.map { item ->
            KakaoSearchMediaBasicData(
                title = item.siteName,
                url = item.url,
                originalUrl = item.imageUrl,
                thumbnailUrl = item.thumbnailUrl,
                dateTime = item.datetime,
                mediaType = KakaoSearchMediaType.IMAGE,
            )
        }
    )

    fun KakaoVideoSearchResponse.toDomain() = KakaoSearchEntity(
        isEnd = meta.isEnd,
        itemList = documents.map { item ->
            KakaoSearchMediaBasicData(
                title = item.title,
                url = item.url,
                thumbnailUrl = item.thumbnailUrl,
                dateTime = item.datetime,
                mediaType = KakaoSearchMediaType.VIDEO,
            )
        }
    )

    fun List<KakaoSearchMediaBasicData>.toDomain() = KakaoFavoriteEntity(
        itemList = this
    )
}