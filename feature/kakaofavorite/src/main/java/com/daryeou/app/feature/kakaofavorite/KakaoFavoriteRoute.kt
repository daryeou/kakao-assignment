package com.daryeou.app.feature.kakaofavorite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun KakaoFavoriteRoute(
    viewModel: KakaoFavoriteViewModel = hiltViewModel(),
) {
    val uriHandler = LocalUriHandler.current

    val kakaoFavoriteUiState by viewModel.kakaoFavoriteUiState.collectAsStateWithLifecycle()
    val kakaoFavoriteList by viewModel.kakaoFavoriteList.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.getKakaoFavoriteList()
    }

    KakaoFavoriteScreen(
        kakaoFavoriteList = kakaoFavoriteList,
        kakaoFavoriteUiState = kakaoFavoriteUiState,
        onClickLink = { mediaItemData ->
            uriHandler.openUri(mediaItemData.mediaInfo.url)
        },
        onClickImage = { mediaItemData ->
            uriHandler.openUri(mediaItemData.mediaInfo.url)
        },
        onClickFavorite = { mediaItemData ->
            viewModel.removeKakaoFavoriteItem(mediaItemData)
        },
    )
}