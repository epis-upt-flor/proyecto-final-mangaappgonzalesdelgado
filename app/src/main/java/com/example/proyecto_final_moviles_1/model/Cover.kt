package com.example.proyecto_final_moviles_1.model

data class Cover(
    val `data`: List<DataX>,
    val limit: Int,
    val offset: Int,
    val response: String,
    val result: String,
    val total: Int
)