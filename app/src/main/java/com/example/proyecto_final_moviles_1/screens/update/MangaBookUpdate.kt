package com.example.proyecto_final_moviles_1.screens.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.proyecto_final_moviles_1.components.MangaAppBar
import com.example.proyecto_final_moviles_1.data.DataOrException
import com.example.proyecto_final_moviles_1.model.MManga
import com.example.proyecto_final_moviles_1.screens.home.HomeScreenViewModel
import java.lang.Exception

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookUpdateScreen(navController: NavController,
                     MangaItemId: String,
                     viewModel:HomeScreenViewModel = hiltViewModel()){

    Scaffold(topBar = {
        MangaAppBar(title = "Update Book",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController){
            navController.popBackStack()
        }
    }) {

        val mangaInfo = produceState<DataOrException<List<MManga>,
                Boolean,
                Exception>>(initialValue = DataOrException(data = emptyList(),
        true, Exception(""))){
             value = viewModel.data.value
        }.value

        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(3.dp)) {
            Column(modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = CenterHorizontally
                ) {
                Log.d("INFO", "MangaUpdateScreen: ${viewModel.data.value.data.toString()}")

                if (mangaInfo.loading == true){
                    LinearProgressIndicator()
                    mangaInfo.loading = false
                }else{
                    Text(text = viewModel.data.value.data?.get(0)?.title.toString())
                }
            }
        }

    }

}