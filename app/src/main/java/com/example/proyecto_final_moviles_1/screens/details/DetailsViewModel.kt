package com.example.proyecto_final_moviles_1.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_moviles_1.data.Resource
import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.model.MangaId
import com.example.proyecto_final_moviles_1.modelChapter.ChapterVolume
import com.example.proyecto_final_moviles_1.respository.MangaRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: MangaRespository)
    : ViewModel()
{
    suspend fun getMangaInfo(id: String): Resource<MangaId> {
        try {
            return repository.getMangaInfo(id)
        } catch (e: Exception) {
            // Manejo de la excepción aquí
            return Resource.Error("Ocurrió un error al obtener la información del manga.")
        }
    }


}