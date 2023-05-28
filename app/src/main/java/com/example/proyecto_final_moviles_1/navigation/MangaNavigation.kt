package com.example.proyecto_final_moviles_1.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_final_moviles_1.screens.MangaSplashScreen
import com.example.proyecto_final_moviles_1.screens.details.BookDetailsScreen
import com.example.proyecto_final_moviles_1.screens.home.Home
import com.example.proyecto_final_moviles_1.screens.login.LoginScreen
import com.example.proyecto_final_moviles_1.screens.search.MangaSearchViewModel
import com.example.proyecto_final_moviles_1.screens.search.SearchScreen
import com.example.proyecto_final_moviles_1.screens.stats.StatsScreen
import com.example.proyecto_final_moviles_1.screens.update.BookUpdateScreen

@Composable
fun MangaNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MangaScreens.SplashScreen.name ){


        composable(MangaScreens.SplashScreen.name){
            MangaSplashScreen(navController = navController) // se pasa esto poder cambiar el screen
        }

        composable(MangaScreens.MangaHomeScreen.name){
            Home(navController = navController)
        }

        composable(MangaScreens.LoginScreen.name){
            LoginScreen(navController = navController)
        }

        composable(MangaScreens.DetailsScreen.name){
            BookDetailsScreen(navController = navController)
        }

        composable(MangaScreens.SearchScreen.name){
            val SearchViewModel = hiltViewModel<MangaSearchViewModel>()
            SearchScreen(navController = navController, viewModel=SearchViewModel)
        }

        composable(MangaScreens.MangaStatsScreen.name){
            StatsScreen(navController = navController)
        }

        composable(MangaScreens.UpdateScreen.name){
            BookUpdateScreen(navController = navController)
        }


    }
}