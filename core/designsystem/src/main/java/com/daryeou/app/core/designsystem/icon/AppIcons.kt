package com.daryeou.app.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Collections
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

object AppIcons {
    val ArrowBack = Icons.Outlined.ArrowBackIos
    val Close = Icons.Outlined.Close
    val Collection = Icons.Outlined.Collections
    val CollectionFilled = Icons.Rounded.Collections
    val Favorite = Icons.Outlined.FavoriteBorder
    val FavoriteFilled = Icons.Outlined.Favorite
    val Search = Icons.Outlined.Search
    val SearchFilled = Icons.Rounded.Search
    val OpenInNew = Icons.Outlined.OpenInNew
    val Image = Icons.Outlined.Image
    val Video = Icons.Outlined.Movie
}

sealed class IconWrapper {
    data class ImageVectorIcon(val imageVector: ImageVector) : IconWrapper()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : IconWrapper()
}