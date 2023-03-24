package com.daryeou.app.feature.kakaofavorite.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.daryeou.app.feature.kakaofavorite.KakaoFavoriteRoute

const val kakaoFavoriteNavigationRoute = "kakao_favorite_route"

fun NavController.navigateToKakaoFavorite(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(kakaoFavoriteNavigationRoute, navOptions)
}

fun NavGraphBuilder.kakaoFavoriteScreen(
    showSnackbar: (String) -> Unit,
) {
    composable(
        route = kakaoFavoriteNavigationRoute,
    ) { _ ->
        KakaoFavoriteRoute(
            showSnackbar = showSnackbar,
        )
    }
}