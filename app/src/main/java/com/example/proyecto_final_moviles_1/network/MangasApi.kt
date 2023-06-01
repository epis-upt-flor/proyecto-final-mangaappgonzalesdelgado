package com.example.proyecto_final_moviles_1.network



import com.example.proyecto_final_moviles_1.model.CoverId

import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.model.Manga
import com.example.proyecto_final_moviles_1.model.MangaId
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface MangasApi {

    //listar mangas
    @GET("manga?limit=15")
    suspend fun getAllMangas(@Query("title") query: String? = null): Manga

    //listar mangas por id

    @GET("manga/{id}")
    suspend fun getMangaId(@Path("id") id : String? = null): MangaId

    //mostrar imagenes de portada
    @GET("cover/{mangaOrCoverId}")
    suspend fun getAllCover(@Path("mangaOrCoverId") mangaOrCoverId: String): CoverId







}

