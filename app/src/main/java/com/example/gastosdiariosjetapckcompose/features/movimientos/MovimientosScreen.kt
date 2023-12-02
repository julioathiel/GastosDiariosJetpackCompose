package com.example.gastosdiariosjetapckcompose.features.movimientos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.SharedLogic
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel


@Composable
fun MovimientosScreen(
    NavController: NavController,
    movimientosViewModel: MovimientosViewModel,
    homeViewModel: HomeViewModel
) {
    //creando el ciclo de vida de la pantalla
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<MovimientosUiState>(
        initialValue = MovimientosUiState.Loading,
        key1 = lifecycle,
        key2 = movimientosViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            movimientosViewModel.uiState.collect { value = it }
        }
    }
    when (uiState) {
        is MovimientosUiState.Error -> {}
        MovimientosUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                CircularProgressIndicator(
                    Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                )
            }
        }

        is MovimientosUiState.Success -> {
            //si hay datos sera el unico momento en donde se pinta la pantalla
            // Obtener la lista de transacciones desde el ViewModel
            val movimientos = (uiState as MovimientosUiState.Success).movimientos
            if (movimientos.isEmpty()) {
                // Si la lista está vacía, mostrar un mensaje o un indicador visual
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    Text(
                        text = "No hay transacciones disponibles",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    MovimientosList(
                        listTransacciones = (uiState as MovimientosUiState.Success).movimientos,
                        movimientosViewModel
                    )
                }
            }
        }

    }
}


@Composable
fun MovimientosList(
    listTransacciones: List<MovimientosModel>,
    movimientosViewModel: MovimientosViewModel
) {
    // Invertir el orden de la lista asi lo  que se grega va quedando arriba
    val transaccionesRevertidas = listTransacciones.reversed()
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        state = listState
    ) {
        items(transaccionesRevertidas, key = { it.id }) { transaccion ->
            ItemCardMovimientos(listTransacciones, transaccion, movimientosViewModel)
        }
    }
    LaunchedEffect(listTransacciones.size) {
        // muestra el ultimo elemento agregado en la parte superior
        listState.scrollToItem(index = 0)
    }
}

@Composable
fun ItemCardMovimientos(
    listTransacciones: List<MovimientosModel>,
    transaccion: MovimientosModel,
    movimientosViewModel: MovimientosViewModel
) {
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val showConfirmationEditar = remember { mutableStateOf(false) }
    val showConfirmationEliminar = remember { mutableStateOf(false) }

    val textColor = if (transaccion.select) {
        //si el usuario eligio ingreso, el color de los numeros sera verde
        Color(0xFF98FF98)
    } else Color.White //sin color
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .height(72.dp)
            .clickable { }
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    showConfirmationDialog.value = true
                })
            }
    ) {
        Spacer(modifier = Modifier.padding(start = 16.dp))
        val iconResourceId = LocalContext.current.resources.getIdentifier(
            transaccion.iconResourceName,
            "drawable",
            LocalContext.current.packageName
        )
        Image(
            painter = painterResource(id = iconResourceId),
            contentDescription = null,
            alignment = Alignment.Center,
            colorFilter = ColorFilter.tint(color = Color.LightGray)
        )

        Spacer(modifier = Modifier.padding(start = 16.dp))
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = transaccion.title, modifier = Modifier.weight(1f),
                    color = Color.White, fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = transaccion.cash, modifier = Modifier.padding(end = 16.dp),
                    color = textColor
                )
            }
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = transaccion.subTitle,
                    color = Color(0XFF52576B),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = transaccion.date,
                    color = Color(0XFF52576B),
                    modifier = Modifier.padding(end = 16.dp),
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        // Mostrar un AlertDialog para confirmar la eliminación

        if (showConfirmationDialog.value) {
            DialogOpcion(
                onDissmis = { showConfirmationDialog.value = false },
                showConfirmationEditar,
                showConfirmationEliminar,
                movimientosViewModel,
                listTransacciones,
                transaccion
            )
        }

    }

}

@Composable
fun DialogOpcion(
    onDissmis: () -> Unit,
    showConfirmationEditar: MutableState<Boolean>,
    showConfirmationEliminar: MutableState<Boolean>,
    movimientosViewModel: MovimientosViewModel,
    listTransacciones: List<MovimientosModel>,
    transaccion: MovimientosModel,
) {
    val sharedLogic = SharedLogic()

    Dialog(onDismissRequest = { onDissmis() }) {
        Spacer(modifier = Modifier.size(16.dp))
        Card(shape = MaterialTheme.shapes.small) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Elige una opcion",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(30.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                    BotonOpcion(title = "Editar") {
                        showConfirmationEditar.value = true
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                    BotonOpcion(title = "Eliminar") {
                        showConfirmationEliminar.value = true
                    }

                    if (showConfirmationEditar.value) {
                        sharedLogic.AddTransactionDialog(
                            transaccion,
                            showDialogTransaccion = showConfirmationEditar.value,
                            onDissmis = {
                                showConfirmationEditar.value = false
                                onDissmis()
                            },
                            movimientosViewModel = movimientosViewModel,
                            listTransacciones
                        )
                    }
                    if (showConfirmationEliminar.value) {
                        ConfirmationDialog(
                            showDialog = showConfirmationEliminar.value,
                            onConfirm = {
                                movimientosViewModel.onItemRemoveMov(
                                    listTransacciones,
                                    transaccion
                                )
                                showConfirmationEliminar.value = false
                            },
                            onDismiss = { showConfirmationEliminar.value = false }
                        )
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun BotonOpcion(title: String, onEjecutarOpcion: () -> Unit) {
    OutlinedButton(
        onClick = { onEjecutarOpcion() },
        modifier = Modifier.width(130.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 1.dp, color = Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFF8247C5),
            contentColor = Color.White
        )
    ) {
        Text(text = title)
    }
}

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Eliminar elemento") },
            text = { Text(text = "¿Estás seguro de eliminar este elemento?") },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    Text(text = "Si, Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(text = "Cancelar", color = Color.White)
                }
            }
        )
    }
}
