package com.example.proyecto_final_moviles_1.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_final_moviles_1.screens.MangaSplashScreen
import com.example.proyecto_final_moviles_1.screens.chapters.ImageScreen
import com.example.proyecto_final_moviles_1.screens.chapters.MangaChapterScreen
import com.example.proyecto_final_moviles_1.screens.details.DetailsViewModel

import com.example.proyecto_final_moviles_1.screens.details.MangaDetailsScreen
import com.example.proyecto_final_moviles_1.screens.home.Home
import com.example.proyecto_final_moviles_1.screens.home.HomeScreenViewModel
import com.example.proyecto_final_moviles_1.screens.login.LoginScreen
import com.example.proyecto_final_moviles_1.screens.search.MangaSearchViewModel
import com.example.proyecto_final_moviles_1.screens.search.SearchScreen
import com.example.proyecto_final_moviles_1.screens.stats.StatsScreen
import com.example.proyecto_final_moviles_1.screens.update.BookUpdateScreen

@Composable
fun MangaNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MangaScreens.SplashScreen.name) {


        composable(MangaScreens.SplashScreen.name) {
            MangaSplashScreen(navController = navController) // se pasa esto poder cambiar el screen
        }

        composable(MangaScreens.MangaHomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            Home(navController = navController, viewModel = homeViewModel)
        }

        composable(MangaScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        val detailName = MangaScreens.DetailsScreen.name
        composable("$detailName/{id}", arguments = listOf(navArgument("id") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("id").let {
                val detailsViewModel = hiltViewModel<DetailsViewModel>()
                MangaDetailsScreen(navController = navController, id = it.toString() , viewModel = detailsViewModel)
            }
        }

        composable(MangaScreens.SearchScreen.name) {
            val SearchViewModel = hiltViewModel<MangaSearchViewModel>()
            SearchScreen(navController = navController, viewModel = SearchViewModel)
        }

        composable(MangaScreens.MangaStatsScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            StatsScreen(navController = navController, viewModel = homeViewModel)
        }


        val updateName = MangaScreens.UpdateScreen.name
        composable("$updateName/{MangaItemId}", arguments = listOf(navArgument("MangaItemId") {
            type = NavType.StringType
        })) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("MangaItemId").let {
                BookUpdateScreen(navController = navController, MangaItemId = it.toString())
            }
        }

        val chapterList = MangaScreens.ChaptersScreen.name
        composable("$chapterList/{id}", arguments = listOf(navArgument("id"){
            type = NavType.StringType
        })) {backStackEntry ->
            backStackEntry.arguments?.getString("id").let{

                MangaChapterScreen(navController = navController,id = it.toString())
            }

        }


        composable(MangaScreens.ImageChaptersScreen.name) {
            ImageScreen(navController = navController)
        }

    }
}