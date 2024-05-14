package com.example.gastosdiariosjetapckcompose.features.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.gastosdiariosjetapckcompose.navigation.Routes


@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navController:NavController) {
    BackHandler {
        //al presion el boton fisico de retoceso, se dirige a la pantalla de configuracion
        navController.navigate(Routes.ConfigurationScreen.route)
    }

    val emailRegister: String by registerViewModel.registerEmail.observeAsState(initial = "")
    //controlando estado del botton desde el registerviewModel
    val isRegisterEnable:Boolean by registerViewModel.isRegisterEnable.observeAsState(initial = false)

  //  var password by remember { mutableStateOf("") }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF101014))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TitleText("Registrate con tu email")
            SubTitle("Te vamos a enviar un correo a esa direccion para validarlo.")
            RegisterEmail(emailRegister){
                registerViewModel.onRegisterChanged(registerEmail = it)
            }

        }
        ButtonConfirm(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp), isRegisterEnable, navController)
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
fun SubTitle(s: String) {
    Text(
        text = s, modifier = Modifier.padding(bottom = 12.dp),
        fontSize = 14.sp,
        color = Color.Gray
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEmail(email: String, onTextChanged: (String) -> Unit) {
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
            supportingText = { Text(text = "Revisa que el formato sea el correcto.")}
        )
}

@Composable
fun ButtonConfirm(modifier: Modifier, isRegisterEnable: Boolean, navController: NavController) {
    Button(
        onClick = { },
        enabled =isRegisterEnable ,
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
