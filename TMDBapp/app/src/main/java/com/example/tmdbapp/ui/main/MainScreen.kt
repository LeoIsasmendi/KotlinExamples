package com.example.tmdbapp.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tmdbapp.navigation.BottomNavItem
import com.example.tmdbapp.navigation.Screen
import com.example.tmdbapp.ui.list.MovieListScreen
import com.example.tmdbapp.ui.search.SearchScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    appNavController: NavHostController
) {
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
    )
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            bottomNavController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        BottomNavGraph(
            mainNavController = appNavController, // Pass the main app NavController
            bottomNavController = bottomNavController,
            paddingValues = innerPadding
        )
    }
}

@Composable
fun BottomNavGraph(
    mainNavController: NavHostController,
    bottomNavController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = bottomNavController,
        startDestination = BottomNavItem.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(BottomNavItem.Home.route) {
            MovieListScreen(
                onMovieClick = { movieId, itemType ->
                    mainNavController.navigate(
                        Screen.movieDetailRoute(movieId, itemType)
                    )
                }
            )
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(
                onMovieClick = { movieId, itemType ->
                    mainNavController.navigate(
                        Screen.movieDetailRoute(movieId, itemType)
                    )
                }
            )
        }
    }
}