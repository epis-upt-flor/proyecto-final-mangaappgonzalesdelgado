package com.example.proyecto_final_moviles_1.screens.chapters

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.proyecto_final_moviles_1.data.Resource
import com.example.proyecto_final_moviles_1.modelChapter.ChapterVolume
import com.example.proyecto_final_moviles_1.modelImage.ImageFile
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun ImageScreen(navController: NavHostController, id: String, viewModel: ChapterViewModel = hiltViewModel()) {
    val image = produceState<Resource<ImageFile>>(initialValue = Resource.Loading()) {
        value = viewModel.getImageFile(id)
    }.value

    Log.d("id", "image es : $id")

    val baseUrl = image.data?.baseUrl + "/data/"
    val hash = image.data?.chapter?.hash + "/"
    val files = image.data?.chapter?.data

    val scrollState = rememberLazyListState()
    val currentIndex = remember { mutableStateOf(0) }

    LaunchedEffect(scrollState.firstVisibleItemIndex) {
        if (currentIndex.value < scrollState.firstVisibleItemIndex) {
            currentIndex.value = scrollState.firstVisibleItemIndex
            scrollState.animateScrollToItem(currentIndex.value)
        }
    }

    Scaffold(
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState
            ) {
                itemsIndexed(files ?: emptyList()) { index, file ->
                    val imageUrl = "$baseUrl$hash$file"

                    Log.d("qhnqlnmsdql", "Url es : $imageUrl")

                    Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                        Image(
                            painter = rememberCoilPainter(request = imageUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    )
}











