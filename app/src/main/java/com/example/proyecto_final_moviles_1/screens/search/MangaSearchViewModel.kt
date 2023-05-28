package com.example.proyecto_final_moviles_1.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.proyecto_final_moviles_1.data.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.model.DataX
import com.example.proyecto_final_moviles_1.respository.MangaRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MangaSearchViewModel @Inject constructor(private val repository: MangaRespository):
    ViewModel(){

     var list: List<Data> by mutableStateOf(listOf())
     //var list1: List<DataX> by mutableStateOf(listOf())

    init {
        loadMangas()
    }

    private fun loadMangas(){
        searchManga("youjo senki")
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
                        //list1=response.data!!
                    }
                     is Resource.Error -> {
                         Log.e("Network", "searchManga: Failed getting Mangas", )
                     }
                     else->{ }
                 }
            }catch (exception: Exception) {
                Log.d("Network", "searchManga: ${exception.message.toString()}")
            }
        }
    }
}

