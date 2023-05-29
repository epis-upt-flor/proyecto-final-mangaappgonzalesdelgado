package com.example.proyecto_final_moviles_1.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_final_moviles_1.model.Manga
import com.example.proyecto_final_moviles_1.screens.MangaSplashScreen

import com.example.proyecto_final_moviles_1.screens.details.MangaDetailsScreen
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

        val detailName = MangaScreens.DetailsScreen.name
        composable("$detailName/{id}", arguments = listOf(navArgument("id"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("id").let {

                MangaDetailsScreen(navController = navController,id = it.toString())
            }
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