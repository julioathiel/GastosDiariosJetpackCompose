package com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.provider.BeginGetCredentialRequest
import com.example.gastosdiariosjetapckcompose.R
import com.google.android.gms.auth.api.identity.AuthorizationClient
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun InitialLoginScreen(
    navigateToRegister: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    viewModel: InitialLoginViewModel
) {
    val context = LocalContext.current
    val state = viewModel.state
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.eventHandler(EventHandlerlLogin.ContinuarConGoogle(account.idToken!!))
                // Navega a la pantalla principal después de iniciar sesión:
                navigateToHomeScreen()
            } catch (e: ApiException) {
                // Maneja el error de inicio de sesión
            }
        }
    }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF202d3c),
                        Color.Black
                    ),
                    startY = 0f,
                    endY = 600f
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Icono dentro del círculo
            Image(
                painter = painterResource(id = R.drawable.logo_limitday),
                contentDescription = null,
                modifier = Modifier.size(70.dp),
                alignment = Alignment.Center
            )
        }
        Spacer(modifier = Modifier.size(50.dp))
        Text(
            text = "limitday",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Controla tus gastos",
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.size(100.dp))
        Button(
            onClick = { navigateToRegister() }, modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 32.dp)
        ) {
            Text(text = "Registrarte gratis")
        }
        Spacer(modifier = Modifier.padding(2.dp))
        CustomButton(
            { navigateToHomeScreen() },
            painterResource(id = R.drawable.ic_smartphone),
            title = "Continuar con número de teléfono"
        )
        Spacer(modifier = Modifier.padding(2.dp))
        CustomButton(
            {
                signInWithGoogle(googleSignInClient, launcher)
            },
            painterResource(id = R.drawable.ic_google),
            title = "Continuar con Goolge"
        )
        Spacer(modifier = Modifier.padding(2.dp))
        CustomButton(
            { viewModel.eventHandler(EventHandlerlLogin.ContinuarConFacebok(true)) },
            painterResource(id = R.drawable.ic_facebook),
            title = "Continuar con Facebook"
        )
        Spacer(modifier = Modifier.padding(2.dp))
        TextButton(
            onClick = { navigateToLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 32.dp)
        ) {
            Text(text = "Iniciar sesión", color = Color.White)
        }
        Spacer(modifier = Modifier.size(100.dp))
    }


}


@Composable
fun CustomButton(onClick: () -> Unit, painter: Painter, title: String) {
    OutlinedButton(
        onClick = { onClick() },
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .height(48.dp)
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
            )
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// Función para iniciar sesión con Google, incluyendo el cierre de sesión previo
fun signInWithGoogle(
    googleSignInClient: GoogleSignInClient,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    googleSignInClient.signOut().addOnCompleteListener {
        // Una vez que el cierre de sesión se complete, inicia el flujo de inicio de sesión
        launcher.launch(googleSignInClient.signInIntent)
    }
}


