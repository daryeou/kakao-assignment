package com.daryeou.app.feature.kakaofavorite

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daryeou.app.core.domain.usecase.kakao.favorite.GetKakaoFavoriteMediaList
import com.daryeou.app.core.domain.usecase.kakao.favorite.RemoveKakaoFavoriteMediaItem
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KakaoFavoriteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getKakaoFavoriteMediaList: GetKakaoFavoriteMediaList,
    private val removeKakaoFavoriteMediaItem: RemoveKakaoFavoriteMediaItem,
) : ViewModel() {
    private val _kakaoFavoriteList: MutableStateFlow<List<KakaoSearchMediaItemData>> =
        MutableStateFlow(mutableStateListOf())
    val kakaoFavoriteList: StateFlow<List<KakaoSearchMediaItemData>>
        get() = _kakaoFavoriteList.asStateFlow()

    private val _kakaoFavoriteUiState: MutableStateFlow<KakaoFavoriteUiState> =
        MutableStateFlow(KakaoFavoriteUiState.LOADING)
    val kakaoFavoriteUiState: StateFlow<KakaoFavoriteUiState>
        get() = _kakaoFavoriteUiState.asStateFlow()

    fun getKakaoFavoriteList() {
        viewModelScope.launch {
            getKakaoFavoriteMediaList().collectLatest { kakaoMediaItemData ->
                _kakaoFavoriteList.value = kakaoMediaItemData.toMutableList()
                _kakaoFavoriteUiState.value = KakaoFavoriteUiState.SUCCESS
            }
        }
    }

    fun removeKakaoFavoriteItem(kakaoMediaItemData: KakaoSearchMediaItemData) {
        viewModelScope.launch {
            removeKakaoFavoriteMediaItem(kakaoMediaItemData.mediaInfo)
            _kakaoFavoriteList.value = _kakaoFavoriteList.value.toMutableList().apply {
                remove(kakaoMediaItemData)
            }
        }
    }
}

enum class KakaoFavoriteUiState {
    LOADING,
    SUCCESS,
}