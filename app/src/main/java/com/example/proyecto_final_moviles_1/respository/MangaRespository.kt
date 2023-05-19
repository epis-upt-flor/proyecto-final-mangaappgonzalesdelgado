package com.example.proyecto_final_moviles_1.respository

import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.model.Manga
import com.example.proyecto_final_moviles_1.network.MangasApi
import javax.inject.Inject

class MangaRespository @Inject constructor (private val manga: MangasApi) {

    //importante:List<> debe ser un conjuntos de informacion

    suspend fun getMangas(searchQuery: String): List<Data> {

    }

}