package com.example.proyecto_final_moviles_1.screens.chapters

import androidx.lifecycle.ViewModel
import com.example.proyecto_final_moviles_1.data.Resource
import com.example.proyecto_final_moviles_1.modelChapter.ChapterVolume
import com.example.proyecto_final_moviles_1.respository.MangaRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChapterViewModel @Inject constructor(private val repository: MangaRespository) : ViewModel() {
    suspend fun getTitles(id: String): Resource<ChapterVolume> {
        return repository.getTitles(id)
    }

}