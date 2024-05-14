package com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.GastosPorCategoriaModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegistroTransaccionesScreen(
    registroTransaccionesViewModel: RegistroTransaccionesViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = { Toolbar() }, content = {it -> Content(registroTransaccionesViewModel,
            Modifier.padding(it))}
    )
}

@Composable
fun Content(registroTransaccionesViewModel: RegistroTransaccionesViewModel, padding: Modifier) {
  //creando el ciclo de vida de la pantalla
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<RegistroTransaccionesUiState>(
        initialValue = RegistroTransaccionesUiState.Loading,
        key1 = lifecycle,
        key2 = registroTransaccionesViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            registroTransaccionesViewModel.uiState.collect { value = it }
        }
    }
    when (uiState) {
        is RegistroTransaccionesUiState.Error -> {}
        RegistroTransaccionesUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.grayUno))
            ) {
                CircularProgressIndicator(
                    Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                )
            }
        }

        is RegistroTransaccionesUiState.Success -> {
            // Obtener la lista de transacciones desde el ViewModel
            val listGastosPorCat =
                (uiState as RegistroTransaccionesUiState.Success).listGastosPorCat
            if (listGastosPorCat.isEmpty()) {
                // Si la lista está vacía, mostrar un mensaje o un indicador visual
                sharedLogic.IsEmpty()
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.grayUno))

                ) {
                    GastosPorCategoriaList(
                        listTransacciones = (uiState as RegistroTransaccionesUiState.Success).listGastosPorCat,
                        registroTransaccionesViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun GastosPorCategoriaList(
    listTransacciones: List<GastosPorCategoriaModel>,
    registroTransaccionesViewModel: RegistroTransaccionesViewModel
) {
    // Invertir el orden de la lista asi lo  que se grega va quedando arriba
    val transaccionesRevertidas = listTransacciones.reversed()
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth().padding(top = 65.dp),
        state = listState
    ) {
        items(transaccionesRevertidas, key = { it.id }) { transaccion ->
            ItemCategory(transaccion, registroTransaccionesViewModel)
        }
    }
    LaunchedEffect(listTransacciones.size) {
        // muestra el ultimo elemento agregado en la parte superior
        listState.scrollToItem(index = 0)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar() {
    TopAppBar(
        title = {
            Text(
                text = "Registro Transacciones",
                color = colorResource(id = R.color.black)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}





