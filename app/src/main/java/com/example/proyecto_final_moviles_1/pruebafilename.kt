package com.example.proyecto_final_moviles_1

import com.example.proyecto_final_moviles_1.di.AppModule
import com.example.proyecto_final_moviles_1.network.MangasApi
import com.example.proyecto_final_moviles_1.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

suspend fun main() {
    val retrofit = AppModule.provideMangaApi()


    val mangaOrCoverId = String() // Reemplaza con el ID real del manga o cover que deseas obtener

    try {
        val data = retrofit.getAllCover(mangaOrCoverId)
        val fileName = data.data.attributes?.fileName?:"no hay nada"
        println("FileName: $fileName")
    } catch (e: Exception) {
        println("Error al consumir el API: ${e.message}")
    }
}


