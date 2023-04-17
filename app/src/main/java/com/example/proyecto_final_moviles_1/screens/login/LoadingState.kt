package com.example.proyecto_final_moviles_1.screens.login

data class LoadingState(val status: Status, val message: String? = null) {

    companion object{
        val IDLE = LoadingState(Status.IDLE)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val LOADING= LoadingState(Status.LOADING)
        val FAILED = LoadingState(Status.FAILED)
    }

    enum class Status{
        LOADING,
        FAILED,
        SUCCESS,
        IDLE
    }

}
