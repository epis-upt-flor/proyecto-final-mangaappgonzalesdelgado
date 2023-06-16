package com.example.proyecto_final_moviles_1.screens.update

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.proyecto_final_moviles_1.R
import com.example.proyecto_final_moviles_1.components.InputField
import com.example.proyecto_final_moviles_1.components.MangaAppBar
import com.example.proyecto_final_moviles_1.components.RatingBar
import com.example.proyecto_final_moviles_1.components.RoundedButton
import com.example.proyecto_final_moviles_1.components.showToast
import com.example.proyecto_final_moviles_1.data.DataOrException
import com.example.proyecto_final_moviles_1.model.MManga
import com.example.proyecto_final_moviles_1.navigation.MangaScreens
import com.example.proyecto_final_moviles_1.screens.home.HomeScreenViewModel
import com.example.proyecto_final_moviles_1.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookUpdateScreen(
    navController: NavController,
    MangaItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    Scaffold(topBar = {
        MangaAppBar(
            title = "Update Book",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) {

        val mangaInfo = produceState<DataOrException<List<MManga>,
                Boolean,
                Exception>>(
            initialValue = DataOrException(
                data = emptyList(),
                true, Exception("")
            )
        ) {
            value = viewModel.data.value
        }.value

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = CenterHorizontally
            ) {
                Log.d("INFO", "MangaUpdateScreen: ${viewModel.data.value.data.toString()}")

                if (mangaInfo.loading == true) {
                    LinearProgressIndicator()
                    mangaInfo.loading = false
                } else {
                    Surface(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        shape = CircleShape,
                        elevation = 4.dp
                    ) {
                        ShowBookUpdate(mangaInfo = viewModel.data.value, MangaItemId = MangaItemId)
                    }

                    ShowSimpleForm(manga = viewModel.data.value.data?.first { mManga ->
                        mManga.MangaId == MangaItemId
                    }!!, navController)

                }
            }
        }

    }

}

@Composable
fun ShowSimpleForm(manga: MManga, navController: NavController) {

    val context = LocalContext.current

    val notesText = remember {
        mutableStateOf("")
    }

    val isStartedReading = remember {
        mutableStateOf(false)
    }

    val isFinishedReading = remember {
        mutableStateOf(false)
    }

    val ratingVal = remember {
        mutableStateOf(0)
    }

    SimpleForm(
        defaultValue = if (manga.notes.toString().isNotEmpty()) manga.notes.toString()
        else "No thoghts available.") { note ->
        notesText.value = note
    }

    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        TextButton(
            onClick = { isStartedReading.value = true },
            enabled = manga.startedReading == null
        ) {

            if (manga.startedReading == null) {

                if (!isStartedReading.value) {
                    Text(text = "Empieza a leer")
                } else {
                    Text(
                        text = "Comenzó a leer",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }

            } else {
                Text("Comenzó en :${formatDate(manga.startedReading!!)}")
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        TextButton(
            onClick = { isFinishedReading.value = true },
            enabled = manga.finishedReading == null
        ) {
            if (manga.finishedReading == null) {
                if (!isFinishedReading.value) {
                    Text(text = "Marcar como leído")
                } else {
                    Text(text = "Lectura terminada!")
                }
            } else {
                Text("Terminó en :${formatDate(manga.finishedReading!!)}")
            }
        }

    }
    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
    manga.rating?.toInt().let {
        RatingBar(rating = it!!) { rating ->
            ratingVal.value = rating
        }
    }

    Spacer(modifier = Modifier.padding(bottom = 15.dp))
    Row {
        val changedNotes = manga.notes != notesText.value
        val changedRating = manga.rating?.toInt() != ratingVal.value
        val isFinishedTimeStamp =
            if (isFinishedReading.value) Timestamp.now() else manga.finishedReading
        val isStartedTimeStamp =
            if (isStartedReading.value) Timestamp.now() else manga.startedReading

        val mangaUpdate =
            changedNotes || changedRating || isStartedReading.value || isFinishedReading.value

        val mangaToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value
        ).toMap()

        RoundedButton(label = "Actualizar") {
            if (mangaUpdate) {
                FirebaseFirestore.getInstance()
                    .collection("mangas")
                    .document(manga.id!!)
                    .update(mangaToUpdate)
                    .addOnCompleteListener { task ->

                        showToast(context, "Actualizacion de manga exitosa!")
                        navController.navigate(MangaScreens.MangaHomeScreen.name)

                    }.addOnFailureListener {
                        Log.w("Error", "Error updating manga", it)
                    }
            }

        }

        Spacer(modifier = Modifier.width(100.dp))

        val openDialog = remember {
            mutableStateOf(false)
        }
        if (openDialog.value) {
            ShowAlertDialog(message = stringResource(id = R.string.sure) + "\n" +
                    stringResource(id = R.string.action), openDialog){
                    FirebaseFirestore.getInstance()
                        .collection("mangas")
                        .document(manga.id!!)
                        .delete()
                        .addOnCompleteListener{
                            if (it.isSuccessful) {
                                openDialog.value = false
                                /*
                                 Don't popBackStack() if we want the immediate recomposition
                                 of the MainScreen UI, instead navigate to the mainScreen!
                                */

                                navController.navigate(MangaScreens.MangaHomeScreen.name)
                            }
                        }
            }
        }

        RoundedButton("Eliminar") {

            openDialog.value = true
        }


    }
    Spacer(modifier = Modifier.height(45.dp))

    Row {
        RoundedButton(label = "Capitulos"){
            navController.navigate(MangaScreens.ChaptersScreen.name+ "/${manga.MangaId}")
        }
    }
// "/${manga.id}"
}


@Composable
fun ShowAlertDialog(
    message: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit) {

    if (openDialog.value) {
        AlertDialog(onDismissRequest = { openDialog.value = false},
            title = { Text(text = "Eliminar manga")},
            text = { Text(text = message)},
            buttons = {
                Row(modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center) {
                    TextButton(onClick = { onYesPressed.invoke() }) {
                        Text(text = "Si")

                    }
                    TextButton(onClick = { openDialog.value = false }) {
                        Text(text = "No")

                    }

                }
            })
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "El mejor manga!",
    onSearch: (String) -> Unit
) {
    Column() {
        val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
        val KeyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textFieldValue.value) { textFieldValue.value.trim().isNotEmpty() }

        InputField(
            modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textFieldValue,
            labelId = "Introduce comentario",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(textFieldValue.value.trim())
                KeyboardController?.hide()
            }
        )
    }

}

@Composable
fun ShowBookUpdate(
    mangaInfo: DataOrException<List<MManga>, Boolean, Exception>,
    MangaItemId: String
) {
    Row() {
        Spacer(modifier = Modifier.width(43.dp))
        if (mangaInfo.data != null) {
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                CardListItem(manga = mangaInfo.data!!.first { mManga ->
                    mManga.MangaId == MangaItemId
                }, onPressDetail = {})
            }
        }
    }
}

@Composable
fun CardListItem(manga: MManga, onPressDetail: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(
                start = 4.dp,
                end = 4.dp,
                top = 4.dp,
                bottom = 8.dp
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable { },
        elevation = 8.dp
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            Image(
                painter = rememberImagePainter(data = manga.urlImage.toString()),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            )
            Column() {
                Text(
                    text = manga.title.toString(),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

//                Text(text = manga.description.toString(),
//                    style = MaterialTheme.typography.body2,
//                    modifier = Modifier.padding(start = 8.dp,
//                        end = 8.dp,
//                        top = 2.dp,
//                        bottom = 0.dp))

                Text(
                    text = manga.gender.toString(),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 0.dp,
                        bottom = 8.dp
                    )
                )
            }
        }


    }
}

