package com.daryeou.app.core.data.mapper

import com.daryeou.app.core.data.model.KakaoImageSearchResponse
import com.daryeou.app.core.data.model.KakaoVideoSearchResponse
import com.daryeou.app.core.domain.entities.kakao.search.KakaoImageSearchEntity
import com.daryeou.app.core.domain.entities.kakao.search.KakaoVideoSearchEntity
import com.daryeou.app.core.model.kakao.KakaoSearchResultItem

object KakaoSearchMapper {
    fun KakaoImageSearchResponse.toDomain() = KakaoImageSearchEntity(
        itemList = this.documents.map { item ->
            KakaoSearchResultItem(
                thumbnailUrl = item.thumbnailUrl,
                dateTime = item.datetime,
            )
        }
    )

    fun KakaoVideoSearchResponse.toDomain() = KakaoVideoSearchEntity(
        itemList = this.documents.map { item ->
            KakaoSearchResultItem(
                thumbnailUrl = item.thumbnailUrl,
                dateTime = item.datetime,
            )
        }
    )
}