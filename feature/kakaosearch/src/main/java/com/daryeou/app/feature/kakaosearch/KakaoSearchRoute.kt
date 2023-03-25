package com.daryeou.app.feature.kakaosearch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun KakaoSearchRoute(
    viewModel: KakaoSearchViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit,
) {
    val kakaoSearchState by viewModel.kakaoSearchState.collectAsStateWithLifecycle()
    val kakaoMediaItemList by viewModel.kakaoMediaItemList.collectAsStateWithLifecycle()

    KakaoSearchScreen(
        kakaoSearchMediaListState = kakaoMediaItemList,
        kakaoSearchState = kakaoSearchState,
        onQuery = { query ->
            viewModel.searchMedia(query)
        },
        onNextPage = viewModel::searchMediaNextPage
        ,
        onClickFavorite = { mediaDetailData ->
            viewModel.toggleFavorite(mediaDetailData)
        },
        onClearState = viewModel::onClearUiState,
        onError = showSnackbar,
    )
}