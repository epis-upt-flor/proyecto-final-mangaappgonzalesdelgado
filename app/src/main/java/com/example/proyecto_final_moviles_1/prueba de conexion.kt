package com.example.proyecto_final_moviles_1

import com.example.proyecto_final_moviles_1.network.MangasApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.proyecto_final_moviles_1.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

//suspend fun main() {
//    val apiService = provideMangaApi()
//    val mangaRepository = MangaRepository(apiService)
//    val isConnected = mangaRepository.checkApiConnection()
//    if (isConnected) {
//        println("Conexión exitosa con la API")
//    } else {
//        println("Error de conexión con la API")
//    }
//}
//
//fun provideMangaApi(): MangasApi {
//    return Retrofit.Builder()
//        .baseUrl(Constants.BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(MangasApi::class.java)
//}
//
//class MangaRepository(private val apiService: MangasApi) {
//    suspend fun checkApiConnection(): Boolean {
//        return try {
//            apiService.getAllCover("98162a5d-1901-4343-8a81-4531e754e688")
//            true // Si la solicitud se completa sin errores, la conexión es exitosa
//        } catch (e: Exception) {
//            false // Error de conexión con la API
//        }
//    }
//}



fun main() {
    runBlocking {
        // Crea una instancia de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.mangadex.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia de la interfaz Retrofit
        val api = retrofit.create(MangasApi::class.java)

        // Lanza la solicitud GET de forma asíncrona utilizando corrutinas
        val resultado = async(Dispatchers.IO) {
            api.getAllCover("93894481-99a5-49c1-b93d-913fcc51b767")
        }

        // Espera a que la solicitud se complete y obtiene el resultado
        val respuesta = resultado.await()

        // Maneja la respuesta
        println("Datos recibidos: ${respuesta.data.attributes?.fileName.toString()}")
    }
}