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
import com.example.gastosdiariosjetapckcompose.commons.CommonsLoadingScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.acerca_de.AcercaDeScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.acerca_de.AcercaDeViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ConfigurationScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.actualizarMaximoFecha.ActualizarMaximoFechaScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.actualizarMaximoFecha.ActualizarMaximoFechaViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ajustes_avanzados.AjustesScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ajustes_avanzados.AjustesViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.components.CongratulationsScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaGastos.CategoriaGastosScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaGastos.CategoriaGastosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaIngresos.CategoriaIngresosScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaIngresos.CategoriaIngresosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.HomeScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.movimientos.MovimientosScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.notificacion.NotificationViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.notificacion.RecordatorioScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.RegistroTransaccionesScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.viewPagerScreen.ViewPagerScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.viewPagerScreen.ViewPagerViewModel

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
    acercaDeViewModel: AcercaDeViewModel,
    ajustesViewModel: AjustesViewModel
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
                homeViewModel
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
            CommonsLoadingScreen(navController = navController)
        }
        composable(Routes.AcercaDe.route) {
            AcercaDeScreen(
                navController,
                acercaDeViewModel
            )
        }
        composable(Routes.AjustesScreen.route) {
            AjustesScreen(ajustesViewModel, navController)
        }
        composable((Routes.CongratulationsScreen.route)) {
            CongratulationsScreen(navController = navController, viewModel = configurationViewModel)
        }
    }
}