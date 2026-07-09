package com.example.myacceptor.ui.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(
    val route: String,
    val title: String
) {
    object Home : Screen("home", "Home")
    object Apps : Screen("apps", "Apps")
    object Fare : Screen("fare", "Fare Limit")
    object Logs : Screen("logs", "Logs")
    object More : Screen("more", "More")
}



@Composable
fun AppNavHost(
    navController: NavHostController
) {

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(
            Screen.Home.route
        ) {
            HomeScreen(
                navController,
                context
            )
        }

        composable(
            Screen.Apps.route
        ) {
            AppsScreen(
                navController,
                context
            )
        }

        composable(
            Screen.Fare.route
        ) {
            FareLimitScreen(
                navController,
                context
            )
        }

        composable(
            Screen.Logs.route
        ) {
            LogViewerScreen(
                context = context,
            )
        }

        composable(
            Screen.More.route
        ) {
            MoreScreen(
                navController,
                context
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {

    val items = listOf(
        Screen.Logs,
        Screen.Home,
        //Screen.Apps,
        //Screen.Fare,
        Screen.More
    )

    val navBackStack =
        navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStack.value?.destination?.route

    NavigationBar {

        items.forEach { screen ->
            NavigationBarItem(
                selected =
                    currentRoute == screen.route,
                onClick = {
                    navController.navigate(
                        screen.route
                    ) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(
                            navController
                                .graph
                                .startDestinationId
                        ) {
                            saveState = true
                        }
                    }
                },
                icon = {

                    Icon(
                        imageVector = when (screen) {
                            Screen.Home ->
                                Icons.Default.Home
                            Screen.Apps ->
                                Icons.Default.Apps
                            Screen.Fare ->
                                Icons.Default.Tune
                            Screen.Logs ->
                                Icons.Default.List
                            Screen.More ->
                                Icons.Default.Menu

                        },
                        contentDescription =
                            screen.title
                    )
                },

                label = {
                    Text(screen.title)
                }
            )
        }
    }
}