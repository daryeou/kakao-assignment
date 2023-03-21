package com.daryeou.app.core.data.source.local

import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.sharedpreference.KakaoSharedPreferencesManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

/**
 * Local Data Source for Kakao Favorite Media
 */
class KakaoFavoriteMediaDataSource @Inject constructor(
    private val kakaoSharedPreferencesManager: KakaoSharedPreferencesManager
) {
    fun getFavoriteList(): List<KakaoSearchMediaBasicData> {
        val favoriteJsonString = kakaoSharedPreferencesManager.getFavoriteJsonString()
        return favoriteJsonString.toFavoriteList().toList()
    }

    fun addFavoriteItem(searchResultItem: KakaoSearchMediaBasicData) {
        val favoriteList = getFavoriteList().toMutableList()
        favoriteList.add(searchResultItem)
        kakaoSharedPreferencesManager.updateFavoriteJsonString(favoriteList.toFavoriteJsonString())
    }

    fun removeFavoriteItem(searchResultItem: KakaoSearchMediaBasicData) {
        val favoriteList = getFavoriteList().toMutableList()
        favoriteList.remove(searchResultItem)
        kakaoSharedPreferencesManager.updateFavoriteJsonString(favoriteList.toFavoriteJsonString())
    }
}

/**
 * Data class to Json String
 */
private fun List<KakaoSearchMediaBasicData>.toFavoriteJsonString(): String {
    val gson = Gson()
    return gson.toJson(this)
}

private fun String.toFavoriteList(): List<KakaoSearchMediaBasicData> {
    if (this.isEmpty()) {
        return listOf()
    }

    val gson = Gson()
    val type = object : TypeToken<List<KakaoSearchMediaBasicData>>() {}.type
    return gson.fromJson(this, type)
}

