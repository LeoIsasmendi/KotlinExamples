package com.example.tmdbapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Filled.Home
    )

    data object Search : BottomNavItem(
        route = "search",
        title = "Search",
        icon = Icons.Filled.Search
    )
}