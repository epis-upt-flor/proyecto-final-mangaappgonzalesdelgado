package com.example.proyecto_final_moviles_1.di

import com.example.proyecto_final_moviles_1.network.MangasApi
import com.example.proyecto_final_moviles_1.respository.FireRepository
import com.example.proyecto_final_moviles_1.respository.MangaRespository
import com.example.proyecto_final_moviles_1.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Singleton
    @Provides
    fun provideFireMangaRespository()= FireRepository(queryManga = FirebaseFirestore.getInstance()
        .collection("mangas"))

    @Singleton
    @Provides
    fun provideBooksRepository(api: MangasApi) = MangaRespository(api)

    @Singleton
    @Provides
    fun provideMangaApi(): MangasApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MangasApi::class.java)
    }
}