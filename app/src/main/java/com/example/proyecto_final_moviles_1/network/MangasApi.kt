package com.example.proyecto_final_moviles_1.network


import com.example.proyecto_final_moviles_1.model.CoverId

import com.example.proyecto_final_moviles_1.model.Manga
import com.example.proyecto_final_moviles_1.model.MangaId
import com.example.proyecto_final_moviles_1.modelChapter.ChapterVolume
import com.example.proyecto_final_moviles_1.modelImage.ImageFile
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface MangasApi {

    //listar mangas
    @GET("manga?limit=30")
    suspend fun getAllMangas(@Query("title") query: String? = null): Manga

    //listar mangas por id

    @GET("manga/{id}")
    suspend fun getMangaId(@Path("id") id: String? = null): MangaId

    //mostrar imagenes de portada
    @GET("cover/{mangaOrCoverId}")
    suspend fun getAllCover(@Path("mangaOrCoverId") mangaOrCoverId: String): CoverId

    //Obtener detalles de capitulos de manga
    @GET("manga/{id}/feed?translatedLanguage[]=en ")
    suspend fun getAllChapters(@Path("id") id: String,@Query("order[chapter]") chapterOrder: String = "asc"): ChapterVolume

    //Obtener imagefile
    @GET("at-home/server/{chapterId}")
    suspend fun getAtHome(@Path("chapterId") chapterId:String) : ImageFile


}





