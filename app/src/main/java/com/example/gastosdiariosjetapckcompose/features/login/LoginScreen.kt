package com.example.gastosdiariosjetapckcompose.features.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.navigation.Routes

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
    //variable para saber si se estan cargando algunos datos antes de mostrar la pantalla principal
    val isLoading: Boolean by loginViewModel.isLoading.observeAsState(initial = false)
    Box(Modifier.fillMaxSize().background(Color.Black)) {
        if (isLoading) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                Spacer(modifier = Modifier.padding(20.dp))
                Header(Modifier.padding(top = 20.dp))
                Body(Modifier.align(Alignment.Center), loginViewModel)
                Footer(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter), loginViewModel, navController
                )
            }
        }
    }
}

@Composable
fun Header(modifier: Modifier) {
    Text(
        text = "¡Hola! Ingresa tus datos",
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
        modifier = modifier
    )
}

@Composable
fun Body(modifier: Modifier, loginViewModel: LoginViewModel) {
    val email: String by loginViewModel.email.observeAsState(initial = "")
    val password: String by loginViewModel.password.observeAsState(initial = "")


    Column(modifier = modifier) {
        Email(email) {
            loginViewModel.onLoginChanged(email = it, password = password)
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Password(password) {
            loginViewModel.onLoginChanged(email = email, password = it)
        }
    }
}

@Composable
fun Footer(modifier: Modifier, loginViewModel: LoginViewModel, navController: NavController) {
    //valor inicial del boton en false
    val isLoginEnable: Boolean by loginViewModel.isLoginEnable.observeAsState(initial = false)

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "¿Olvidaste tu clave?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.clickable { navController.navigate(Routes.ForgotPasswordScreen.route) }
        )
        Spacer(modifier = Modifier.padding(10.dp))

        IsLoginButton(isLoginEnable, navController)

        Spacer(modifier = Modifier.padding(10.dp))

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "¿Primera vez que ingresas?",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 50.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
            Text(
                text = "Crear cuenta",
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.clickable { navController.navigate(Routes.RegisterScreen.route) })
        }

        Spacer(modifier = Modifier.padding(10.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(email: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.Gray
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(password: String, onTextChanged: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Password") },
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color(0xFF8247C5),
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisibility) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Image(
                    imageVector = image, contentDescription = "show password"
                )
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            //pone los puntitos
            PasswordVisualTransformation()
        }
    )
}

@Composable
fun IsLoginButton(isLoginEnable: Boolean, navController: NavController) {
    Button(
        onClick = { navController.navigate(Routes.HomeScreen.route) },
        enabled = isLoginEnable,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            //boton desabilitado
            disabledContainerColor = Color.Gray,
            containerColor = Color(0xFF8247C5)
        )
    ) {
        Text(text = "Ingresar", color = Color.White, fontSize = 16.sp)
    }

}