package com.daryeou.app.feature.kakaosearch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    /**
     * Refresh the UI state when the screen is launched.
     * If change favorite state, the UI state will be changed.
     */
    LaunchedEffect(true) {
        viewModel.onRefresh()
    }

    KakaoSearchScreen(
        kakaoSearchMediaListState = kakaoMediaItemList,
        kakaoSearchState = kakaoSearchState,
        onQuery = { query ->
            viewModel.onQuery(query)
        },
        onNextPage = { query, page ->
            viewModel.onNextPage(query, page)
        },
        onClickFavorite = { mediaDetailData ->
            viewModel.toggleFavorite(mediaDetailData)
        },
        onClearState = viewModel::onClearUiState,
        onError = showSnackbar,
    )
}