package com.example.proyecto_final_moviles_1.modelChapter

data class ChapterVolume(
    val `data`: List<Data>,
    val limit: Int,
    val offset: Int,
    val response: String,
    val result: String,
    val total: Int
)