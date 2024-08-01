package com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.GastosPorCategoriaModel
import com.example.gastosdiariosjetapckcompose.domain.uiState.RegistroTransaccionesUiState
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.components_stadistics.BarGrahpConfigCustom
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.components_stadistics.CategoriaConMasGastos
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.components_stadistics.ItemCategory

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegistroTransaccionesScreen(
    registerTransactionsViewModel: RegistroTransaccionesViewModel,
    navController: NavController
) {
    // Establecer el estado de la pestaña seleccionada como el índice correspondiente a la pestaña "Stadist"
    val selectedTabIndex by rememberSaveable { mutableIntStateOf(1) }
    Scaffold(
        topBar = { Toolbar() },
        bottomBar = {
            sharedLogic.TabView(
                navController = navController,
                seleccionadoTabIndex = selectedTabIndex
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            Content(
                registerTransactionsViewModel,
                Modifier.padding(paddingValues)
            )
        }
    )
}

@Composable
fun Content(vm: RegistroTransaccionesViewModel, modifier: Modifier) {
    //creando el ciclo de vida de la pantalla
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<RegistroTransaccionesUiState>(
        initialValue = RegistroTransaccionesUiState.Loading,
        key1 = lifecycle,
        key2 = vm
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
           vm.uiState.collect { value = it }
        }
    }

    LaunchedEffect(Unit) { vm.getDatosGastos() }

    when (uiState) {
        is RegistroTransaccionesUiState.Error -> {}
        RegistroTransaccionesUiState.Loading -> {
            sharedLogic.LoaderCircularProgressPantalla()
        }

        is RegistroTransaccionesUiState.Success -> {
            // Obtener la lista de transacciones desde el ViewModel
            val listGastosPorCat = (uiState as RegistroTransaccionesUiState.Success).listGastosPorCat
            Column(modifier = modifier.fillMaxSize()) {
                GastosPorCategoriaList(
                    listTransacciones = listGastosPorCat, vm,
                    uiState = uiState as RegistroTransaccionesUiState.Success
                )
            }
        }
    }
}


@Composable
fun GastosPorCategoriaList(
    listTransacciones: List<GastosPorCategoriaModel>,
    registroTransaccionesViewModel: RegistroTransaccionesViewModel,
    uiState: RegistroTransaccionesUiState.Success
) {
    // Invertir el orden de la lista asi lo  que se grega va quedando arriba
    val transaccionesRevertidas = listTransacciones.reversed()
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        state = listState
    ) {
        item {
//            // Mostrar el gráfico aquí siempre, incluso si no hay datos
            BarGrahpConfigCustom(registroTransaccionesViewModel)
        }
        item {
            Spacer(modifier = Modifier.padding(16.dp))
            // Mostrar la categoría con más gastos si hay datos disponibles
            if (uiState.listGastosPorCat.isNotEmpty()) {
                CategoriaConMasGastos(
                    listTransacciones = uiState.listGastosPorCat,
                    registroTransaccionesViewModel
                )
            } else {
                // Mostrar "Sin categoría" y total gastado "0.0" cuando no hay datos
                val sinCategoria = GastosPorCategoriaModel(
                    id = 0,
                    title = "Sin categoría",
                    categoriaIcon = "",
                    totalGastado = "0.0"
                )
                CategoriaConMasGastos(
                    listTransacciones = listOf(sinCategoria),
                    registroTransaccionesViewModel
                )
            }
        }
        // Mostrar mensaje o indicador visual cuando la lista está vacía
        if (transaccionesRevertidas.isEmpty()) {
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)){

                    Text(
                        text = stringResource(R.string.no_hay_transacciones_disponibles),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        } else {
            items(transaccionesRevertidas, key = { it.id }) { transaction ->
                ItemCategory(transaction, registroTransaccionesViewModel)
            }
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
                text = stringResource(id = R.string.toolbar_analisis_de_gastos)
            )
        }
    )
}





