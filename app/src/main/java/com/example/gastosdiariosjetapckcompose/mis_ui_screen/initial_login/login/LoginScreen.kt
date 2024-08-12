package com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.login

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.ui.theme.ColorBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel, navigatoToHome: () -> Unit) {
    val uiState by viewModel.uiState
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    val isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val isShowSnackbar = remember { SnackbarHostState() }

    // Navegar en caso de éxito
    loginSuccess?.let { isSuccess ->
        if (isSuccess) {
            LaunchedEffect(loginSuccess) {
                navigatoToHome()
                viewModel.resetLoginSuccess() // Asegúrate de agregar esta función en tu ViewModel para resetear el estado
            }
        }
    }

    // Mostrar snackbar
    snackbarMessage?.let { messageResId ->
        val context = LocalContext.current
        LaunchedEffect(snackbarMessage) {
            errorMessage = context.getString(R.string.email_error)
            isShowSnackbar.showSnackbar(context.getString(messageResId))
            viewModel.resetSnackbarMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Login", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBlack)
            )
        },
        snackbarHost = { SnackbarHost(hostState = isShowSnackbar) },
        containerColor = ColorBlack
    ) { paddingValues: PaddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.email),
                color = Color.White,
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.titleMedium
            )

            EmailField(
                value = uiState.email,
                onNewValue = viewModel::onEmailChange,
                errorMessage = errorMessage,
                emailError = isError,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.password),
                modifier = Modifier.padding(top = 10.dp),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            PasswordField(
                valuePassword = uiState.password,
                onNewValue = viewModel::onPasswordChange,
                Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            ButtonLogin(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                title = stringResource(id = R.string.iniciar_sesion),
                onClick = { viewModel.onSignUpClick() }) {
                viewModel.enabledButtonRegister()
            }
        }
    }
}

@Composable
fun EmailField(
    value: String,
    onNewValue: (String) -> Unit,
    errorMessage: String,
    emailError: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = emailError
    )
}

@Composable
fun PasswordField(
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
        }
    )
}

@Composable
fun ButtonLogin(modifier: Modifier, title: String, onClick: () -> Unit, onEnabled: () -> Boolean) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        enabled = onEnabled(),
        colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.Gray)
    ) {
        Text(text = title)
    }
}


