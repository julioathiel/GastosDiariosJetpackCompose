@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gastosdiariosjetapckcompose.features.home


import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.features.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.features.home.components_home.AddTransactionDialog
import com.example.gastosdiariosjetapckcompose.features.home.components_home.BodyHeader
import com.example.gastosdiariosjetapckcompose.features.home.components_home.CardBotonRegistro
import com.example.gastosdiariosjetapckcompose.features.home.components_home.CountDate
import com.example.gastosdiariosjetapckcompose.features.home.components_home.Header
import com.example.gastosdiariosjetapckcompose.features.home.components_home.MyFAB
import com.example.gastosdiariosjetapckcompose.features.home.components_home.NuevoMes
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel
import kotlin.system.exitProcess

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    registroTransaccionesViewModel: RegistroTransaccionesViewModel,
    configuracion: ConfigurationViewModel
) {
    BackHandler {
        exitProcess(0)
    }

    val showDialogTransaction: Boolean by homeViewModel.showDialogTransaction.observeAsState(initial = false)
    val isShowSnackbar = remember { SnackbarHostState() }
    // Observa el LiveData y redibuja la pantalla cuando cambia
    val isReinicioExitoso by configuracion.appResetExitoso.observeAsState()
    val itemActualizado by sharedLogic.itemActualizado.observeAsState()
    // Establecer el estado de la pestaña seleccionada como 0 para la pestaña "Home"
    val seleccionadoTabIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        // Observa el reinicio de la aplicación y actualiza los valores relevantes
        if (isReinicioExitoso == true) {
            homeViewModel.initMostrandoAlUsuario()
        } else if (itemActualizado == true) {
            // Observa el cambio de valor de un item  y actualiza los valores relevantes
            homeViewModel.initMostrandoAlUsuario()
        }
    }

    Scaffold(
        bottomBar = {
            sharedLogic.TabView(navController = navController, seleccionadoTabIndex)
        },
        floatingActionButton = { MyFAB(homeViewModel, isShowSnackbar) },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = isShowSnackbar)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        ) {
            Header(usuario = "", homeViewModel = homeViewModel)
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            BodyHeader(navController, homeViewModel)
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            CountDate(Modifier.fillMaxWidth(), homeViewModel)
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            NuevoMes(viewModel = homeViewModel)
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            CardBotonRegistro(homeViewModel)

            AddTransactionDialog(
                showDialogTransaccion = showDialogTransaction,
                onDissmis = { homeViewModel.onDialogClose() },
                homeViewModel,
                registroTransaccionesViewModel,
                navController
            )
        }
    }
}
suspend fun showSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    snackbarHostState.showSnackbar(
        message = message,
        actionLabel = null,
        duration = SnackbarDuration.Short
    )
}
@Composable
fun AnimatedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier,
    )
}