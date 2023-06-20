package com.example.proyecto_final_moviles_1.screens.chapters

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.proyecto_final_moviles_1.data.Resource
import com.example.proyecto_final_moviles_1.modelImage.ImageFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

                    val imageRequest = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .build()

                    val isHorizontal = rememberImageOrientation(imageUrl)
                    val contentScale = if (isHorizontal) ContentScale.FillWidth else ContentScale.FillHeight
                    val modifier = if (isHorizontal) Modifier.fillMaxWidth() else Modifier.fillMaxSize().aspectRatio(0.6f)

                    Box(modifier = modifier) {
                        Image(
                            painter = rememberImagePainter(request = imageRequest),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = contentScale
                        )
                    }
                }
            }
        }
    )
}




@Composable

private fun rememberImageOrientation(imageUrl: String): Boolean {
    var isHorizontal by remember(imageUrl) { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(imageUrl) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val imageLoader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .build()
                val result = imageLoader.execute(request)
                val drawable = result.drawable

                if (drawable is BitmapDrawable) {
                    val bitmap = drawable.bitmap
                    val width = bitmap.width
                    val height = bitmap.height
                    isHorizontal = width > height
                }
            }
        }
    }

    return isHorizontal
}














