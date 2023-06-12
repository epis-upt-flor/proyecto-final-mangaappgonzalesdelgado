package com.example.proyecto_final_moviles_1.navigation

enum class MangaScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    MangaHomeScreen,
    SearchScreen,
    DetailsScreen,
    UpdateScreen,
    MangaStatsScreen,
    ChaptersScreen;
    companion object {
        fun fromRoute(route: String):MangaScreens
        = when(route?.substringBefore("/")){
            SplashScreen.name ->SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            SearchScreen.name -> SearchScreen
            DetailsScreen.name -> DetailsScreen
            UpdateScreen.name -> UpdateScreen
            MangaStatsScreen.name -> MangaStatsScreen
            ChaptersScreen.name -> ChaptersScreen //ruta de imagnes de capitulos
            null -> MangaHomeScreen
            else -> throw IllegalArgumentException("Ruta $route desconocida")
        }
    }

}