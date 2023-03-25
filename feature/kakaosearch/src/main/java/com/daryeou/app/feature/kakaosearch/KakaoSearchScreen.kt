package com.daryeou.app.feature.kakaosearch

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.daryeou.app.core.designsystem.component.LoadingWheel
import com.daryeou.app.core.designsystem.theme.AppTheme
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaType
import com.daryeou.app.feature.kakaosearch.component.KakaoSearchBar
import java.util.Date

@Composable
internal fun KakaoSearchScreen(
    kakaoSearchState: KakaoSearchUiState,
    kakaoSearchMediaListState: KakaoSearchMediaListState,
    onQuery: (String) -> Unit,
    onNextPage: () -> Unit,
    onClickFavorite: (KakaoSearchMediaItemData) -> Unit,
    onClearState: () -> Unit,
    onError: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    val queryLengthErrorMessage = stringResource(id = R.string.kakao_search_query_length_error)
    val searchErrorMessage = stringResource(id = R.string.kakao_search_api_error)

    var showBackButton by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        KakaoSearchBar(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            active = false,
            onQuery = { query ->
                if (query.isEmpty()) {
                    onError(queryLengthErrorMessage)
                } else if (kakaoSearchState != KakaoSearchUiState.LOADING) {
                    onQuery(query)
                    showBackButton = true
                }
            },
            showBackButton = showBackButton,
            onBackPress = {
                onClearState()
                showBackButton = false
            },
        )

        when (kakaoSearchState) {
            KakaoSearchUiState.IDLE -> {
                KakaoSearchIdleColumn()
            }

            KakaoSearchUiState.LOADING -> {
                LoadingWheel()
            }

            KakaoSearchUiState.EMPTY -> {
                KakaoSearchEmpty()
            }

            KakaoSearchUiState.ERROR -> {
                onError(searchErrorMessage)
            }

            KakaoSearchUiState.SHOW_RESULT -> {
                KakaoSearchResultColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    kakaoMediaItemList = kakaoSearchMediaListState,
                    onNextPage = onNextPage,
                    onClickLink = { mediaDetailData ->
                        uriHandler.openUri(mediaDetailData.mediaInfo.url)
                    },
                    onClickFavorite = onClickFavorite,
                )
            }
        }
    }
}

@Composable
private fun KakaoSearchIdleColumn() {
    Text(
        modifier = Modifier
            .padding(36.dp),
        text = stringResource(id = R.string.kakao_search_headline),
        style = MaterialTheme.typography.headlineLarge,
        softWrap = true,
        lineHeight = 48.sp,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun KakaoSearchEmpty() {
    val emptyLottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.lottie_empty)
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .wrapContentHeight(),
            composition = emptyLottieComposition,
            iterations = LottieConstants.IterateForever,
        )
        Text(
            text = stringResource(id = R.string.kakao_search_media_item_empty),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
@Composable
internal fun KakaoSearchScreenPreview() {
    AppTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { _ ->
            KakaoSearchScreen(
                kakaoSearchMediaListState = KakaoSearchMediaListState(
                    pageable = true,
                    page = 1,
                    mediaList = MutableList(10) {
                        KakaoSearchMediaItemData(
                            isFavorite = false,
                            mediaInfo = KakaoSearchMediaBasicData(
                                title = "title",
                                url = "https://www.naver.com",
                                thumbnailUrl = "https://pbs.twimg.com/media/Frj3K70akAAurBa?format=jpg&name=small",
                                dateTime = Date(),
                                mediaType = KakaoSearchMediaType.values().random(),
                            )
                        )
                    }
                ),
                kakaoSearchState = KakaoSearchUiState.IDLE,
                onQuery = {},
                onNextPage = {},
                onClickFavorite = {},
                onClearState = {},
                onError = {},
            )
        }
    }
}

@Preview
@Composable
internal fun KakaoSearchIdlePreview() {
    AppTheme {
        KakaoSearchIdleColumn()
    }
}

@Preview
@Composable
internal fun KakaoSearchEmptyPreview() {
    AppTheme {
        KakaoSearchEmpty()
    }
}