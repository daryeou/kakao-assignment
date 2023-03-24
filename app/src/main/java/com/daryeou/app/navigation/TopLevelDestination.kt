package com.daryeou.app.navigation


import com.daryeou.app.core.designsystem.icon.AppIcons
import com.daryeou.app.core.designsystem.icon.IconWrapper
import com.daryeou.app.core.designsystem.icon.IconWrapper.ImageVectorIcon
import com.daryeou.app.feature.kakaofavorite.navigation.kakaoFavoriteNavigationRoute
import com.daryeou.app.feature.kakaofavorite.R as kakaoFavoriteR
import com.daryeou.app.feature.kakaosearch.navigation.kakaoSearchNavigationRoute
import com.daryeou.app.feature.kakaosearch.R as kakaoSearchR

enum class TopLevelDestination(
    val route: String,
    val selectedIcon: IconWrapper,
    val unselectedIcon: IconWrapper,
    val iconTextId: Int,
) {
    Search(
        kakaoSearchNavigationRoute,
        ImageVectorIcon(AppIcons.SearchFilled),
        ImageVectorIcon(AppIcons.Search),
        kakaoSearchR.string.kakao_search,
    ),
    Favorite(
        kakaoFavoriteNavigationRoute,
        ImageVectorIcon(AppIcons.FavoriteFilled),
        ImageVectorIcon(AppIcons.Favorite),
        kakaoFavoriteR.string.kakao_favorite,
    ),
}