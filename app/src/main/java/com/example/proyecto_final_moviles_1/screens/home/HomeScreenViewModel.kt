package com.example.proyecto_final_moviles_1.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_moviles_1.data.DataOrException
import com.example.proyecto_final_moviles_1.model.MManga
import com.example.proyecto_final_moviles_1.respository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository) :
    ViewModel() {

    val data: MutableState<DataOrException<List<MManga>, Boolean, Exception>> =
        mutableStateOf(DataOrException(listOf(), true, Exception("")))

    init {
        getAllMangasFromDatabase()
    }

    public fun getAllMangasFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllMangasFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
        Log.d("debos revisar esto", "getAllMangasFromDatabase: ${data.value.data?.toList().toString()}")
    }

}