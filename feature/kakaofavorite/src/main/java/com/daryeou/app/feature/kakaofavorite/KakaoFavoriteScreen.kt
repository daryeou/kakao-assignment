package com.daryeou.app.feature.kakaofavorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.daryeou.app.core.designsystem.component.LoadingWheel
import com.daryeou.app.core.designsystem.theme.AppTheme
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData

@Composable
internal fun KakaoFavoriteScreen(
    kakaoFavoriteUiState: KakaoFavoriteUiState,
    kakaoFavoriteList: List<KakaoSearchMediaItemData>,
    onClickLink: (KakaoSearchMediaItemData) -> Unit,
    onClickImage: (KakaoSearchMediaItemData) -> Unit,
    onClickFavorite: (KakaoSearchMediaItemData) -> Unit,
    showSnackbar: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.kakao_favorite_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp),
        )

        when (kakaoFavoriteUiState) {
            KakaoFavoriteUiState.LOADING -> {
                LoadingWheel()
            }

            KakaoFavoriteUiState.SUCCESS -> {
                if (kakaoFavoriteList.isEmpty()) {
                    KakaoFavoriteEmpty()
                } else {
                    KakaoFavoriteGrid(
                        kakaoMediaItemList = KakaoFavoriteMediaGridData(kakaoFavoriteList),
                        onClickLink = onClickLink,
                        onClickImage = onClickImage,
                        onClickFavorite = onClickFavorite,
                    )
                }
            }
        }
    }
}

@Composable
private fun KakaoFavoriteEmpty() {
    val emptyLottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.lottie_empty_box)
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxSize(0.5f),
            composition = emptyLottieComposition,
            iterations = LottieConstants.IterateForever,
        )
        Text(
            text = stringResource(id = R.string.kakao_favorite_empty),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
internal fun KakaoFavoriteScreenPreview() {
    AppTheme {
        KakaoFavoriteScreen(
            kakaoFavoriteUiState = KakaoFavoriteUiState.SUCCESS,
            kakaoFavoriteList = listOf(),
            onClickLink = {},
            onClickImage = {},
            onClickFavorite = {},
            showSnackbar = {},
        )
    }
}