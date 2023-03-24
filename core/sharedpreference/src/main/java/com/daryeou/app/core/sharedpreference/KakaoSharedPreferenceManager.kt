package com.daryeou.app.core.sharedpreference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class KakaoSharedPreferencesManager @Inject constructor(
    private val instance: SharedPreferences
) {
    fun getFavoriteMediaList(): List<KakaoSearchMediaBasicData> {
        val favoriteJsonString = instance.getString(FAVORITE_MEDIA_LIST, "") ?: ""
        return favoriteJsonString.toFavoriteList()
    }


    fun updateFavoriteMediaList(favoriteMediaList: List<KakaoSearchMediaBasicData>) {
        val jsonString = favoriteMediaList.toFavoriteJsonString()
        instance.edit {
            putString(FAVORITE_MEDIA_LIST, jsonString)
        }
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

private const val FAVORITE_MEDIA_LIST = "kakao_favorite_media_list"

