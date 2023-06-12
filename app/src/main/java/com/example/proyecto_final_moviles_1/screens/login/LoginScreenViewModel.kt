package com.example.proyecto_final_moviles_1.screens.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_moviles_1.model.MUser
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
    //val loadingState = MutableStateFlow(LoadingState.IDLE) por ahora no se por lo haremos internamente
    private val auth: FirebaseAuth = Firebase.auth


    private val _loading = MutableLiveData(false) // sirver para sostener data reactiva
    val loading: LiveData<Boolean> = _loading

    val errorMessage: MutableState<String?> = mutableStateOf(null)


    // Login
    fun singInWithEmailAndPassword(email: String, password: String, home: ()-> Unit)
    = viewModelScope.launch{ // lo metemos a un scope para que corra en otra coroutine parqa que no hayan problemas
        try{
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        Log.d("FB", "singInWithEmailAndPassword yay: ${task.result.toString()}")
                        home()
                    }else {
                        val error = ""
                        errorMessage.value = error
                    }

                }
        }catch(e: Exception){
            Log.d("FB","singInWithEmailAndPassword:${e.message}")
        }
    }


    // se crea el usuario en account en firebase
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: ()-> Unit
    ){
        if(_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        val displayName = task.result?.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    }else{
                        Log.d("FB", "createUserWithEmailAndPassword: ${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }
    }

    // aqui añadimos al firestore
    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid  // este es basicamente la sesion
        val user = MUser(userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Hola, ¿como estan?",
            profession = "Alumno Epis",
            id= null).toMap()


        FirebaseFirestore.getInstance().collection("users")
            .add(user)

    }
}