package com.example.gastosdiariosjetapckcompose.domain.model

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.features.configuration.ConfigurationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogCustom {

    private fun pantallaInfo(pantalla: String, context: Context): PantallaInfo {
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
                    iconoResId = R.drawable.ic_futbol // ID del recurso de icono para otras pantallas
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
        opcionesEliminar: List<OpcionEliminarModel>,
        onConfirm: (Set<OpcionEliminarModel>) -> Unit
    ) {
        val pantallaInfo = pantallaInfo(pantalla, context)
        var isReset by remember { mutableStateOf(false) }
        var selectedOptions by remember { mutableStateOf(setOf<OpcionEliminarModel>()) }


        Dialog(onDismissRequest = { onDismiss() }) {
            Card(shape = RoundedCornerShape(28.dp)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    val rotationDegrees by animateFloatAsState(
                        targetValue = if (isReset) 1080f else 0f, // 360 grados x 3 vueltas
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 3000, // Duración total de la animación en milisegundos
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Restart
                        ), label = "floatAnimation"
                    )

                    Image(
                        painter = painterResource(id = pantallaInfo.iconoResId),
                        contentDescription = "refresh",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .rotate(rotationDegrees),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                    //titulo
                    Text(
                        text = pantallaInfo.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    //Cuerpo dialogo
                    Text(
                        text = pantallaInfo.texto,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    opcionesEliminar.forEach { option ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selectedOptions.contains(option),
                                onCheckedChange = { isChecked ->
                                    val newSelections = selectedOptions.toMutableSet()
                                    if (isChecked) {
                                        newSelections.add(option)
                                    } else {
                                        newSelections.remove(option)
                                    }
                                    selectedOptions = newSelections
                                }
                            )

                            Text(text = option.nombre)
                        }
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                    Row(Modifier.fillMaxWidth(), Arrangement.End) {
                        TextButton(onClick = { onDismiss() }) {
                            Text(text = stringResource(id = android.R.string.cancel))
                        }
                        val scope = rememberCoroutineScope()
                        TextButton(onClick = {
                            isReset = true

                            scope.launch(Dispatchers.Main) {
                                //le ponemos un retardo para que el icono tenga la animacion
                                delay(3000)//retardo de 3 segundos
                                // Ejecutar las acciones correspondientes a las opciones seleccionadas
                                onAccept()
                                onConfirm(selectedOptions)
                            }
                        }
                        ) {
                            Text(
                                text = stringResource(id = android.R.string.ok)
                            )
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
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {

                    sharedLogic.LoaderData(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(60.dp),
                        image = R.raw.congratulation_lottie,
                        repeat = false
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                    //titulo
                    Text(
                        text = pantallaInfo.titulo,
                        style = MaterialTheme.typography.titleMedium,
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

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = {
                                onAccept()
                            },
                        ) {
                            Text(text = stringResource(id = android.R.string.ok))
                        }

                    }
                }
            }
        }
    }
}

