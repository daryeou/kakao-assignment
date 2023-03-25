package com.daryeou.app.feature.kakaosearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
import com.daryeou.app.core.ui.KakaoMediaItemCompatCard

@Immutable
data class KakaoSearchMediaListState(
    val pageable: Boolean,
    val page: Int,
    val mediaList: List<KakaoSearchMediaItemData>,
)

@Composable
internal fun KakaoSearchResultColumn(
    modifier: Modifier = Modifier,
    kakaoMediaItemList: KakaoSearchMediaListState,
    onNextPage: () -> Unit,
    onClickLink: (KakaoSearchMediaItemData) -> Unit,
    onClickFavorite: (KakaoSearchMediaItemData) -> Unit,
) {
    val listState = rememberLazyListState()

    val shouldStartPaginate by remember(kakaoMediaItemList) {
        derivedStateOf {
            kakaoMediaItemList.pageable &&
                    (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: 0) >= kakaoMediaItemList.mediaList.size - 1
        }
    }

    LaunchedEffect(shouldStartPaginate) {
        if (shouldStartPaginate) {
            onNextPage()
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(
            count = kakaoMediaItemList.mediaList.size,
        ) { index ->
            val mediaItem = kakaoMediaItemList.mediaList[index]
            var expanded by rememberSaveable() { mutableStateOf(false) }

            KakaoMediaItemCompatCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                itemData = mediaItem,
                isExpanded = expanded,
                onClickLink = onClickLink,
                onClickImage = {
                    expanded = !expanded
                },
                onClickFavorite = onClickFavorite,
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                if (kakaoMediaItemList.pageable) {
                    KakaoSearchLoadingItem()
                } else {
                    KakaoSearchLastItem()
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.KakaoSearchLoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .padding(8.dp)
            .align(Alignment.CenterHorizontally)
    )
    Text(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        text = stringResource(id = R.string.kakao_search_media_item_loading),
    )
}

@Composable
private fun KakaoSearchLastItem() {
    Text(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        text = stringResource(id = R.string.kakao_search_media_item_last),
    )
}