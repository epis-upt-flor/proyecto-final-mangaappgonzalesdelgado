package com.example.proyecto_final_moviles_1.screens.home

import android.annotation.SuppressLint
import android.media.Image
import android.security.identity.AccessControlProfile
import android.text.TextUtils.split
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.proyecto_final_moviles_1.components.MangaAppBar
import com.example.proyecto_final_moviles_1.components.TitleSection
import com.example.proyecto_final_moviles_1.model.MManga
import com.example.proyecto_final_moviles_1.navigation.MangaScreens
import com.google.firebase.auth.FirebaseAuth

@Preview
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController = NavController(LocalContext.current)){

    Scaffold(topBar = {
        MangaAppBar(title = "NerdCrew", navController = navController )
    },
        floatingActionButton = {
            FABContent{

            }
        }) {

        Surface(modifier = Modifier.fillMaxSize()){
            HomeContent(navController)
        }
    }
}
@Composable
fun HomeContent(navController: NavController){

    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if(!email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else "N/A"


    Column(Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top
        ){
        Row(modifier = Modifier.align(alignment = Alignment.Start)){
            TitleSection(label = "Estas leyendo \n"+" activity ahora mismo...")

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
                Text(text = currentUserName!!, // non null aserted call
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()

            }
        }

        ListCard()


    }

}


@Preview
@Composable
fun RoundedButton(
    label: String = "Leyendo",
    radius: Int = 29,
    onPress: () -> Unit = {}
){
    Surface(modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius)),
            color = Color(0xFF92CBDF)
        ) {

        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = TextStyle(color = Color.White, fontSize = 15.sp))
        }

    }


}




@Preview
@Composable
fun ListCard(manga: MManga = MManga("asd","hola","Daniel y Jesus", "hola mundo"),
                onPressDetails: (String) -> Unit = {} // porq queremos pasar un evento clickeable y id y pues toda al info del manga
             ){

    val context = LocalContext.current
    val resources = context.resources


    val displayMetrics = resources.displayMetrics // sacamos la info de lo que esta mostrando la app, en este caso los tamaños de los diferentes objetos

    val screenWidth = displayMetrics.widthPixels/ displayMetrics.density // va a sacar el tamaño de la pantalla del celu
    val spacing = 10.dp

    Card(shape = RoundedCornerShape(29.dp),
            /*backgroundColor = Color.White,*/
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onPressDetails.invoke(manga.title.toString()) }
        ) {
        Column(modifier = Modifier.width(screenWidth.dp - (spacing * 2) ),
            horizontalAlignment = Alignment.Start ){
            
            Row(horizontalArrangement = Arrangement.Center) {

                Image(painter = rememberImagePainter(data = "https://mangadex.org/covers/a77742b1-befd-49a4-bff5-1ad4e6b0ef7b/f6cd8dcd-ad53-47bc-9699-fb758c51d9ba.jpg"), // obviamente la imagen pero falta trabajar aqui topdavia
                    contentDescription = "Imagen de libro",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                    )
                
                Spacer(modifier = Modifier.width(50.dp))

                Column(modifier = Modifier.padding(top = 25.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) { // los botonsitos en la imagen
                    
                    Icon(imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon/ corazoncito",
                        modifier = Modifier.padding(bottom = 1.dp)
                        )

                    MangaRating(score = 4.0)

                    
                }
            }

            Text(text = "Titulo", modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)

            Text(text = "Autores: todos...", modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.caption)



        }

        // este el el botoncito celeste fuera del column
        Row(horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ){
            RoundedButton(label = "Leyendo", radius = 70)

        }


    }
}

@Composable
fun MangaRating(score: Double = 4.5) {
    Surface(modifier = Modifier
        .height(70.dp)
        .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        elevation = 6.dp,
        /*color = Color.White*/
    ) {
        Column(modifier = Modifier.padding(4.dp)) { // estrella y rating
            Icon(imageVector = Icons.Filled.StarBorder, contentDescription = "Star",
            modifier = Modifier.padding(3.dp))

            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)
        }

    }

}


@Composable
fun ReadingRightNowArea(mangas: List<MManga>,
                        navController: NavController){

}


