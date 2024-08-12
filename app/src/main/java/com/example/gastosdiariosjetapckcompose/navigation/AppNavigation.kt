package com.example.gastosdiariosjetapckcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.user_profile.UserProfileScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.user_profile.UserProfileViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaGastos.CategoriaGastosScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaGastos.CategoriaGastosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaIngresos.CategoriaIngresosScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaIngresos.CategoriaIngresosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.HomeScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.InitialLoginScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.InitialLoginViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.login.LoginScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.login.LoginViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.register.RegisterScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.register.RegisterViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.movimientos.MovimientosScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.notificacion.NotificationViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.notificacion.RecordatorioScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.RegistroTransaccionesScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.viewPagerScreen.ViewPagerScreen
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.viewPagerScreen.ViewPagerViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(
    auth: FirebaseAuth,
    navHostController: NavHostController,
    initialLoginViewModel: InitialLoginViewModel,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
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
    ajustesViewModel: AjustesViewModel,
    userProfileViewModel: UserProfileViewModel
) {
    val loadingDestino = if (auth.currentUser?.email != null) {
        Routes.HomeScreen.route
    } else {
        Routes.InitialLoginScreen.route
    }

    NavHost(
        navController = navHostController, startDestination = loadingDestino
    ) {
        composable(Routes.InitialLoginScreen.route) {
            InitialLoginScreen(
                navigateToRegister = { navHostController.navigate(Routes.RegisterScreen.route) },
                navigateToLogin = { navHostController.navigate(Routes.LoginScreen.route) },
                navigateToHomeScreen = { navHostController.navigate(Routes.HomeScreen.route) },
                viewModel = initialLoginViewModel
            )
        }
        composable(Routes.RegisterScreen.route) {
            RegisterScreen(viewModel = registerViewModel, navHostController)
        }
        composable(Routes.LoginScreen.route) {
            LoginScreen(viewModel = loginViewModel,
                navigatoToHome = { navHostController.navigate(Routes.HomeScreen.route) })
        }

        composable(Routes.ViewPagerScreen.route) {
            ViewPagerScreen(
                navController = navHostController,
                viewModel = viewPagerViewModel
            )
        }

        composable(Routes.HomeScreen.route) {
            HomeScreen(
                navController = navHostController,
                homeViewModel
            )
        }

        composable(Routes.RegistroTransaccionesScreen.route) {
            RegistroTransaccionesScreen(
                registroTransaccionesViewModel,
                navController = navHostController
            )
        }
        composable(Routes.ConfigurationScreen.route) {
            ConfigurationScreen(
                navController = navHostController,
                configurationViewModel
            )

        }
        composable(Routes.RecordatorioScreen.route) {
            RecordatorioScreen(
                navController = navHostController,
                notificationViewModel
            )
        }
        composable(Routes.ActualizarMaximoFechaScreen.route) {
            ActualizarMaximoFechaScreen(
                NavController = navHostController,
                actualizarMaximoFechaViewModel
            )
        }
        composable(Routes.CategoriaGastosScreen.route) {
            CategoriaGastosScreen(
                navController = navHostController, categoriaGastosViewModel
            )
        }
        composable(Routes.CategoriaIngresosScreen.route) {
            CategoriaIngresosScreen(
                navController = navHostController,
                categoriaIngresosViewModel = categoriaIngresosViewModel
            )
        }
        composable(Routes.MovimientosScreen.route) {
            MovimientosScreen(
                navController = navHostController,
                movimientosViewModel = movimientosViewModel
            )
        }
        composable(Routes.RegistroTransaccionesScreen.route) {
            RegistroTransaccionesScreen(
                registroTransaccionesViewModel,
                navController = navHostController
            )
        }
        composable(Routes.ConfigurationScreen.route) {
            ConfigurationScreen(
                navController = navHostController,
                configurationViewModel
            )
        }

        composable(Routes.UserProfileScreen.route) {
            UserProfileScreen(userProfileViewModel, onLogout = {
                navHostController.navigate(Routes.InitialLoginScreen.route)
            })
        }

        composable(Routes.LoadingScreen.route) {
            CommonsLoadingScreen(navController = navHostController)
        }
        composable(Routes.AcercaDe.route) {
            AcercaDeScreen(
                navHostController,
                acercaDeViewModel
            )
        }
        composable(Routes.AjustesScreen.route) {
            AjustesScreen(ajustesViewModel, navHostController)
        }
        composable((Routes.CongratulationsScreen.route)) {
            CongratulationsScreen(
                navController = navHostController,
                viewModel = configurationViewModel
            )
        }
    }
}