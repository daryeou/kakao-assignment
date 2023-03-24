package com.daryeou.app.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.daryeou.app.feature.kakaofavorite.navigation.kakaoFavoriteScreen
import com.daryeou.app.feature.kakaosearch.navigation.kakaoSearchScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun KakaoNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.Search.route,
    ) {
        kakaoSearchScreen(
            showSnackbar = { errorMsg ->
                snackbarHostState.showMessage(coroutineScope, errorMsg)
            },
        )
        kakaoFavoriteScreen(
            showSnackbar = { errorMsg ->
                snackbarHostState.showMessage(coroutineScope, errorMsg)
            },
        )
    }
}

private fun SnackbarHostState.showMessage(
    coroutineScope: CoroutineScope,
    text: String,
) {
    coroutineScope.launch {
        currentSnackbarData?.dismiss()
        showSnackbar(text)
    }
}
