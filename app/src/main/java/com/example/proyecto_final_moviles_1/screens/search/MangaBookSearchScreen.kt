package com.example.proyecto_final_moviles_1.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.proyecto_final_moviles_1.di.AppModule
import com.example.proyecto_final_moviles_1.model.CoverId
import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.model.Relationship
import com.example.proyecto_final_moviles_1.model.superData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: MangaSearchViewModel = hiltViewModel()

) {
    Scaffold(topBar = {
        MangaAppBar(
            title = "Buscar Manga",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false
        ) {

            navController.navigate(MangaScreens.MangaHomeScreen.name)
        }
    }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
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
    if (viewModel.isloading) {
        Row(
            modifier = Modifier.padding(end = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Cargando...")
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = Color.Blue,
                        strokeWidth = 4.dp
                    )
                }
            }

        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {

            items(items = listOfMangas) { manga ->
                MangaRow(manga, navController)
            }


        }
    }

}


@Composable
fun MangaRow(
    manga: Data,

    navController: NavController

) {

    val mangaId = manga.id
    val tipo = manga.type
    val estado = manga.attributes.state
    val genero = manga.attributes.tags.map { tag ->
        tag.attributes.name.en
    }


    val coverlist: List<Relationship> = manga.relationships
    var coverId = String()

    val coverArtIds = coverlist
        .filter { it.type == "cover_art" } // Filtrar solo los elementos con tipo "cover_art"
        .map { it.id } // Obtener el atributo id de cada elemento

    for (coverArtId in coverArtIds) {
        coverId = coverArtId
    }

    /////////////////////////////////////////////////////////////////////////////
    suspend fun obtenerImageFile(coverId: String): String {
        val retrofit = AppModule.provideMangaApi()
        val conect = retrofit.getAllCover(coverId)
        return conect.data.attributes.fileName
    }

    // Llama a la función suspendida desde un contexto de coroutine
    GlobalScope.launch(Dispatchers.Main) {
        val miVariableExterior: String = obtenerImageFile(coverId)
        // Aquí puedes utilizar miVariableExterior con el valor obtenido de la función suspendida

    }


    val miVariableExterior = remember { mutableStateOf("") }

    // Llamada a la función suspendida dentro de un efecto de composición LaunchedEffect
    LaunchedEffect(key1 = coverId) {
        miVariableExterior.value = obtenerImageFile(coverId)

    }

    Log.d("IMAGEFIELMANGA", "El valor de myVariable es: $miVariableExterior")

///////////////////////////////////////////////////////////////////

    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(MangaScreens.DetailsScreen.name + "/${manga.id}")
            }
            .fillMaxWidth()
            .height(135.dp)
            .padding(3.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFDDEEFF), Color(0xFFABCDEF))
                    )
                )
        ) {
            Row(
                modifier = Modifier.padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ...
                val imageUrl = "https://mangadex.org/covers/$mangaId/${miVariableExterior.value}"
                Image(
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = "Manga Image",
                    modifier = Modifier
                        .width(80.dp)
                        .fillMaxHeight()
                        .padding(end = 4.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                )

                Column(Modifier.padding(start = 8.dp)) {
                    // ...

                    Text(
                        text = "Tipo: $tipo",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                    Text(
                        text = "Estado: $estado",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                    Text(
                        text = "Género: $genero",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


// campo de búsqueda que captura la consulta de búsqueda
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Buscar",
    onSearch: (String) -> Unit = {}
) {
    Column(modifier) {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val KeyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQueryState.value,
            onValueChange = { searchQueryState.value = it },
            label = { Text("Buscar") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (valid) {
                    onSearch(searchQueryState.value.trim())
                    searchQueryState.value = ""
                    KeyboardController?.hide()
                }
            }),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,

                cursorColor = Color.Gray,
                textColor = Color.Gray
            )
        )
    }
}
