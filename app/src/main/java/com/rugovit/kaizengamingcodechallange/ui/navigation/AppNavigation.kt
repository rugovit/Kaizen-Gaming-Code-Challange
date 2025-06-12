package com.rugovit.kaizengamingcodechallange.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rugovit.kaizengamingcodechallange.ui.features.landing.views.LandingScreen
import com.rugovit.kaizengamingcodechallange.ui.features.sports.views.SportsScreen

object Routes {
    const val LANDING = "landing"
    const val SPORTS = "sports"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.LANDING) {
        composable(Routes.LANDING) {
            LandingScreen(onNavigateToSports = {
                navController.navigate(Routes.SPORTS)
            })
        }
        composable(Routes.SPORTS) {
            SportsScreen()
        }
    }
}

