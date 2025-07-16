package com.example.tmdbapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tmdbapp.ui.detail.DetailScreen
import com.example.tmdbapp.ui.list.MovieListScreen
import com.example.tmdbapp.ui.main.MainScreen
import com.example.tmdbapp.ui.search.SearchScreen

object Screen {
    const val MAIN = "mainGraph"
    const val MOVIE_SEARCH = "movieSearch"
    const val MOVIE_LIST = "movieList"
    const val MOVIE_DETAIL = "movieDetail/{itemId}/{itemType}" // Path parameters

    fun movieDetailRoute(itemId: Int, itemType: String) = "movieDetail/$itemId/$itemType"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MAIN) {
        composable(Screen.MAIN) {
            MainScreen(appNavController = navController)
        }
        composable(Screen.MOVIE_SEARCH) {
            SearchScreen(onMovieClick = { itemId, itemType ->
                navController.navigate(Screen.movieDetailRoute(itemId, itemType))
            })
        }
        composable(Screen.MOVIE_LIST) {
            MovieListScreen(
                onMovieClick = { itemId, itemType ->
                    navController.navigate(Screen.movieDetailRoute(itemId, itemType))
                }
            )
        }
        composable(
            route = Screen.MOVIE_DETAIL,
            arguments = listOf(
                navArgument("itemId") { type = NavType.IntType },
                navArgument("itemType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            val itemType = backStackEntry.arguments?.getString("itemType") ?: "movie"
            DetailScreen(
                itemId = itemId,
                itemType = itemType,
                onNavigateUp = { navController.popBackStack() }
            )
        }
    }
}