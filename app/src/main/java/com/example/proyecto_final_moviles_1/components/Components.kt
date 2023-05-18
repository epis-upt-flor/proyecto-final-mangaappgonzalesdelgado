package com.example.proyecto_final_moviles_1.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.proyecto_final_moviles_1.model.MManga
import com.example.proyecto_final_moviles_1.navigation.MangaScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MangaLogo(modifier: Modifier = Modifier) {
    Text(
        text = "NerdCrew",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.h3,
        color = Color.Red.copy(alpha = 0.5f)
    )
}



@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next, // relacionado al boton
    onAction: KeyboardActions = KeyboardActions.Default
){
    InputField(modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}


@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it }, // aqui se añade lo que este en el textField
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction


    )
}



@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction=ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {

    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value, onValueChange ={
        passwordState.value = it
    },
        label = { Text(text = labelId )},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {PasswodVisibility(passwordVisibility = passwordVisibility)},
        keyboardActions = onAction
    )
}


@Composable
fun PasswodVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icons.Default.Close
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

            Text(text = manga.title.toString(), modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)

            Text(text = manga.authors.toString(), modifier = Modifier.padding(4.dp),
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
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = {onTap()  },
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xff92CBDF)) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription = "Añadir un Manga",
            tint = MaterialTheme.colors.onSecondary)

    }
}