package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home


import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home.AddTransactionDialog
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home.BodyHeader
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home.CardBotonRegistro
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home.CountDate
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home.Header
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home.MyFAB
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home.NuevoMes
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import kotlin.system.exitProcess

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    BackHandler {
        exitProcess(0)
    }

    val showDialogTransaction: Boolean by viewModel.showDialogTransaction.observeAsState(initial = false)
    val isShowSnackbar = remember { SnackbarHostState() }
    val seleccionadoTabIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) { viewModel.initMostrandoAlUsuario() }

    Scaffold(
        bottomBar = {
            sharedLogic.TabView(navController = navController, seleccionadoTabIndex)
        },
        floatingActionButton = { MyFAB(viewModel, isShowSnackbar) },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {
            SnackbarHost(hostState = isShowSnackbar)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        ) {
            Header(usuario = "", viewModel = viewModel)
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            BodyHeader(onNavigationMovimientos = {navController.navigate(route = Routes.MovimientosScreen.route)}, viewModel)
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            CountDate(Modifier.fillMaxWidth(), viewModel)
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            NuevoMes(viewModel = viewModel)
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            CardBotonRegistro(viewModel)
            AddTransactionDialog(
                modifier = Modifier.fillMaxWidth(),
                showDialogTransaccion = showDialogTransaction,
                onDissmis = { viewModel.onDialogClose() },
                viewModel, navController
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
    val progressBarColor = if (animatedProgress >= 0.8f) {
        MaterialTheme.colorScheme.error // Cambia el color a rojo si el progreso es del 80% o más
    } else if (animatedProgress >= 0.7f) {
        MaterialTheme.colorScheme.onSurface //color naranja
    } else {
        MaterialTheme.colorScheme.surface // Mantiene el color por defecto en otro caso
    }


    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier
    )
}