package com.example.proyecto_final_moviles_1.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto_final_moviles_1.components.InputField
import com.example.proyecto_final_moviles_1.components.MangaAppBar
import com.example.proyecto_final_moviles_1.navigation.MangaScreens
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.proyecto_final_moviles_1.model.AttributesX
import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.model.Relationship
import com.example.proyecto_final_moviles_1.model.cover


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: MangaSearchViewModel = hiltViewModel()
    //androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Scaffold(topBar = {
        MangaAppBar(
            title = "Buscar Manga",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false
        ) {
            //navController.popBackStack()
            navController.navigate(MangaScreens.MangaHomeScreen.name)
        }
    }) {
        Surface() {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { searchQuery ->
                    viewModel.searchManga(query = searchQuery)
                }



                Spacer(modifier = Modifier.height(13.dp))
                MangaList(navController, viewModel)
            }
        }
    }

}

@Composable
fun MangaList(navController: NavController, viewModel: MangaSearchViewModel = hiltViewModel()) {


    val listOfMangas = viewModel.list
    //val listOfrelationship = viewModel1.list


//    val listOfMangas = listOf(
//        MManga(id = "1", title = "PARA DESPUES", authors = "Nosotros", notes = "nada"),
//        MManga(id = "1", title = "PARA DESPUES", authors = "Nosotros", notes = "nada"),
//        MManga(id = "1", title = " DESPUES", authors = "Todos", notes = "nada"),
//        MManga(id = "1", title = "PARA DESPUES", authors = "Nosotros", notes = "nada"),
//        MManga(id = "1", title = "PARA DESPUES", authors = "Nosotros", notes = "nada")
//    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(items = listOfMangas) { manga ->
            MangaRow(manga, navController)
        }
    }

}

@Composable
fun MangaRow(
    manga: Data,




    navController: NavController
) {
    Card(modifier = Modifier
        .clickable { }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {





            val imageUrl=
                "https://mangadex.org/covers/b7d069cb-4ab9-4c21-a20b-38f7c269be4e/fe93f5cc-32a3-4bf1-8dba-b7aed939a119.jpg"
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "Manga Image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )
            Column() {


                Text(text = manga.attributes.title.en, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Tipo: ${manga.type}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )
                //Todo:add more fields later!
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    //ViewModel: MangaSearchViewModel,
    loading: Boolean = false,
    hint: String = "Buscar",
    onSearch: (String) -> Unit = {}
) {
    Column() {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val KeyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(valueState = searchQueryState, labelId = "Buscar", enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                KeyboardController?.hide()
            }
        )

    }

}