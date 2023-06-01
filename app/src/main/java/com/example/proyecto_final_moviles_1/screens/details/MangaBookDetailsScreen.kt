package com.example.proyecto_final_moviles_1.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.proyecto_final_moviles_1.components.MangaAppBar
import com.example.proyecto_final_moviles_1.data.Resource
import com.example.proyecto_final_moviles_1.di.AppModule
import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.model.MangaId
import com.example.proyecto_final_moviles_1.model.Relationship
import com.example.proyecto_final_moviles_1.navigation.MangaScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MangaDetailsScreen(
    navController: NavController,
    id: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {

    Scaffold(topBar = {
        MangaAppBar(
            title = "Detalles Manga",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.navigate(MangaScreens.SearchScreen.name)
        }
    }) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val mangaInfo = produceState<Resource<MangaId>>(initialValue = Resource.Loading()) {
                    value = viewModel.getMangaInfo(id)
                }.value

                if (mangaInfo.data == null) {
                    Row() {
                        LinearProgressIndicator()
                        Text(text = "Cargando...")
                    }


                } else {
                    ShowMangaDetails(mangaInfo, navController)
                }

            }


        }
    }

}

@Composable
fun ShowMangaDetails(mangaInfo: Resource<MangaId>, navController: NavController) {
    val mangaData = mangaInfo.data?.data?.attributes
    val GmangaId = mangaInfo.data?.data?.id
    val title = mangaInfo.data?.data?.attributes?.title

    val coverlist: List<Relationship> = mangaInfo.data?.data!!.relationships
    var coverId = String()


    val coverArtIds = coverlist
        .filter { it.type == "cover_art" } // Filtrar solo los elementos con tipo "cover_art"
        .map { it.id } // Obtener el atributo id de cada elemento

    for (coverArtId in coverArtIds) {
        coverId = coverArtId
    }


    suspend fun obtenerImageFile(coverId: String): String {
        val retrofit = AppModule.provideMangaApi()
        val conect = retrofit.getAllCover(coverId)
        return conect.data.attributes.fileName
    }

    val miVariableExterior = remember { mutableStateOf("") }

    // Llamada a la función suspendida dentro de un efecto de composición LaunchedEffect
    LaunchedEffect(key1 = coverId) {
        miVariableExterior.value = obtenerImageFile(coverId)
        Log.d("imagen", "El valor de myVariable es: $miVariableExterior")
    }


    val imageUrl = remember { mutableStateOf("") }

    LaunchedEffect(key1 = coverId) {
        val result = obtenerImageFile(coverId)
        miVariableExterior.value = result
        imageUrl.value = "https://mangadex.org/covers/${GmangaId}/${miVariableExterior.value}"


    }


    Card(
        modifier = Modifier.padding(34.dp),
        shape = CircleShape, elevation = 4.dp
    ) {
        Image(
            painter = rememberImagePainter(data = imageUrl.value),
            contentDescription = "Manga Image",
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(1.dp)
        )


    }
    Text(
        text = title.toString(),
        style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis,
        maxLines = 19
    )

    Text(text = "Estado : ${mangaData?.state.toString()}")

    Spacer(modifier = Modifier.height(5.dp))

    val cleanDescription = HtmlCompat.fromHtml(
        mangaData!!.description.toString(),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()

    val localDims = LocalContext.current.resources.displayMetrics
    Surface(
        modifier = Modifier
            .height(localDims.heightPixels.dp.times(0.09f))
            .padding(4.dp), shape = RectangleShape, border = BorderStroke(1.dp, Color.DarkGray)
    )
    {
        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item {
                Text(text = mangaData!!.description.toString())
            }
        }

    }

}


