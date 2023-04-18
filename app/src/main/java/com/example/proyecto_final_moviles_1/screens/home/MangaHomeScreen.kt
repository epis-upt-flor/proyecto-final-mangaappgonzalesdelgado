package com.example.proyecto_final_moviles_1.screens.home

import android.annotation.SuppressLint
import android.media.Image
import android.security.identity.AccessControlProfile
import android.text.TextUtils.split
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
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
import com.example.proyecto_final_moviles_1.R
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
    }

}

@Composable
fun MangaAppBar(
    title: String,
    showProfile: Boolean = true,
    navController: NavController
    ){
    TopAppBar(title = {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                          if(showProfile){
                              Icon(imageVector = Icons.Default.Favorite,
                                  contentDescription = "Icono del logo",
                                  modifier = Modifier
                                      .clip(RoundedCornerShape(12.dp))
                                      .scale(0.9f)
                              )

                          }
                          Text(text = title,
                              color = Color.Red.copy(alpha =0.7f),
                              style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                          )
                          Spacer(modifier = Modifier.width(150.dp))
                          
                      }


    },
        actions = {
                  IconButton(onClick = {
                      FirebaseAuth.getInstance().signOut().run{ // cerrar sesion!!!
                          navController.navigate(MangaScreens.LoginScreen.name)
                      }
                  }) {
                        Icon(imageVector = Icons.Filled.Logout, // icono de cerrar sesion
                            contentDescription = "Cerrar Sesión"
                        //tint = Color.Green.copy(alpha = 0.4f)
                        )


                  }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp)

}



@Composable
fun TitleSection(modifier: Modifier = Modifier, 
                 label: String){
    
    Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {

        Column {
            Text(text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left
            )

        }
        
    }

}



@Composable
fun ReadingRightNowArea(mangas: List<MManga>,
                        navController: NavController){

}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = {onTap()  },
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xff92CBDF)) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription = "Añadir un Manga",
            tint = MaterialTheme.colors.onSecondary)

    }
}
