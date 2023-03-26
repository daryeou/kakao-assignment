package com.daryeou.app.feature.kakaosearch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daryeou.app.core.domain.model.ApiResult
import com.daryeou.app.core.domain.usecase.kakao.favorite.AddKakaoFavoriteMediaItem
import com.daryeou.app.core.domain.usecase.kakao.favorite.RemoveKakaoFavoriteMediaItem
import com.daryeou.app.core.domain.usecase.kakao.search.GetKakaoMediaSearchSortedResult
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
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

    private val _kakaoSearchState: MutableStateFlow<KakaoSearchUiState> =
        MutableStateFlow(KakaoSearchUiState.IDLE)
    val kakaoSearchState: StateFlow<KakaoSearchUiState>
        get() = _kakaoSearchState.asStateFlow()

    private val _KakaoMediaItemList: MutableStateFlow<KakaoSearchMediaListState> =
        MutableStateFlow(KakaoSearchMediaListState("", false, 1, listOf()))
    val kakaoMediaItemList: StateFlow<KakaoSearchMediaListState>
        get() = _KakaoMediaItemList.asStateFlow()

    fun onRefresh() {
        val itemState = _KakaoMediaItemList.value
        if (itemState.query.isNotEmpty()) {
            searchMedia(itemState.query, itemState.page)
        }
    }

    fun onQuery(query: String) {
        _kakaoSearchState.value = KakaoSearchUiState.LOADING
        if (query.isNotEmpty()) {
            searchMedia(query, 1, true)
        }
    }

    fun onNextPage(query: String, page: Int) {
        searchMedia(query, page)
    }

    private fun searchMedia(query: String, totalPage: Int, refresh: Boolean = false) {
        viewModelScope.launch {
            getKakaoMediaSearchSortedResult(totalPage, KakaoSearchPageSize, query, refresh).collectLatest { apiResult ->
                when (apiResult) {
                    /**
                     * If [ApiResult.Success],
                     * if the list is empty, set the state to [KakaoSearchUiState.EMPTY]
                     * if the list is not empty, set the state to [KakaoSearchUiState.SHOW_RESULT]
                     */
                    is ApiResult.Success -> {
                        if (apiResult.value.itemList.isEmpty()) {
                            _KakaoMediaItemList.value = KakaoSearchMediaListState(
                                query = "",
                                pageable = false,
                                page = 1,
                                mediaList = listOf()
                            )
                            _kakaoSearchState.value = KakaoSearchUiState.EMPTY
                        } else {
                            _KakaoMediaItemList.value = KakaoSearchMediaListState(
                                query = query,
                                pageable = apiResult.value.isEnd.not(),
                                page = totalPage,
                                mediaList = apiResult.value.itemList
                            )
                            _kakaoSearchState.value = KakaoSearchUiState.SHOW_RESULT
                        }
                    }

                    /**
                     * If ApiResult.Error or ApiResult.Exception,
                     * change state to [KakaoSearchUiState.ERROR]
                     */
                    else -> {
                        _kakaoSearchState.value = KakaoSearchUiState.ERROR
                    }
                }
            }
        }
    }

    fun toggleFavorite(mediaDetailData: KakaoSearchMediaItemData) {
        viewModelScope.launch {
            if (mediaDetailData.isFavorite) {
                removeKakaoFavoriteMediaItem(mediaDetailData.mediaInfo)
            } else {
                addKakaoFavoriteMediaItem(mediaDetailData.mediaInfo)
            }

            _KakaoMediaItemList.update { previousSearchMediaListState ->
                previousSearchMediaListState.copy(
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

    // restore ui state from error
    fun onClearUiState() {
        _kakaoSearchState.value = KakaoSearchUiState.IDLE
    }
}

const val KakaoSearchPageSize = 50

enum class KakaoSearchUiState {
    IDLE,
    LOADING,
    EMPTY,
    SHOW_RESULT,
    ERROR,
}