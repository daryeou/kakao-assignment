package com.daryeou.app.core.data.source.local

import com.daryeou.app.core.model.kakao.KakaoSearchResultItem
import com.daryeou.app.core.sharedpreference.KakaoSharedPreferencesManager
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Local Data Source for Kakao Favorite Media
 */
class KakaoFavoriteMediaDataSource @Inject constructor(
    private val kakaoSharedPreferencesManager: KakaoSharedPreferencesManager
) {
    fun getFavoriteList(): List<KakaoSearchResultItem> {
        val favoriteJsonString = kakaoSharedPreferencesManager.getFavoriteJsonString()
        return favoriteJsonString.toFavoriteList().toList()
    }

    fun addFavoriteItem(searchResultItem: KakaoSearchResultItem) {
        val favoriteList = getFavoriteList().toMutableList()
        favoriteList.add(searchResultItem)
        kakaoSharedPreferencesManager.updateFavoriteJsonString(favoriteList.toFavoriteJsonString())
    }

    fun removeFavoriteItem(searchResultItem: KakaoSearchResultItem) {
        val favoriteList = getFavoriteList().toMutableList()
        favoriteList.remove(searchResultItem)
        kakaoSharedPreferencesManager.updateFavoriteJsonString(favoriteList.toFavoriteJsonString())
    }
}

/**
 * Data class to Json String
 */
private fun List<KakaoSearchResultItem>.toFavoriteJsonString(): String {
    val gson = Gson()
    return gson.toJson(this)
}

private fun String.toFavoriteList(): Array<KakaoSearchResultItem> {
    val gson = Gson()
    return gson.fromJson(this, Array<KakaoSearchResultItem>::class.java)
}

