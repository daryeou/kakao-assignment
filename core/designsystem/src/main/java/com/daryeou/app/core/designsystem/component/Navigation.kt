package com.daryeou.app.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daryeou.app.core.designsystem.component.NavigationDefaults.navigationBackgroundColor
import com.daryeou.app.core.designsystem.icon.IconWrapper
import com.daryeou.app.core.designsystem.icon.IconWrapper.ImageVectorIcon
import com.daryeou.app.core.designsystem.icon.IconWrapper.DrawableResourceIcon

@Composable
fun AppNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp,
        )
        NavigationBar(
            modifier = modifier,
            tonalElevation = 0.dp,
            containerColor = navigationBackgroundColor(),
            content = content,
        )
    }
}

@Composable
fun RowScope.AppNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    unselectedIcon: IconWrapper,
    selectedIcon: IconWrapper = unselectedIcon,
    enabled: Boolean = true,
    @StringRes labelId: Int,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            val icon = if (selected) {
                selectedIcon
            } else {
                unselectedIcon
            }
            when (icon) {
                is ImageVectorIcon -> Icon(
                    imageVector = icon.imageVector,
                    contentDescription = "icon",
                )

                is DrawableResourceIcon -> Icon(
                    painter = painterResource(id = icon.id),
                    contentDescription = "icon"
                )
            }
        },
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(text = stringResource(id = labelId))
        },
        alwaysShowLabel = alwaysShowLabel,
    )
}

private object NavigationDefaults {
    val navigationBackgroundColor = @Composable {
        MaterialTheme.colorScheme.surface
    }
}