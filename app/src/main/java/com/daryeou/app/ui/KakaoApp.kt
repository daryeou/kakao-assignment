package com.daryeou.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.daryeou.app.core.designsystem.component.AppNavigationBar
import com.daryeou.app.core.designsystem.component.AppNavigationBarItem
import com.daryeou.app.navigation.KakaoNavHost
import com.daryeou.app.navigation.TopLevelDestination

@Composable
fun KakaoApp() {
    val navHostController = rememberNavController()
    val backStackEntry by navHostController.currentBackStackEntryAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            AppBottomBar(
                destination = TopLevelDestination.values().toList(),
                onNavigateToDestination = { destination ->
                    navHostController.navigate(destination.route) {
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                currentDestination = backStackEntry?.destination
            )
        },
    ) { contentPadding ->
        Box(
            modifier =
            Modifier.padding(contentPadding)
        ) {
            KakaoNavHost(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navHostController,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}

@Composable
private fun AppBottomBar(
    destination: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    AppNavigationBar(
        modifier = modifier,
    ) {
        destination.forEach { destination ->
            val isSelected = currentDestination?.route == destination.route

            AppNavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateToDestination(destination) },
                unselectedIcon = destination.unselectedIcon,
                selectedIcon = destination.selectedIcon,
                labelId = destination.iconTextId,
                alwaysShowLabel = false,
            )
        }
    }
}