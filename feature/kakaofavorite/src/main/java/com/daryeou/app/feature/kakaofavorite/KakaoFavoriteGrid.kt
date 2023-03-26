package com.daryeou.app.feature.kakaofavorite

import KakaoMediaItemFixedCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData

@Immutable
data class KakaoFavoriteMediaGridData(
    val mediaList: List<KakaoSearchMediaItemData>,
)

private const val CELL_COUNT = 3
private val gridItemSpan: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(CELL_COUNT) }

@Composable
internal fun KakaoFavoriteGrid(
    modifier: Modifier = Modifier,
    kakaoMediaItemList: KakaoFavoriteMediaGridData,
    onClickLink: (KakaoSearchMediaItemData) -> Unit,
    onClickImage: (KakaoSearchMediaItemData) -> Unit,
    onClickFavorite: (KakaoSearchMediaItemData) -> Unit,
) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxHeight(),
        state = gridState,
        columns = GridCells.Fixed(CELL_COUNT),
        verticalArrangement = remember {
            object : Arrangement.Vertical {
                override fun Density.arrange(
                    totalSize: Int,
                    sizes: IntArray,
                    outPositions: IntArray
                ) {
                    var currentOffset = 0
                    sizes.forEachIndexed { index, size ->
                        if (index == sizes.lastIndex) {
                            outPositions[index] = totalSize - size
                        } else {
                            outPositions[index] = currentOffset
                            currentOffset += (size + 8.dp.toPx()).toInt()
                        }
                    }
                }
            }
        },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = remember { PaddingValues(8.dp) },
    ) {
        items(
            count = kakaoMediaItemList.mediaList.size,
            key = { index -> index },
        ) { index ->
            val mediaItem = kakaoMediaItemList.mediaList[index]

            KakaoMediaItemFixedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                itemData = mediaItem,
                onClickLink = onClickLink,
                onClickImage = onClickImage,
                onClickFavorite = onClickFavorite,
            )
        }

        item(
            span = gridItemSpan,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                KakaoSearchLastItem()
            }
        }
    }
}

@Composable
private fun KakaoSearchLastItem() {
    Text(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        text = stringResource(id = R.string.kakao_favorite_media_item_last),
    )
}