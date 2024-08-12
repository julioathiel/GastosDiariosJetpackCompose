package com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.user_profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import androidx.compose.ui.unit.dp
import com.example.gastosdiariosjetapckcompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(viewModel: UserProfileViewModel, onLogout: () -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Perfil") })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // Foto de perfil
            Box(modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .clickable {
                    // Lógica para cambiarla foto de perfil
                }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_persona_circle), // Reemplazacon la imagen actual del usuario
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(128.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
                // Icono para cambiar la foto
                IconButton(
                    onClick = { /* Lógica para cambiar la foto de perfil */ },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle, // O cualquier otro ícono que prefieras
                        contentDescription = "Cambiar foto de perfil",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Nombre del usuario
            val nameUser: String = viewModel.getNameUser().toString()
            Text(
                text = nameUser ?: "Nombre de usuario",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(32.dp))
            // Opciones del perfil
            Button(onClick = { /* Lógica para editar el perfil */ }) {
                Text("Editar perfil")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { showDialog = true }) {
                Text("Eliminar cuenta")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                viewModel.signOut()
            }) {
                Text("Cerrar sesión")
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(onClick = {
                    // Lógica para eliminar la cuenta
                    viewModel.deleteUser()
                    showDialog = false
                    // Navega a la pantalla de inicio de sesión o realiza otras acciones necesarias
                    onLogout()
                    Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}