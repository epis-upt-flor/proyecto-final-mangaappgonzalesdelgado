package com.example.proyecto_final_moviles_1.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class MManga(
    @Exclude var id: String? = null,
    var title: String? = null,
    var gender: List<String>? = null,
    var description: String? = null,
    var notes : String? = null,
    var rating: Double? = null,

    @get:PropertyName("url_Image")
    @set:PropertyName("url_Image")
    var urlImage: String? = null,

    @get:PropertyName("started_reading_at")
    @set:PropertyName("started_reading_at")
    var startedReading: Timestamp? = null,

    @get:PropertyName("finished_reading_at")
    @set:PropertyName("finished_reading_at")
    var finishedReading: Timestamp? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,

    @get:PropertyName("Manga_Id")
    @set:PropertyName("Manga_Id")
    var MangaId: String? = null
)
