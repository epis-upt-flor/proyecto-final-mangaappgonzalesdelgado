package com.example.proyecto_final_moviles_1.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.proyecto_final_moviles_1.data.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.respository.MangaRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MangaSearchViewModel @Inject constructor(private val repository: MangaRespository):
    ViewModel(){

     var list: List<Data> by mutableStateOf(listOf())
     var isloading : Boolean by mutableStateOf(true)

    init {
        loadMangas()
    }

     fun loadMangas(){
        searchManga("a")

    }
    fun searchManga(query: String){
        viewModelScope.launch(Dispatchers.Default){

            if (query.isEmpty()){
                return@launch
            }
            try {
                 when(val response = repository.getMangas(query)){
                    is Resource.Success -> {
                        list = response.data!!
                        if (list.isNotEmpty()) isloading = false

                    }
                     is Resource.Error -> {
                         Log.e("Network", "searchManga: Failed getting Mangas", )
                     }
                     else->{ isloading = false}
                 }
            }catch (exception: Exception) {
                isloading = false
                Log.d("Network", "searchManga: ${exception.message.toString()}")
            }
        }
    }


}
