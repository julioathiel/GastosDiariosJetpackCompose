package com.example.gastosdiariosjetapckcompose.domain.model

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.gastosdiariosjetapckcompose.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogCustom {

    fun pantallaInfo(pantalla: String, context: Context): PantallaInfo {
        return when (pantalla) {
            "homscreen" -> {
                PantallaInfo(
                    titulo = "Pantalla Principal",
                    texto = "Contenido del texto",
                    iconoResId = R.drawable.ic_home // ID del recurso de icono para la pantalla principal
                )
            }

            "pantallaregistoCategoria" -> {
                PantallaInfo(
                    titulo = context.getString(R.string.reiniciarAplicacion),
                    texto = "Aceptas eliminar todo?",
                    iconoResId = R.drawable.ic_refresh// ID del recurso de icono para la pantalla de registro de categoría
                )
            }
            "reinicioCompletado" -> {
                PantallaInfo(
                    titulo = context.getString(R.string.reinicioCompletado),
                    texto = context.getString(R.string.reseteoConfirmado),
                    iconoResId = R.drawable.ic_check// ID del recurso de icono para la pantalla de registro de categoría
                )
            }

            else -> {
                PantallaInfo(
                    titulo = "Titulo por defecto",
                    texto = "Contenido por defecto",
                    iconoResId = com.google.android.material.R.drawable.m3_appbar_background // ID del recurso de icono para otras pantallas
                )
            }
        }
    }

    @Composable
    fun MostrarDialogo(
        context: Context,
        pantalla: String,
        onDismiss: () -> Unit,
        onAccept: () -> Unit,
    ) {
        val pantallaInfo = pantallaInfo(pantalla, context)

        Dialog(onDismissRequest = { onDismiss() }) {
            Card(shape = RoundedCornerShape(28.dp)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Icon(
                        painterResource(id = pantallaInfo.iconoResId),
                        contentDescription = "icono",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    //titulo
                    Text(
                        text = pantallaInfo.titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    //Cuerpo dialogo
                    Text(
                        text = pantallaInfo.texto,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.size(24.dp))

                    Row(Modifier.fillMaxWidth(), Arrangement.End) {
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(text = "Cancelar", color = colorResource(id = R.color.purple_500))
                        }
                       val scope = rememberCoroutineScope()
                        Button(
                            onClick = {
                                onAccept()
                                // Detiene la animación del icono de refresh después de un tiempo de espera
                                scope.launch {
                                    delay(2000) // Ajusta el tiempo de espera según tus necesidades
                                }
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(text = "Aceptar", color = colorResource(id = R.color.purple_500))
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun MostrarDialogoCongratulations(
        context: Context,
        pantalla: String,
        onDismiss: () -> Unit,
        onAccept: () -> Unit,
    ) {
        val pantallaInfo = pantallaInfo(pantalla, context)

        Dialog(onDismissRequest = { onDismiss() }) {
            Card(shape = RoundedCornerShape(28.dp)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Icon(
                        painterResource(id = pantallaInfo.iconoResId),
                        contentDescription = "icono",
                        tint = Color.Green,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    //titulo
                    Text(
                        text = pantallaInfo.titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    //Cuerpo dialogo
                    Text(
                        text = pantallaInfo.texto,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.size(24.dp))

                    Row(Modifier.fillMaxWidth(), Arrangement.End) {
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(text = "Cancelar", color = colorResource(id = R.color.purple_500))
                        }
                        Button(
                            onClick = {
                                onAccept()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(text = "Aceptar", color = colorResource(id = R.color.purple_500))
                        }

                    }
                }
            }
        }
    }
}

