package com.daryeou.app.feature.kakaosearch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.daryeou.app.feature.kakaosearch.KakaoSearchRoute

const val kakaoSearchNavigationRoute = "kakao_search_route"

fun NavController.navigateToKakaoSearch(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(kakaoSearchNavigationRoute, navOptions)
}

fun NavGraphBuilder.kakaoSearchScreen(
    showSnackbar: (String) -> Unit,
) {
    composable(
        route = kakaoSearchNavigationRoute,
    ) { _ ->
        KakaoSearchRoute(
            showSnackbar = showSnackbar,
        )
    }
}