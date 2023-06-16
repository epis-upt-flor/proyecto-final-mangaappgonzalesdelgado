package com.example.proyecto_final_moviles_1.screens.chapters

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.proyecto_final_moviles_1.components.MangaAppBar
import com.example.proyecto_final_moviles_1.components.cardChapter
import com.example.proyecto_final_moviles_1.data.Resource
import com.example.proyecto_final_moviles_1.modelChapter.ChapterVolume

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MangaChapterScreen(navController: NavController, id: String, viewModel: ChapterViewModel = hiltViewModel()) {


    val chapters = produceState<Resource<ChapterVolume>>(initialValue = Resource.Loading()){
        value = viewModel.getTitles(id)
    }.value

    val numberTitle = chapters.data?.data?.map { it.attributes.title }

//    Log.d("numberTitle", "Number of chapters: ${numberTitle}")

    val numberChapter = chapters.data?.data?.map { it.attributes.chapter }

//    Log.d("numberChapter", "Number of chapters: ${numberChapter}")

    val idchapter = chapters.data?.data?.map { chapter -> chapter.id }








    Scaffold(topBar = {
        MangaAppBar(
            title = "Lista de capitulos",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false
        ) {

            navController.popBackStack()
        }
    }) {
        LazyColumn() {
            items(numberTitle?.size ?: 0) { index ->

                cardChapter(navController,numberChapter?.get(index), numberTitle?.get(index),idchapter?.get(index))

            }
        }
    }
}


