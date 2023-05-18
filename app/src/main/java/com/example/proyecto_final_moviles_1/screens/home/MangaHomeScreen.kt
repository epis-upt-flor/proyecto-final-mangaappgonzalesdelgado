package com.example.proyecto_final_moviles_1.screens.home

import android.annotation.SuppressLint
import android.media.Image
import android.security.identity.AccessControlProfile
import android.text.TextUtils.split
import android.util.Log
import android.widget.HorizontalScrollView
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.proyecto_final_moviles_1.R
import com.example.proyecto_final_moviles_1.components.FABContent
import com.example.proyecto_final_moviles_1.components.ListCard
import com.example.proyecto_final_moviles_1.components.MangaAppBar
import com.example.proyecto_final_moviles_1.components.MangaRating
import com.example.proyecto_final_moviles_1.components.TitleSection
import com.example.proyecto_final_moviles_1.model.MManga
import com.example.proyecto_final_moviles_1.navigation.MangaScreens
import com.google.firebase.auth.FirebaseAuth

@Preview
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController = NavController(LocalContext.current)) {

    Scaffold(topBar = {
        MangaAppBar(title = "NerdCrew", navController = navController)
    },
        floatingActionButton = {
            FABContent {

            }
        }) {

        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavController) {

    val listOfMangas = listOf(
        MManga(id ="1", title = "PARA DESPUES", authors = "Nosotros", notes = "nada"),
        MManga(id ="1", title = "PARA DESPUES", authors = "Nosotros", notes = "nada"),
        MManga(id ="1", title = " DESPUES", authors = "Todos", notes = "nada"),
        MManga(id ="1", title = "PARA DESPUES", authors = "Nosotros", notes = "nada"),
        MManga(id ="1", title = "PARA DESPUES", authors = "Nosotros", notes = "nada")
    )

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
                    text = currentUserName!!, // non null aserted call
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()

            }
        }

        ReadingRightNowArea(mangas = listOf(), navController = navController)

        TitleSection(label = "Lista de lectura")

        BoolListArea(listOfMangas = listOfMangas, navController = navController)

    }

}




@Composable
fun BoolListArea(listOfMangas: List<MManga>, navController: NavController){
    HorizontalScrollableComponent(listOfMangas){
        Log.d("TAG", "BoolListArea :$it")
        //Todo: en click a la carta va a navegar a detalles
    }
}

@Composable
fun HorizontalScrollableComponent(listOfMangas: List<MManga>, onCardPressed: (String) -> Unit) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(280.dp)
        .horizontalScroll(scrollState)) {

        for(manga in listOfMangas) {
            ListCard(manga){
                onCardPressed(it)
            }
        }
    }
}


@Composable
fun ReadingRightNowArea(
    mangas: List<MManga>,
    navController: NavController
) {
    ListCard()
}


