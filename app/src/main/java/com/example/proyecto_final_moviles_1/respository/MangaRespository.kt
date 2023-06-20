package com.example.proyecto_final_moviles_1.respository

import android.content.ClipData
import com.example.proyecto_final_moviles_1.data.DataOrException
import com.example.proyecto_final_moviles_1.data.Resource
import com.example.proyecto_final_moviles_1.di.AppModule.provideMangaApi
import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.model.DataX
import com.example.proyecto_final_moviles_1.model.Manga
import com.example.proyecto_final_moviles_1.model.MangaId
import com.example.proyecto_final_moviles_1.modelChapter.ChapterVolume
import com.example.proyecto_final_moviles_1.modelImage.ImageFile
import com.example.proyecto_final_moviles_1.network.MangasApi
import com.google.api.ResourceProto.resource
import retrofit2.Retrofit
import javax.inject.Inject

class MangaRespository @Inject constructor(private val api: MangasApi) {


    suspend fun getMangas(searchQuery: String): Resource<List<Data>> {
        return try {
            Resource.Loading(data = true)
            val datalist = api.getAllMangas(searchQuery).data
            if (datalist.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = datalist)


        }catch(exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }



    }
    suspend fun getMangaInfo(id: String): Resource<MangaId>{

        if (id.isBlank()) {
            return Resource.Error(message = "Invalid manga ID")
        }

        val response = try {
            Resource.Loading(data =true)
            api.getMangaId(id)

        }catch(exception:Exception) {
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }


    suspend fun getTitles(id: String): Resource<ChapterVolume>{

        if (id.isBlank()) {
            return Resource.Error(message = "Invalid ID")
        }

        val response = try {
            Resource.Loading(data =true)
            api.getAllChapters(id)

        }catch(exception:Exception) {
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }



    suspend fun getImageFile(id: String): Resource<ImageFile> {

        if (id.isBlank()) {
            return Resource.Error(message = "Invalid ID")
        }


        val  response = try {
            Resource.Loading(data = true)
            api.getAtHome(id)

        }catch(exception: Exception){
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }


}






