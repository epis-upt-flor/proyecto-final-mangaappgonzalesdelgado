package com.example.proyecto_final_moviles_1

import com.example.proyecto_final_moviles_1.data.Resource
import com.example.proyecto_final_moviles_1.model.Data
import com.example.proyecto_final_moviles_1.network.MangasApi
import com.example.proyecto_final_moviles_1.respository.MangaRespository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets


//simulador de manga dex
class ExampleUnitTest {
   private val mockWebServer = MockWebServer()

    private val newProvider = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MangasApi::class.java)

    private val newsRepository = MangaRespository(newProvider)


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getAllMangasTest(){
        mockWebServer.enqueueResponse("responseListMangas.json")
        runBlocking{
            val mangas : Resource<List<Data>> = newsRepository.getMangas("youjo senki")

            val expectedManga = 5 // cambiar el valor para ver la excepcion
            assertTrue("El tamaño de la lista de mangas no coincide.", mangas.data?.size!! == expectedManga )
//

            val expectedId = "d773c8be-8e82-4ff1-a4e9-46171395319b"// cambiar el valor para ver la excepcion
            assertTrue("El ID del manga no coincide.", mangas.data?.get(0)?.id == expectedId )


        }
    }


    @Test
    fun getMangaIdTest(){
        mockWebServer.enqueueResponse("responseIdMangas.json")
        runBlocking{
            val mangasId =newsRepository.getMangaInfo("d773c8be-8e82-4ff1-a4e9-46171395319b")

            assertEquals("Youjo Senki", mangasId.data?.data?.attributes?.title?.en)
            assertEquals("published", mangasId.data?.data?.attributes?.state)

            val lista = listOf("Reincarnation", "Genderswap", "Historical","Action","Psychological","Survival","Magic",
                "Military","Isekai","Gore","Drama","Fantasy","Adaptation")

            assertEquals(lista, mangasId.data?.data?.attributes?.tags?.map { tag ->tag.attributes.name.en})

        }

    }


    @Test
    fun getAllChaptersTest(){
        mockWebServer.enqueueResponse("responseTitleList.json")
        runBlocking {
            val chaptersList = newsRepository.getTitles("d773c8be-8e82-4ff1-a4e9-46171395319b")

            assertEquals(4, chaptersList.data?.data?.size) // numero orginal 65, pero en esta prueba se limita la respuesta del api a 4

            val titleList = listOf("The Sky in Norden","Type 95 Elinium Operation Orb","Die Wacht am Rhein I","Die Wacht am Rhein II")

            assertEquals(titleList,chaptersList.data?.data?.map { it.attributes.title })
        }
    }

    @Test
    fun getAtHomeTest(){
        mockWebServer.enqueueResponse("responseImageChapter.json")
        runBlocking {
            val imageFile =newsRepository.getImageFile("c30e8966-cef8-46c6-bc31-f24f6827bf84")


            assertEquals("https://uploads.mangadex.org",imageFile.data?.baseUrl)
            assertEquals("132c890721a6e1998be6501561e6e6f5",imageFile.data?.chapter?.hash)
            assertEquals("1-f6fe9feae6cebf03bed14d9e28966f41ddbb46256ff4183529976fed5f9706df.jpg",
                imageFile.data?.chapter?.data?.get(0)
            )
        }
    }


}

fun MockWebServer.enqueueResponse(filePath : String){
    val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
    val source = inputStream?.source()?.buffer()
    source?.let {
        enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(it.readString(StandardCharsets.UTF_8))
        )
    }
}



