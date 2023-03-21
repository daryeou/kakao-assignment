package com.daryeou.app.feature.kakaosearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daryeou.app.core.model.kakao.KakaoSearchMediaDetailData
import com.daryeou.app.core.ui.KakaoMediaItemCard

@Immutable
data class KakaoSearchMediaListState(
    val pageable: Boolean,
    val page: Int,
    val mediaList: List<KakaoSearchMediaDetailData>,
)

private const val CELL_COUNT = 2
private val lastItemSpan: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(CELL_COUNT) }

@Composable
internal fun KakaoSearchResultGrid(
    modifier: Modifier = Modifier,
    kakaoMediaItemList: KakaoSearchMediaListState,
    onNextPage: () -> Unit,
    onClickImage: (KakaoSearchMediaDetailData) -> Unit,
    onClickFavorite: (KakaoSearchMediaDetailData) -> Unit,
) {
    val gridState = rememberLazyGridState()
    val shouldStartPaginate by remember(kakaoMediaItemList) {
        derivedStateOf {
            kakaoMediaItemList.pageable &&
                    (gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: 0) >= kakaoMediaItemList.mediaList.size - 1
        }
    }

    LaunchedEffect(shouldStartPaginate) {
        if (shouldStartPaginate) {
            onNextPage()
        }
    }

    LazyVerticalGrid(
        modifier = modifier,
        state = gridState,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(
            count = kakaoMediaItemList.mediaList.size,
        ) { index ->
            val mediaItem = kakaoMediaItemList.mediaList[index]
            KakaoMediaItemCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                detailData = mediaItem,
                onClickImage = {
                    onClickImage(mediaItem)
                },
                onClickFavorite = {
                    onClickFavorite(mediaItem)
                },
            )
        }

        item(
            span = lastItemSpan,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
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