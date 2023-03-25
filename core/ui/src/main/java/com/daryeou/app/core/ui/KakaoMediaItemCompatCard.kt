package com.daryeou.app.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.daryeou.app.core.designsystem.icon.AppIcons
import com.daryeou.app.core.designsystem.theme.AppTheme
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun KakaoMediaItemCompatCard(
    modifier: Modifier = Modifier,
    itemData: KakaoSearchMediaItemData,
    isExpanded: Boolean = false,
    onClickImage: (KakaoSearchMediaItemData) -> Unit,
    onClickLink: (KakaoSearchMediaItemData) -> Unit,
    onClickFavorite: (KakaoSearchMediaItemData) -> Unit,
) {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val createdDateText = formatter.format(itemData.mediaInfo.dateTime)

    var imageWidthTarget by rememberSaveable { mutableStateOf(0.3f) }
    var captionBackgroundAlphaTarget by rememberSaveable { mutableStateOf(0f) }
    var titlePaddingTarget by rememberSaveable { mutableStateOf(0.3f) }

    val imageWidthFraction: Float by animateFloatAsState(
        targetValue = imageWidthTarget,
        animationSpec = tween(
            durationMillis = 300,
        ),
    )

    val captionBackgroundAlpha: Float by animateFloatAsState(
        targetValue = captionBackgroundAlphaTarget,
        animationSpec = tween(
            durationMillis = 300,
        ),
    )

    val titlePaddingFraction: Float by animateFloatAsState(
        targetValue = titlePaddingTarget,
        animationSpec = tween(
            durationMillis = 300,
        ),
    )

    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            imageWidthTarget = 1f
            captionBackgroundAlphaTarget = 0.5f
            titlePaddingTarget = 0f
        } else {
            imageWidthTarget = 0.3f
            captionBackgroundAlphaTarget = 0f
            titlePaddingTarget = 0.3f
        }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(
                    onClick = {
                        onClickImage(itemData)
                    },
                ),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(itemData.mediaInfo.thumbnailUrl)
                    .size(Size.ORIGINAL)
                    .build(),
                contentDescription = "Media Image",
                modifier = Modifier
                    .fillMaxWidth(imageWidthFraction)
                    .aspectRatio(1f),
                contentScale = ContentScale.FillWidth,
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(
                            alpha = captionBackgroundAlpha,
                        ),
                    )
                    .padding(start = maxWidth * titlePaddingFraction)
                    .padding(16.dp),
            ) {
                Text(
                    text = itemData.mediaInfo.title,
                    modifier = Modifier
                        .wrapContentHeight(),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = createdDateText,
                    modifier = Modifier
                        .wrapContentHeight(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(
                            alpha = captionBackgroundAlpha,
                        ),
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .size(36.dp)
                        .padding(6.dp),
                    imageVector = if (itemData.mediaInfo.mediaType == KakaoSearchMediaType.IMAGE) {
                        AppIcons.Image
                    } else {
                        AppIcons.Video
                    },
                    contentDescription = "Media Type",
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = AppIcons.OpenInNew,
                    contentDescription = "Open Web",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(
                            shape = CircleShape
                        )
                        .clickable(
                            onClick = {
                                onClickLink(itemData)
                            },
                        )
                        .padding(6.dp)
                )
                Icon(
                    imageVector = if (itemData.isFavorite) {
                        AppIcons.FavoriteFilled
                    } else {
                        AppIcons.Favorite
                    },
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(
                            shape = CircleShape
                        )
                        .clickable(
                            onClick = {
                                onClickFavorite(itemData)
                            },
                        )
                        .padding(6.dp)
                )
            }
        }

    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
@Composable
internal fun MediaCompatCardPreview() {
    AppTheme() {
        var expanded by rememberSaveable() { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxSize()) {
            KakaoMediaItemCompatCard(
                modifier = Modifier,
                isExpanded = expanded,
                itemData = KakaoSearchMediaItemData(
                    isFavorite = false,
                    KakaoSearchMediaBasicData(
                        title = "Title",
                        url = "https://www.naver.com",
                        thumbnailUrl = "https://www.simplilearn.com/ice9/free_resources_article_thumb/what_is_image_Processing.jpg",
                        dateTime = Date(),
                        mediaType = KakaoSearchMediaType.IMAGE,
                    )
                ),
                onClickLink = {},
                onClickImage = {
                    expanded = !expanded
                },
                onClickFavorite = {},
            )
        }
    }
}