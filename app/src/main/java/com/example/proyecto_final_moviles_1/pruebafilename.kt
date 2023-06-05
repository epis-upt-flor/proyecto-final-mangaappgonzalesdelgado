package com.example.proyecto_final_moviles_1

import com.example.proyecto_final_moviles_1.di.AppModule
import com.example.proyecto_final_moviles_1.network.MangasApi
import com.example.proyecto_final_moviles_1.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

suspend fun main() {
    val retrofit = AppModule.provideMangaApi()


    val mangaOrCoverId = "d773c8be-8e82-4ff1-a4e9-46171395319b" // Reemplaza con el ID real del manga o cover que deseas obtener

    try {
        val data = retrofit.getAllChapters(mangaOrCoverId)
        val fileNames = data.data.map { it.attributes.title }
        for (fileName in fileNames) {
            println("FileName: $fileName")
        }
    } catch (e: Exception) {
        println("Error al consumir el API: ${e.message}")
    }
}



//todos los titulos de los capitulos del un manga
//try {
//    val data = retrofit.getAllChapters(mangaOrCoverId)
//    val fileNames = data.data.map { it.attributes.title }
//    for (fileName in fileNames) {
//        println("FileName: $fileName")
//    }
//} catch (e: Exception) {
//    println("Error al consumir el API: ${e.message}")
//}



//try {
//    val data = retrofit.getAllChapters(mangaOrCoverId)
//    val fileName = data.data[0].attributes.title
//    println("FileName: $fileName")
//} catch (e: Exception) {
//    println("Error al consumir el API: ${e.message}")
//}