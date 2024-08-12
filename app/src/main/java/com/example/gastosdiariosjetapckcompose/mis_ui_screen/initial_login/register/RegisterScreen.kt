package com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.ui.theme.ColorBlack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController) {
    val uiState by viewModel.uiState

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.crear_cuenta),
                    color = Color.White
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBlack)
        )
    }, containerColor = ColorBlack) { paddingValues: PaddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = stringResource(id = R.string.email), color = Color.White, style = MaterialTheme.typography.titleMedium)
            EmailRegisterField(
                valueEmail = uiState.email,
                onNewValue = viewModel::onEmailChange,
                Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = stringResource(id = R.string.password),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            PasswordRegisterField(
                valuePassword = uiState.password,
                onNewValue = viewModel::onPasswordChange,
                Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.password_error),
                color = Color.White, style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            ButtonRegister(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).height(56.dp),
                title = stringResource(id = R.string.crear_cuenta),
                onClick = { viewModel.onSignUpClick() }) {
                viewModel.enabledButtonRegister()
            }
        }
    }

}

@Composable
fun EmailRegisterField(
    valueEmail: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = valueEmail,
        onValueChange = { onNewValue(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        //   isError = emailError,
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun PasswordRegisterField(
    valuePassword: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = valuePassword,
        onValueChange = { onNewValue(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisibility) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = image, contentDescription = "show password")
            }
        }, visualTransformation = if (passwordVisibility) {
            //si passwordVisibility es true...no hara nada, osea se muestra el texto
            VisualTransformation.None
        } else {
            //no se ve nada, pone puntitos ....
            PasswordVisualTransformation()
        }, colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun ButtonRegister(modifier:Modifier,title: String, onClick: () -> Unit, onEnabled: () -> Boolean) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        enabled = onEnabled(),
        colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.Gray)
    ) {
        Text(text = title)
    }
}

