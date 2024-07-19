package com.example.gastosdiariosjetapckcompose.features.movimientos

import android.util.Log
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.GastosPorCategoriaModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.uiState.MovimientosUiState
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import java.text.SimpleDateFormat
import java.util.Locale

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
                text = stringResource(R.string.toolbar_registro_gastos)
            )
        }
    )
}

@Composable
fun Content(movimientosViewModel: MovimientosViewModel, modifier: Modifier) {
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
            sharedLogic.LoaderCircularProgressPantalla()
        }

        is MovimientosUiState.Success -> {
            //si hay datos sera el unico momento en donde se pinta la pantalla
            // Obtener la lista de transacciones desde el ViewModel
            val movimientos = (uiState as MovimientosUiState.Success).movimientos

            if (movimientos.isEmpty()) {
                // Si la lista está vacía, mostrar un mensaje o un indicador visual
                sharedLogic.IsEmpty()
            } else {
                Box(modifier = modifier.fillMaxSize()) {
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
    val listState = rememberLazyListState()


    // Invertir el orden de la lista asi lo  que se grega va quedando arriba
    //listTransacciones.reversed()

    //agrupando las fechas que coincidan
    val movimientosPorFecha = listTransacciones.reversed().groupBy { it.date }

    //ordenando fechas por orden descendentes, esto creara que los dias mas recientes esten arriba
    val fechasOrdenadas = movimientosPorFecha.keys.sortedByDescending { it }
    Log.d("datess","$fechasOrdenadas")

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState
    ) {
        items(fechasOrdenadas) { fecha ->
            ItemFecha(fecha)//composable que muestra la fecha
            // Mostrar los movimientos correspondientes a la fecha
            val movimientosFecha = movimientosPorFecha[fecha] ?: emptyList()
            movimientosFecha.forEach { transaccion ->
                ItemCardAllExpenses(listTransacciones, transaccion, movimientosViewModel)
            }
        }
    }
    LaunchedEffect(listTransacciones.size) {
        // muestra el ultimo elemento agregado en la parte superior
        listState.scrollToItem(index = 0)
    }
}

@Composable
fun ItemFecha(fecha: String) {
    Spacer(modifier = Modifier.padding(16.dp))
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