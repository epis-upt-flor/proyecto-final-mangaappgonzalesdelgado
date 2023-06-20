package com.example.proyecto_final_moviles_1.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.proyecto_final_moviles_1.components.MangaAppBar
import com.example.proyecto_final_moviles_1.components.RoundedButton
import com.example.proyecto_final_moviles_1.data.Resource
import com.example.proyecto_final_moviles_1.di.AppModule
import com.example.proyecto_final_moviles_1.model.MManga
import com.example.proyecto_final_moviles_1.model.MangaId
import com.example.proyecto_final_moviles_1.model.Relationship
import com.example.proyecto_final_moviles_1.modelChapter.ChapterVolume
import com.example.proyecto_final_moviles_1.navigation.MangaScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
                    ShowMangaDetails(mangaInfo, navController)
                }

            }


        }
    }

}

@Composable
fun ShowMangaDetails(mangaInfo: Resource<MangaId>, navController: NavController,viewModel: DetailsViewModel = hiltViewModel()) {
    val mangaData = mangaInfo.data?.data?.attributes
    val GmangaId = mangaInfo.data?.data?.id
    val genero = mangaData?.tags?.map { tag ->
        tag.attributes.name.en
    }

    val coverlist: List<Relationship> = mangaInfo.data?.data!!.relationships
    var coverId = String()


    val coverArtIds = coverlist
        .filter { it.type == "cover_art" } // Filtrar solo los elementos con tipo "cover_art"
        .map { it.id } // Obtener el atributo id de cada elemento

    for (coverArtId in coverArtIds) {
        coverId = coverArtId
    }

    //------------------------------------------------------------------
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

//---------------------------------------------------------------------------------------


    Box(
        modifier = Modifier
            .padding(15.dp)
            .size(width = 300.dp, height = 220.dp) // Puedes ajustar el tamaño aquí
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.surface)
                .padding(6.dp),
            elevation = 8.dp
        ) {
            Image(
                painter = rememberImagePainter(data = imageUrl.value),
                contentDescription = "Manga Image",
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f),
                contentScale = ContentScale.FillBounds
            )
        }
    }




    Text(
        text = mangaData?.title?.en.toString(),
        style = MaterialTheme.typography.h6.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold
        ),
        overflow = TextOverflow.Ellipsis,
        maxLines = 19
    )


    Text(
        text = "Estado : ${mangaData?.state.toString()}",
        style = MaterialTheme.typography.subtitle1
    )

    Text(
        text = buildAnnotatedString {
            pushStyle(MaterialTheme.typography.subtitle1.toSpanStyle())
            append("Genero: $genero")
            pop()
        },
        textAlign = TextAlign.Center,
        maxLines = 3
    )


    Spacer(modifier = Modifier.height(5.dp))


    val cleanDescription = HtmlCompat.fromHtml(
        mangaData!!.description.en,
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()

    val localDims = LocalContext.current.resources.displayMetrics

    var showFullDescription by remember { mutableStateOf(false) }
    val expandedHeight = animateDpAsState(
        if (showFullDescription && cleanDescription.length > 200) localDims.heightPixels.dp.times(
            0.08f
        ) else localDims.heightPixels.dp.times(0.06f)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(expandedHeight.value)
            .height(IntrinsicSize.Min)
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Transparent)
            ) {
                ClickableText(
                    text = if (showFullDescription && cleanDescription.length > 200) AnnotatedString(
                        cleanDescription
                    ) else AnnotatedString(cleanDescription.take(200)),
                    style = TextStyle(fontWeight = FontWeight.Normal),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (cleanDescription.length > 200) {
                            showFullDescription = !showFullDescription
                        }
                    }
                )
            }
        }
    }



    //Buttons

    Row(
        modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        RoundedButton(label = "Guardar") {
            //guardar tus mangas en tu firestore database

            val manga = MManga(
                title = mangaData.title.en,
                gender = genero,
                description = mangaData.description.en,
                notes = "",
                rating = 0.0,
                urlImage = imageUrl.value,
                MangaId = GmangaId,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            )

            saveFirestore(manga, navController = navController)


        }
        Spacer(modifier = Modifier.width(45.dp))
        RoundedButton(label = "Cancelar") {
            navController.popBackStack()
        }

    }

    Spacer(modifier = Modifier.height(45.dp))

    //boton de lista de capitulos

    Row {
        RoundedButton(label = "Capitulos"){
            navController.navigate(MangaScreens.ChaptersScreen.name + "/${GmangaId}")
        }
    }

}


fun saveFirestore(manga: MManga, navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    val dbCollection = db.collection("mangas")

    if (manga.toString().isNotEmpty()) {
        dbCollection.add(manga)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.popBackStack()
                        }


                    }.addOnFailureListener {
                        Log.e("Error al guardar", "Error al guardar en Firestore", it)
                    }

            }


    } else {


    }


}


