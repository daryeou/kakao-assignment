package com.daryeou.app.feature.kakaosearch

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.daryeou.app.core.designsystem.icon.AppIcons
import com.daryeou.app.core.designsystem.theme.AppTheme
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaDetailData
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun KakaoSearchScreen(
    kakaoSearchMediaListState: KakaoSearchMediaListState,
    kakaoSearchState: KakaoSearchState,
    queryValue: String,
    onTextInputEvent: (String) -> Unit,
    onQuery: (String) -> Unit,
    onNextPage: () -> Unit,
    onClickImage: (KakaoSearchMediaDetailData) -> Unit,
    onClickFavorite: (KakaoSearchMediaDetailData) -> Unit,
    onSearchError: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val onEnterEvent = {
        keyboardController?.hide()
        onQuery(queryValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 8.dp),
            value = queryValue,
            onValueChange = onTextInputEvent,
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onDone = { onEnterEvent() }),
            trailingIcon = {
                IconButton(
                    onClick = onEnterEvent,
                ) {
                    Icon(
                        imageVector = AppIcons.Search,
                        contentDescription = "Search icon"
                    )
                }
            },
        )

        when (kakaoSearchState) {
            KakaoSearchState.IDLE -> {
                KakaoSearchIdleColumn()
            }

            KakaoSearchState.EMPTY -> {
                KakaoSearchEmptyColumn()
            }

            KakaoSearchState.ERROR -> {
                onSearchError()
            }

            KakaoSearchState.SHOW_RESULT -> {
                KakaoSearchResultGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    kakaoMediaItemList = kakaoSearchMediaListState,
                    onNextPage = onNextPage,
                    onClickImage = onClickImage,
                    onClickFavorite = onClickFavorite,
                )
            }
        }

    }
}

@Composable
private fun KakaoSearchIdleColumn() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(
            modifier = Modifier
                .padding(36.dp),
            text = stringResource(id = R.string.kakao_search_idle_description),
            style = MaterialTheme.typography.headlineLarge,
            softWrap = true,
            lineHeight = 48.sp,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun KakaoSearchEmptyColumn() {
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
    val context = LocalContext.current
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
                        KakaoSearchMediaDetailData(
                            isFavorite = false,
                            mediaInfo = KakaoSearchMediaBasicData(
                                thumbnailUrl = "https://pbs.twimg.com/media/Frj3K70akAAurBa?format=jpg&name=small",
                                dateTime = Date(),
                            )
                        )
                    }.apply {
                        addAll(
                            MutableList(10) {
                                KakaoSearchMediaDetailData(
                                    isFavorite = true,
                                    mediaInfo = KakaoSearchMediaBasicData(
                                        thumbnailUrl = "https://pbs.twimg.com/media/Frj3K70akAAurBa?format=jpg&name=small",
                                        dateTime = Date(),
                                    )
                                )
                            }
                        )
                    }
                ),
                kakaoSearchState = KakaoSearchState.IDLE,
                queryValue = "Hello",
                onTextInputEvent = {},
                onQuery = {},
                onNextPage = {
                    Toast.makeText(context, "onNextPage", Toast.LENGTH_SHORT).show()
                },
                onClickImage = {
                    Toast.makeText(context, "onClickImage", Toast.LENGTH_SHORT).show()
                },
                onClickFavorite = {
                    Toast.makeText(context, "onClickFavorite", Toast.LENGTH_SHORT).show()
                },
                onSearchError = {
                    Toast.makeText(context, "onSearchError", Toast.LENGTH_SHORT).show()
                }
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
        KakaoSearchEmptyColumn()
    }
}