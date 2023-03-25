package com.daryeou.app.feature.kakaosearch

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.daryeou.app.core.designsystem.icon.AppIcons
import com.daryeou.app.core.designsystem.theme.AppTheme
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaType
import kotlinx.coroutines.delay
import java.util.Date

@Composable
internal fun KakaoSearchScreen(
    kakaoSearchState: KakaoSearchUiState,
    kakaoSearchMediaListState: KakaoSearchMediaListState,
    queryValue: String,
    onBackPress: () -> Unit,
    onTextInputEvent: (String) -> Unit,
    onQuery: (String) -> Unit,
    onNextPage: () -> Unit,
    onClickLink: (KakaoSearchMediaItemData) -> Unit,
    onClickFavorite: (KakaoSearchMediaItemData) -> Unit,
    onSearchError: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            if (kakaoSearchState != KakaoSearchUiState.IDLE) {
                IconButton(
                    onClick = {
                        onBackPress()
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.ArrowBack,
                        contentDescription = "Back Icon"
                    )
                }
            }
            KakaoSearchBar(
                queryValue = queryValue,
                onTextInputEvent = onTextInputEvent,
                onQuery = onQuery,
            )
        }


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
                onSearchError()
            }

            KakaoSearchUiState.SHOW_RESULT -> {
                KakaoSearchResultColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    kakaoMediaItemList = kakaoSearchMediaListState,
                    onNextPage = onNextPage,
                    onClickLink = onClickLink,
                    onClickFavorite = onClickFavorite,
                )
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class
)
@Composable
private fun KakaoSearchBar(
    queryValue: String,
    onTextInputEvent: (String) -> Unit,
    onQuery: (String) -> Unit,
) {
    val context = LocalContext.current
    val placeholderTextArray =
        remember { context.resources.getStringArray(R.array.kakao_search_placeholder) }
    val placeholderText by produceState(initialValue = placeholderTextArray.first()) {
        var index = 0
        while (true) {
            index++
            if (index >= placeholderTextArray.size) {
                index = 0
            }
            value = placeholderTextArray[index]
            delay(3000)
        }
    }

    var searchbarActive by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth(),
        query = queryValue,
        onQueryChange = onTextInputEvent,
        active = searchbarActive,
        onActiveChange = { active ->
            searchbarActive = active
        },
        onSearch = { query ->
            searchbarActive = false
            onQuery(query)
        },
        placeholder = {
            AnimatedContent(
                targetState = placeholderText,
                transitionSpec = {
                    val contentTransform =
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    contentTransform.using(SizeTransform(clip = false))
                }
            ) { targetString ->
                Text(
                    text = targetString,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    searchbarActive = false
                    onQuery(queryValue)
                },
            ) {
                Icon(
                    imageVector = AppIcons.Search,
                    contentDescription = "Search Icon"
                )
            }
        },
        content = {}
    )
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
                        KakaoSearchMediaItemData(
                            isFavorite = false,
                            mediaInfo = KakaoSearchMediaBasicData(
                                title = "title",
                                url = "https://www.naver.com",
                                thumbnailUrl = "https://pbs.twimg.com/media/Frj3K70akAAurBa?format=jpg&name=small",
                                dateTime = Date(),
                                mediaType = KakaoSearchMediaType.IMAGE,
                            )
                        )
                    }.apply {
                        addAll(
                            MutableList(10) {
                                KakaoSearchMediaItemData(
                                    isFavorite = true,
                                    mediaInfo = KakaoSearchMediaBasicData(
                                        title = "title",
                                        url = "https://www.naver.com",
                                        thumbnailUrl = "https://pbs.twimg.com/media/Frj3K70akAAurBa?format=jpg&name=small",
                                        dateTime = Date(),
                                        mediaType = KakaoSearchMediaType.VIDEO,
                                    )
                                )
                            }
                        )
                    }
                ),
                kakaoSearchState = KakaoSearchUiState.IDLE,
                queryValue = "Hello",
                onTextInputEvent = {},
                onQuery = {},
                onBackPress = {},
                onNextPage = {},
                onClickLink = {},
                onClickFavorite = {},
                onSearchError = {}
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