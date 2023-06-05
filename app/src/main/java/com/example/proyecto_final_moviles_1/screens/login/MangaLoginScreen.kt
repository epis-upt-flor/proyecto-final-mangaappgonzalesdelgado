package com.example.proyecto_final_moviles_1.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.proyecto_final_moviles_1.R
import com.example.proyecto_final_moviles_1.components.EmailInput
import com.example.proyecto_final_moviles_1.components.MangaLogo
import com.example.proyecto_final_moviles_1.components.PasswordInput
import com.example.proyecto_final_moviles_1.navigation.MangaScreens


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavController,
                viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                ){

    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top){
            MangaLogo()
            if(showLoginForm.value) UserForm(loading = false,isCreateAccount = false){
                email,password->


                viewModel.singInWithEmailAndPassword(email, password){
                    navController.navigate(MangaScreens.MangaHomeScreen.name)  // como creamos un lambda ahora mandamos el navcontroller que esta creado para poder navergar al homescreen
                }

            }
            else{
                UserForm(loading = false,isCreateAccount = true){email,password ->
                    viewModel.createUserWithEmailAndPassword(email, password){
                        navController.navigate(MangaScreens.MangaHomeScreen.name)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

        Row(modifier = Modifier.padding(15.dp).offset(y = 600.dp),
            horizontalArrangement = Arrangement.Center,

            ){
            val text = if (showLoginForm.value) "Registrarse" else "Iniciar Sesion"
            Text(text = "¿Nuevo Usuario?")
            Text(text,
            modifier = Modifier
                .clickable {  //hacemos el texto clickeable
                    showLoginForm.value = !showLoginForm.value
                }
                .padding(start = 5.dp),
            fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondaryVariant
            )
        }

        
    }

}

@ExperimentalComposeUiApi
@Preview
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (isCreateAccount) {
            Text(
                text = stringResource(id = R.string.create_acct),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(text = "Email") },
            enabled = !loading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions { passwordFocusRequest.requestFocus() },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(text = "Contraseña") },
            enabled = !loading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardActions = KeyboardActions {
                if (valid) {
                    onDone(email.value.trim(), password.value.trim())
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisibility.value = !passwordVisibility.value },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequest)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onDone(email.value.trim(), password.value.trim())
                keyboardController?.hideSoftwareKeyboard()
            },
            enabled = valid,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (isCreateAccount) "Crear cuenta" else "Iniciar sesión",
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}



@Composable
fun SubmitButton(textId: String,
                 loading: Boolean,
                 validInputs: Boolean,
                 onClick:() -> Unit) {
    Button(onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId , modifier = Modifier.padding((5.dp)))
    }
}




