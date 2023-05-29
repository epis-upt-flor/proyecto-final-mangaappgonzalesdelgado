package com.example.proyecto_final_moviles_1.screens.details

import android.annotation.SuppressLint
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
import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.navigation.MangaScreens

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

                val mangaInfo = produceState<Resource<Data>>(initialValue = Resource.Loading()) {
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
fun ShowMangaDetails(mangaInfo: Resource<Data>, navController: NavController) {
    val mangaData = mangaInfo.data?.attributes
    val GmangaId = mangaInfo.data?.id
    val imageUrl =
        "https://mangadex.org/covers/b7d069cb-4ab9-4c21-a20b-38f7c269be4e/fe93f5cc-32a3-4bf1-8dba-b7aed939a119.jpg"

    Card(
        modifier = Modifier.padding(34.dp),
        shape = CircleShape, elevation = 4.dp
    ) {
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = "Manga Image",
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(1.dp)
        )


    }
    Text(
        text = GmangaId.toString(),
        style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis,
        maxLines = 19
    )

    Text(text = "Estado : ${mangaData?.state.toString()}")

    Spacer(modifier = Modifier.height(5.dp))

//    val cleanDescription = HtmlCompat.fromHtml(
//        mangaData!!.description.toString(),
//        HtmlCompat.FROM_HTML_MODE_LEGACY
//    ).toString()

//    val localDims = LocalContext.current.resources.displayMetrics
//    Surface(
//        modifier = Modifier
//            .height(localDims.heightPixels.dp.times(0.09f))
//            .padding(4.dp), shape = RectangleShape, border = BorderStroke(1.dp, Color.DarkGray)
//    )
//    {
//        LazyColumn(modifier = Modifier.padding(3.dp)) {
//            item {
//                Text(text = mangaData!!.description.toString())
//            }
//        }
//
//    }

}
