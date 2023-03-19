package com.daryeou.app.core.sharedpreference

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class KakaoSharedPreferencesManager @Inject constructor(
    private val instance: SharedPreferences
) {
    fun getFavoriteJsonString(): String =
        instance.getString(FAVORITE_MEDIA_JSON_STRING, null) ?: ""

    fun updateFavoriteJsonString(jsonString: String) {
        instance.edit {
            putString(FAVORITE_MEDIA_JSON_STRING, jsonString)
        }
    }
}

private const val FAVORITE_MEDIA_JSON_STRING = "favorite_media_json_string"

