package com.example.gastosdiariosjetapckcompose.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gastosdiariosjetapckcompose.features.acerca_de.AcercaDeScreen
import com.example.gastosdiariosjetapckcompose.features.acerca_de.AcercaDeViewModel
import com.example.gastosdiariosjetapckcompose.features.configuration.ConfigurationScreen
import com.example.gastosdiariosjetapckcompose.features.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.features.configuration.actualizarMaximoFecha.ActualizarMaximoFechaScreen
import com.example.gastosdiariosjetapckcompose.features.configuration.actualizarMaximoFecha.ActualizarMaximoFechaViewModel
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaGastos.CategoriaGastosScreen
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaGastos.CategoriaGastosViewModel
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaIngresos.CategoriaIngresosScreen
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaIngresos.CategoriaIngresosViewModel
import com.example.gastosdiariosjetapckcompose.features.home.HomeScreen
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosScreen
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.features.recordatorio.NotificationViewModel
import com.example.gastosdiariosjetapckcompose.features.recordatorio.RecordatorioScreen
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesScreen
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel
import com.example.gastosdiariosjetapckcompose.features.viewPagerScreen.ViewPagerScreen
import com.example.gastosdiariosjetapckcompose.features.viewPagerScreen.ViewPagerViewModel

@Composable
fun AppNavigation(
    homeViewModel: HomeViewModel,
    movimientosViewModel: MovimientosViewModel,
    configurationViewModel: ConfigurationViewModel,
    registroTransaccionesViewModel: RegistroTransaccionesViewModel,
    categoriaGastosViewModel: CategoriaGastosViewModel,
    categoriaIngresosViewModel: CategoriaIngresosViewModel,
    actualizarMaximoFechaViewModel: ActualizarMaximoFechaViewModel,
    viewPagerViewModel: ViewPagerViewModel,
    notificationViewModel: NotificationViewModel,
    acercaDeViewModel: AcercaDeViewModel
) {
    val navController = rememberNavController()

    val showViewPager by viewPagerViewModel.showViewPager().collectAsState(initial = null)
    var loadingDestino = ""
    loadingDestino = if (showViewPager == null) {
        Routes.LoadingScreen.route
    } else {
        if (showViewPager == true) {
            Routes.HomeScreen.route
        } else {
            Routes.ViewPagerScreen.route
        }
    }

    NavHost(
        navController = navController, startDestination = loadingDestino
    ) {
        composable(Routes.ViewPagerScreen.route) {
            ViewPagerScreen(
                navController = navController,
                viewModel = viewPagerViewModel
            )
        }

        composable(Routes.HomeScreen.route) {
            HomeScreen(
                navController = navController,
                homeViewModel,
                configurationViewModel
            )
        }

        composable(Routes.RegistroTransaccionesScreen.route) {
            RegistroTransaccionesScreen(
                registroTransaccionesViewModel,
                navController = navController
            )
        }
        composable(Routes.ConfigurationScreen.route) {
            ConfigurationScreen(
                navController = navController,
                configurationViewModel
            )

        }
        composable(Routes.RecordatorioScreen.route) {
            RecordatorioScreen(
                navController = navController,
                notificationViewModel
            )
        }
        composable(Routes.ActualizarMaximoFechaScreen.route) {
            ActualizarMaximoFechaScreen(
                NavController = navController,
                actualizarMaximoFechaViewModel
            )
        }
        composable(Routes.CategoriaGastosScreen.route) {
            CategoriaGastosScreen(
                navController = navController, categoriaGastosViewModel
            )
        }
        composable(Routes.CategoriaIngresosScreen.route) {
            CategoriaIngresosScreen(
                navController = navController,
                categoriaIngresosViewModel = categoriaIngresosViewModel
            )
        }
        composable(Routes.MovimientosScreen.route) {
            MovimientosScreen(
                navController = navController,
                movimientosViewModel = movimientosViewModel
            )
        }
        composable(Routes.RegistroTransaccionesScreen.route) {
            RegistroTransaccionesScreen(
                registroTransaccionesViewModel,
                navController = navController
            )
        }
        composable(Routes.ConfigurationScreen.route) {
            ConfigurationScreen(
                navController = navController,
                configurationViewModel
            )
        }

        composable(Routes.LoadingScreen.route) {
            LoadingScreen(navController = navController)
        }
        composable(Routes.AcercaDe.route){
           AcercaDeScreen(
                navController,
                acercaDeViewModel
            )
        }
    }
}

@Composable
fun LoadingScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            Modifier
                .size(30.dp)
                .align(Alignment.Center)
        )
    }
}