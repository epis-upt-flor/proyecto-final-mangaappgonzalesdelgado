package com.example.proyecto_final_moviles_1.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.proyecto_final_moviles_1.components.FABContent
import com.example.proyecto_final_moviles_1.components.ListCard
import com.example.proyecto_final_moviles_1.components.MangaAppBar
import com.example.proyecto_final_moviles_1.components.TitleSection
import com.example.proyecto_final_moviles_1.model.MManga
import com.example.proyecto_final_moviles_1.navigation.MangaScreens
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    Scaffold(topBar = {
        MangaAppBar(title = "NerdCrew", navController = navController)
    },
        floatingActionButton = {
            FABContent {
                navController.navigate(MangaScreens.SearchScreen.name)
            }
        }) {

        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController, viewModel)
        }
    }
}

@Composable
fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel) {

    var listOfMangas = emptyList<MManga>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfMangas = viewModel.data.value.data!!.toList().filter { mManga ->
            mManga.userId == currentUser?.uid.toString()
        }

        Log.d("Mangas", "HomeContent:${listOfMangas}")
    }

//    val listOfMangas = listOf(
//        MManga(id ="1", title = "PARA DESPUES", description = "Nosotros", notes = "nada"),
//        MManga(id ="1", title = "PARA DESPUES", description = "Nosotros", notes = "nada"),
//        MManga(id ="1", title = " DESPUES", description = "Todos", notes = "nada"),
//        MManga(id ="1", title = "PARA DESPUES", description = "Nosotros", notes = "nada"),
//        MManga(id ="1", title = "PARA DESPUES", description = "Nosotros", notes = "nada")
//    )

    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else "N/A"


    Column(
        Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Estas leyendo \n" + " activity ahora mismo...")

            Spacer(modifier = Modifier.fillMaxWidth(0.7f))   // separacion

            Column {  // icono y nombre
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Perfil",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(MangaScreens.MangaStatsScreen.name)  // icono clickeable
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant
                )
                Text(
                    text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()

            }
        }

        ReadingRightNowArea(listOfMangas = listOfMangas, navController = navController)

        TitleSection(label = "Lista de lectura")

        BookListArea(listOfMangas = listOfMangas, navController = navController)

    }

}


@Composable
fun BookListArea(listOfMangas: List<MManga>, navController: NavController) {

    val addedMangas = listOfMangas.filter { mManga ->
        mManga.startedReading == null && mManga.finishedReading == null
    }


    HorizontalScrollableComponent(addedMangas) {
        navController.navigate(MangaScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfMangas: List<MManga>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCardPressed: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .horizontalScroll(scrollState)
    ) {
        if (viewModel.data.value.loading == true) {
            LinearProgressIndicator()
        } else {
            if (listOfMangas.isNullOrEmpty()) {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(
                        text = "No se encontraron mangas. Agrega un Manga",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            } else {
                for (manga in listOfMangas) {
                    ListCard(manga) {
                        onCardPressed(manga.MangaId.toString())
                    }
                }
            }
        }


    }
}


@Composable
fun ReadingRightNowArea(
    listOfMangas: List<MManga>,
    navController: NavController
) {

    val readingNowList = listOfMangas.filter { mManga ->
        mManga.startedReading != null && mManga.finishedReading == null
    }

    HorizontalScrollableComponent(readingNowList) {
        navController.navigate(MangaScreens.UpdateScreen.name + "/$it")
    }
}


