package com.daryeou.app.feature.kakaosearch.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import com.daryeou.app.core.designsystem.icon.AppIcons
import com.daryeou.app.feature.kakaosearch.R
import kotlinx.coroutines.delay

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class
)
@Composable
internal fun KakaoSearchBar(
    modifier: Modifier = Modifier,
    active: Boolean = false,
    query: String = "",
    onQueryChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit = {},
    onQuery: () -> Unit,
    showBackButton: Boolean = false,
    onBackPress: () -> Unit = {},
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



    val keyboardController = LocalSoftwareKeyboardController.current
    val onSearch = {
        keyboardController?.hide()
        onQuery()
    }

    val onBackPressEvent = {
        keyboardController?.hide()
        onQueryChange("")
        onBackPress()
    }

    BackHandler(enabled = showBackButton) {
        onBackPressEvent()
    }

    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        query = query,
        onQueryChange = { textInput ->
            onQueryChange(textInput)
        },
        active = active,
        onActiveChange = onActiveChange,
        onSearch = { onSearch() },
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
        leadingIcon = {
            AnimatedVisibility(visible = showBackButton) {
                IconButton(
                    onClick = onBackPressEvent,
                ) {
                    Icon(
                        imageVector = AppIcons.Close,
                        contentDescription = "Close Icon"
                    )
                }
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { onSearch() },
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