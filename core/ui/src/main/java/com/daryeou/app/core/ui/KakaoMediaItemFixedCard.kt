import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.daryeou.app.core.designsystem.icon.AppIcons
import com.daryeou.app.core.designsystem.theme.AppTheme
import com.daryeou.app.core.model.kakao.KakaoSearchMediaBasicData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaItemData
import com.daryeou.app.core.model.kakao.KakaoSearchMediaType
import java.util.Date

@Composable
fun KakaoMediaItemFixedCard(
    modifier: Modifier = Modifier,
    itemData: KakaoSearchMediaItemData,
    onClickImage: (KakaoSearchMediaItemData) -> Unit,
    onClickLink: (KakaoSearchMediaItemData) -> Unit,
    onClickFavorite: (KakaoSearchMediaItemData) -> Unit,
) {
    Card(
        modifier = modifier
            .clickable(
                onClick = {
                    onClickImage(itemData)
                },
            ),
        shape = RectangleShape,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            AsyncImage(
                model = itemData.mediaInfo.thumbnailUrl,
                contentDescription = "Media Image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = AppIcons.OpenInNew,
                    contentDescription = "OpenWeb",
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
internal fun MediaCardPreview() {
    AppTheme() {
        Box(modifier = Modifier.fillMaxSize()) {
            KakaoMediaItemFixedCard(
                modifier = Modifier
                    .size(240.dp),
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
                onClickImage = { },
                onClickLink = { },
                onClickFavorite = { },
            )
        }
    }
}