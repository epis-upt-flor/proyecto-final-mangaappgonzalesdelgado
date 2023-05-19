package com.example.proyecto_final_moviles_1.network


import com.example.proyecto_final_moviles_1.model.Manga
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface MangasApi {

    //listar mangas
    @GET("/manga")
    suspend fun getAllMangas(): Response<Manga>

    //listar mangas por id
    @GET("/manga/{id}")
    suspend fun getMangaId(@Path("id") id : String):Response<Manga>

}