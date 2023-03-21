package com.daryeou.app.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.daryeou.app.core.designsystem.icon.AppIcons
import com.daryeou.app.core.designsystem.theme.AppTheme
import com.daryeou.app.core.designsystem.theme.border
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaDetailData
import java.util.Date

@Composable
fun KakaoMediaItemCard(
    modifier: Modifier = Modifier,
    detailData: KakaoSearchMediaDetailData,
    onClickImage: () -> Unit,
    onClickFavorite: () -> Unit,
) {
    Card(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                onClick = onClickImage,
            ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.border,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            AsyncImage(
                model = detailData.mediaInfo.thumbnailUrl,
                contentDescription = "Media Image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Icon(
                imageVector = if (detailData.isFavorite) {
                    AppIcons.FavoriteFilled
                } else {
                    AppIcons.Favorite
                },
                contentDescription = "Favorite",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .fillMaxSize(0.4f)
                    .clip(
                        shape = CircleShape
                    )
                    .clickable(
                        onClick = onClickFavorite,
                    )
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
@Composable
internal fun MediaCardPreview() {
    AppTheme() {
        Box(modifier = Modifier.fillMaxSize()) {
            KakaoMediaItemCard(
                modifier = Modifier
                    .size(240.dp),
                detailData = KakaoSearchMediaDetailData(
                    isFavorite = false,
                    KakaoSearchMediaBasicData(
                        thumbnailUrl = "https://www.simplilearn.com/ice9/free_resources_article_thumb/what_is_image_Processing.jpg",
                        dateTime = Date(),
                    )
                ),
                onClickImage = { },
                onClickFavorite = { },
            )
        }
    }
}