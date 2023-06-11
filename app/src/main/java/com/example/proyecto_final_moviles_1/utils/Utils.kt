package com.example.proyecto_final_moviles_1.utils

import android.icu.text.DateFormat
import com.google.firebase.Timestamp

fun formatDate(timestamp: Timestamp): String {
    return DateFormat.getDateInstance().format(timestamp.toDate()).toString()
        .split(",")[0] // March 12
}

