package com.daryeou.app.feature.kakaosearch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun KakaoSearchRoute(
    viewModel: KakaoSearchViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    val kakaoSearchState by viewModel.kakaoSearchState.collectAsStateWithLifecycle()
    val kakaoMediaItemList by viewModel.kakaoMediaItemList.collectAsStateWithLifecycle()

    val queryLengthErrorMessage = stringResource(id = R.string.kakao_search_query_length_error)
    val searchErrorMessage = stringResource(id = R.string.kakao_search_api_error)

    var queryValue by remember { mutableStateOf("") }

    KakaoSearchScreen(
        kakaoSearchMediaListState = kakaoMediaItemList,
        kakaoSearchState = kakaoSearchState,
        queryValue = queryValue,
        onTextInputEvent = { query ->
            queryValue = query
        },
        onQuery = { query ->
            if (query.isEmpty()) {
                showSnackbar(queryLengthErrorMessage)
            } else {
                viewModel.searchMedia(query)
            }
        },
        onBackPress = {
            TODO()
        },
        onNextPage = {
            viewModel.searchMediaNextPage()
        },
        onClickLink = { mediaDetailData ->
            uriHandler.openUri(mediaDetailData.mediaInfo.url)
        },
        onClickFavorite = { mediaDetailData ->
            viewModel.toggleFavorite(mediaDetailData)
        },
        onSearchError = {
            showSnackbar(searchErrorMessage)
            viewModel.onClearError()
        },
    )
}