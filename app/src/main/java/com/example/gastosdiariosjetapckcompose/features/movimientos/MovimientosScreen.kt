package com.example.gastosdiariosjetapckcompose.features.movimientos

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.uiState.MovimientosUiState
import com.example.gastosdiariosjetapckcompose.navigation.Routes

@Composable
fun MovimientosScreen(
    navController: NavController,
    movimientosViewModel: MovimientosViewModel
) {

    BackHandler {
        //al presion el boton fisico de retoceso, se dirige a la pantalla de configuracion
        navController.navigate(Routes.HomeScreen.route)
    }
    Scaffold(topBar = { Toolbar() },
        content = { miPadding ->
            Content(movimientosViewModel, Modifier.padding(miPadding))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.toolbar_registro_gastos),
                color = colorResource(id = R.color.black)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun Content(movimientosViewModel: MovimientosViewModel, padding: Modifier) {
//creando el ciclo de vida de la pantalla
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle

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
                    .background(color = colorResource(id = R.color.background))
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
                sharedLogic.IsEmpty()
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.grayUno))
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
    //agrupando las fechas que coincidan
    val movimientosPorFecha = transaccionesRevertidas.groupBy { it.date }
    //ordenando fechas por orden descendentes, esto creara que los mas recientes esten arriba
    val fechasOrdenadas = movimientosPorFecha.keys.sortedByDescending { it }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 65.dp),
        state = listState
    ) {

        items(fechasOrdenadas) { fecha ->

            ItemFecha(fecha)
            // Mostrar los movimientos correspondientes a la fecha
            val movimientosFecha = movimientosPorFecha[fecha] ?: emptyList()
            movimientosFecha.forEach { transaccion ->
                ItemCardAllExpenses(listTransacciones, transaccion, movimientosViewModel)
            }
        }
//        items(transaccionesRevertidas, key = { it.id }) { transaccion ->
//            ItemCardAllExpenses(listTransacciones, transaccion, movimientosViewModel)
//        }
    }
    LaunchedEffect(listTransacciones.size) {
        // muestra el ultimo elemento agregado en la parte superior
        listState.scrollToItem(index = 0)
    }
}

@Composable
fun ItemFecha(fecha: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(30.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = fecha,
            style = MaterialTheme.typography.labelMedium
        )
    }
}


@Composable
fun ItemCardAllExpenses(
    listTransacciones: List<MovimientosModel>,
    transaccion: MovimientosModel,
    movimientosViewModel: MovimientosViewModel
) {
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val showConfirmationEditar = remember { mutableStateOf(false) }
    val showConfirmationEliminar = remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }
    var isLongPressed by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    val textColor = if (transaccion.select) {
        //si el usuario eligio ingreso, el color de los numeros sera verde
        colorResource(id = R.color.verdeDinero)
    } else colorResource(id = R.color.black) //sin color

    LaunchedEffect(isClicked) {
        isClicked = false
        isLongPressed = false
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                when {
                    isLongPressed -> Color.LightGray // Color de fondo cuando se produce un clic prolongado
                    isClicked -> Color.Gray // Color de fondo cuando se produce un clic normal
                    else -> Color.White
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isExpanded = !isExpanded
                }, onLongPress = {
                    // Manejar el clic prolongado
                    isClicked = true
                    isLongPressed = true
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

        //contenedor de icono
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = colorResource(id = R.color.grayUno),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Icono dentro del círculo
            Image(
                painter = painterResource(id = iconResourceId),
                contentDescription = null,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(colorResource(id = R.color.black))
            )
        }

        Spacer(modifier = Modifier.padding(start = 16.dp))
        //contenedor de titulo y subtitulo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaccion.title,
                    color = colorResource(id = R.color.black),
                    fontFamily = FontFamily(Font(R.font.lato_black)),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    // Mostrar "..." si el texto excede una línea
                    overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                )
                if (transaccion.subTitle.isNotEmpty()) {
                    Text(
                        text = transaccion.subTitle,
                        color = colorResource(id = R.color.grayCuatro),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 16.sp,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        // Mostrar "..." si el texto excede una línea
                        overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                    )
                }
            }
            //contenedor de dinero y fecha
            Column(Modifier.padding(end = 8.dp), horizontalAlignment = Alignment.End) {
                Text(
                    text = movimientosViewModel.formattedCurrency(transaccion.cash.toDouble()),
                    color = textColor,
                    fontFamily = FontFamily(Font(R.font.lato_bold))
                )
                //texto que muestra fecha
//                Text(
//                    text = transaccion.date,
//                    color = colorResource(id = R.color.grayCuatro),
//                    fontSize = 12.sp,
//                    fontFamily = FontFamily(Font(R.font.lato_regular))
//                )
            }
        }
        Spacer(modifier = Modifier.padding(40.dp))
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

    Dialog(onDismissRequest = { onDissmis() }) {
        Spacer(modifier = Modifier.size(16.dp))
        Card(shape = RoundedCornerShape(30.dp)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Cambiar",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.lato_bold)),
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.size(30.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.End) {
                    BotonOpcion(title = "Eliminar") {
                        showConfirmationEliminar.value = true
                    }
                    BotonOpcion(title = "Editar") {
                        showConfirmationEditar.value = true
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
                        movimientosViewModel.onItemRemoveMov(listTransacciones, transaccion)
                    }
                }
            }
        }
    }
}

@Composable
fun BotonOpcion(title: String, onEjecutarOpcion: () -> Unit) {
    OutlinedButton(
        onClick = { onEjecutarOpcion() },
        modifier = Modifier.width(130.dp),
        border = BorderStroke(width = 1.dp, color = Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = colorResource(id = R.color.blue)
        )
    ) {
        Text(text = title)
    }
}
