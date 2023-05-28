package com.example.proyecto_final_moviles_1.network



import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.model.Manga
import com.example.proyecto_final_moviles_1.model.cover
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface MangasApi {

    //listar mangas
    @GET("/manga")
    suspend fun getAllMangas(@Query("title") query: String? = null): Manga

    //listar mangas por id
    @GET("/manga/{id}")
    suspend fun getMangaId(@Path("id") id : String): Data

    //mostrar imagenes de portada
    @GET("/cover/{mangaOrCoverId}")
    suspend fun getAllCover(@Query("mangaOrCoverId") query: String? = null): cover


//    @GET("manga/{mangaId}")
//    suspend fun getMangaDetails(@Path("mangaId") mangaId: String): Response<MangaDetailsResponse>

}

