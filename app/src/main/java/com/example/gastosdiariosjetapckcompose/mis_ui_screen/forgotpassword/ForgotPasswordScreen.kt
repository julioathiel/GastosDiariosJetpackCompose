package com.example.gastosdiariosjetapckcompose.mis_ui_screen.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    forgotPasswordViewModel: ForgotPasswordViewModel
) {
    val emailForgot: String by forgotPasswordViewModel.forgotEmail.observeAsState(initial = "")
    //controlando estado del botton desde el registerviewModel
    val isForgotEnable: Boolean by forgotPasswordViewModel.isForgotEnabled.observeAsState(initial = false)

    //  var password by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101014))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TitleText("Ingresa tu e-mail para cambiarla")
            Spacer(modifier = Modifier.padding(20.dp))
            ForgotEmail(emailForgot) {
                forgotPasswordViewModel.onForgotChanged(emailForgot = it)
            }

        }
        ButtonConfirm(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp), isForgotEnable, navController
        )
    }
}


@Composable
fun TitleText(s: String) {
    Text(
        text = s,
        modifier = Modifier.padding(vertical = 20.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White
    )
}

@Composable
fun ForgotEmail(email: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xFF8247C5),
            unfocusedIndicatorColor = Color.Transparent
        ),
        supportingText = { Text(text = "Revisa que el formato sea el correcto.") }
    )
}

@Composable
fun ButtonConfirm(modifier: Modifier, isRegisterEnable: Boolean, navController: NavController) {
    Button(
        onClick = { },
        enabled = isRegisterEnable,
        modifier = modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            //boton desabilitado
            disabledContainerColor = Color.Gray,
            containerColor = Color(0xFF8247C5)
        )
    ) {
        Text(text = "Continuar", color = Color.White)
    }
}

