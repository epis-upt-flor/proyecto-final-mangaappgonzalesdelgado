package com.example.proyecto_final_moviles_1.screens



import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*


import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyecto_final_moviles_1.components.MangaLogo
import com.example.proyecto_final_moviles_1.navigation.MangaScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun MangaSplashScreen(navController: NavController){

    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    LaunchedEffect(key1 = true){
        scale.animateTo(targetValue = 0.9f,
                animationSpec = tween(durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f).getInterpolation(it)
                    }
                )
        )
        delay(2000L)
        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){ // si ya existe instancia de firebase se salta el login
            navController.navigate(MangaScreens.LoginScreen.name)
        }else {
            navController.navigate(MangaScreens.MangaHomeScreen.name)
        }

        //navController.navigate(MangaScreens.LoginScreen.name) //cuando necesito ir a login activo esto

    }

    Surface(modifier = Modifier
        .padding(15.dp)
        .size(330.dp)
        .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp,color = Color.LightGray),

        ){
            Column(

                modifier = Modifier.padding(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center


            ) {
                MangaLogo()
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "\"Universidad Privada de Tacna\"",
                    style = MaterialTheme.typography.h6,
                    color = Color.LightGray
                )
            }
    }

}

