package com.example.proyecto_final_moviles_1.respository

import com.example.proyecto_final_moviles_1.data.DataOrException
import com.example.proyecto_final_moviles_1.model.MManga
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


//@HiltViewModel
class FireRepository @Inject constructor(private val queryManga: Query) {

    suspend fun getAllMangasFromDatabase(): DataOrException<List<MManga>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MManga>, Boolean, Exception>()

        try {

            dataOrException.loading = true
            dataOrException.data = queryManga.get()
                .await().documents.map { documentSnapshot -> documentSnapshot.toObject(MManga::class.java)!! }

            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false

        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }
        return dataOrException
    }

}