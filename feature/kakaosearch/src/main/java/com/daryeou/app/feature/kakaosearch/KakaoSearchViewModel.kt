package com.daryeou.app.feature.kakaosearch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.domain.usecase.kakao.favorite.AddKakaoFavoriteMediaItem
import com.daryeou.app.core.domain.usecase.kakao.favorite.RemoveKakaoFavoriteMediaItem
import com.daryeou.app.core.domain.usecase.kakao.search.GetKakaoMediaSearchSortedResult
import com.daryeou.app.core.model.kakao.KakaoSearchMediaDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KakaoSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getKakaoMediaSearchSortedResult: GetKakaoMediaSearchSortedResult,
    private val addKakaoFavoriteMediaItem: AddKakaoFavoriteMediaItem,
    private val removeKakaoFavoriteMediaItem: RemoveKakaoFavoriteMediaItem,
) : ViewModel() {

    private val _KakaoMediaItemList: MutableStateFlow<KakaoSearchMediaListState> = MutableStateFlow(KakaoSearchMediaListState(false, 1, listOf()))
    val kakaoMediaItemList: StateFlow<KakaoSearchMediaListState>
        get() = _KakaoMediaItemList.asStateFlow()

    private val _kakaoSearchState: MutableStateFlow<KakaoSearchState> = MutableStateFlow(KakaoSearchState.IDLE)
    val kakaoSearchState: StateFlow<KakaoSearchState>
        get() = _kakaoSearchState.asStateFlow()

    fun searchMedia(query: String) {
        viewModelScope.launch {
            getKakaoMediaSearchSortedResult(1, KakaoSearchPageSize, query).collectLatest { apiResult ->
                when (apiResult) {
                    is ApiResult.Success -> {
                        if (apiResult.value.isEmpty()) {
                            _KakaoMediaItemList.value = KakaoSearchMediaListState(
                                pageable = false,
                                page = 1,
                                mediaList = listOf()
                            )
                            _kakaoSearchState.value = KakaoSearchState.EMPTY
                        } else {
                            _KakaoMediaItemList.value = KakaoSearchMediaListState(
                                pageable = true,
                                page = 1,
                                mediaList = apiResult.value.toList()
                            )
                            _kakaoSearchState.value = KakaoSearchState.SHOW_RESULT
                        }
                    }

                    else -> {
                        _kakaoSearchState.value = KakaoSearchState.ERROR
                    }
                }
            }
        }
    }

    fun searchMediaNextPage() {
        viewModelScope.launch {
            val nextPage = _KakaoMediaItemList.value.page + 1
            getKakaoMediaSearchSortedResult(nextPage).collectLatest { apiResult ->
                when (apiResult) {
                    is ApiResult.Success -> {
                        if (apiResult.value.isEmpty()) {
                            _KakaoMediaItemList.update { mediaListState ->
                                mediaListState.copy(pageable = false)
                            }
                        } else {
                            _KakaoMediaItemList.update { mediaListState ->
                                mediaListState.copy(
                                    page = nextPage,
                                    mediaList = mediaListState.mediaList + apiResult.value.toList()
                                )
                            }
                        }
                        _kakaoSearchState.value = KakaoSearchState.SHOW_RESULT
                    }

                    else -> {
                        _KakaoMediaItemList.update { mediaListState ->
                            mediaListState.copy(pageable = false)
                        }
                        _kakaoSearchState.value = KakaoSearchState.ERROR
                    }
                }
            }
        }
    }

    fun toggleFavorite(mediaDetailData: KakaoSearchMediaDetailData) {
        viewModelScope.launch {
            if (mediaDetailData.isFavorite) {
                removeKakaoFavoriteMediaItem(mediaDetailData.mediaInfo)
            } else {
                addKakaoFavoriteMediaItem(mediaDetailData.mediaInfo)
            }

            _KakaoMediaItemList.update { previousSearchMediaListState ->
                KakaoSearchMediaListState(
                    pageable = previousSearchMediaListState.pageable,
                    page = previousSearchMediaListState.page,
                    mediaList = previousSearchMediaListState.mediaList.toMutableList().apply {
                        val index = indexOfFirst { previousMediaDetailData ->
                            previousMediaDetailData.mediaInfo == mediaDetailData.mediaInfo
                        }
                        if (index != -1) {
                            this[index] = mediaDetailData.copy(isFavorite = !mediaDetailData.isFavorite)
                        }
                    }
                )
            }
        }
    }
}

const val KakaoSearchPageSize = 20

enum class KakaoSearchState {
    IDLE,
    EMPTY,
    SHOW_RESULT,
    ERROR,
}