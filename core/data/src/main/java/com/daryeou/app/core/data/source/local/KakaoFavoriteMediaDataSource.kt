package com.daryeou.app.core.data.source.local

import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.sharedpreference.KakaoSharedPreferencesManager
import javax.inject.Inject

/**
 * Local Data Source for Kakao Favorite Media
 */
class KakaoFavoriteMediaDataSource @Inject constructor(
    private val kakaoSharedPreferencesManager: KakaoSharedPreferencesManager
) {
    fun getFavoriteList(): List<KakaoSearchMediaBasicData> {
        val favoriteJsonString = kakaoSharedPreferencesManager.getFavoriteMediaList()
        return favoriteJsonString
    }

    fun addFavoriteItem(searchResultItem: KakaoSearchMediaBasicData) {
        val favoriteList = getFavoriteList().toMutableList()
        favoriteList.add(searchResultItem)
        kakaoSharedPreferencesManager.updateFavoriteMediaList(favoriteList)
    }

    fun removeFavoriteItem(searchResultItem: KakaoSearchMediaBasicData) {
        val favoriteList = getFavoriteList().toMutableList()
        favoriteList.remove(searchResultItem)
        kakaoSharedPreferencesManager.updateFavoriteMediaList(favoriteList)
    }
}



