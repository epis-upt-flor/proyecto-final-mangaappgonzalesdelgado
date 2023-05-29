package com.example.proyecto_final_moviles_1.data

sealed class ResourceFile<T>(
    val status: Status,
    val data: T? = null,
    val error: Throwable? = null
) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    class Success<T>(data: T) : ResourceFile<T>(Status.SUCCESS, data)
    class Error<T>(error: Throwable?) : ResourceFile<T>(Status.ERROR, error = error)
    class Loading<T> : ResourceFile<T>(Status.LOADING)
}
