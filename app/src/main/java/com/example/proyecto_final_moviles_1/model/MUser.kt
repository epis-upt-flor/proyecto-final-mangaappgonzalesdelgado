package com.example.proyecto_final_moviles_1.model

data class MUser(
    val id: String?,
    val userId: String,
    val displayName: String,
    val avatarUrl: String,
    val quote: String,
    val profession: String){
    fun toMap():MutableMap<String,Any>{ // para convertir las variables de arriba y poder meterlas en un mutablemap y enviarlas a FB
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "quote" to this.quote,
            "profession" to this.profession,
            "avatar_url" to this.avatarUrl
        )
    }
}
