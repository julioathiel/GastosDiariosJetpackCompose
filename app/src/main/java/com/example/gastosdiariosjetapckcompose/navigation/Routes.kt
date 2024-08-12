package com.example.gastosdiariosjetapckcompose.navigation

import com.example.gastosdiariosjetapckcompose.data.core.Constants.ACERCA_DE_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.ACTUALIZAR_MAXIMO_FECHA_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.AJUSTES_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.CATEGORIA_GASTOS_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.CATEGORIA_INGRESOS_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.CONFIGURATION_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.CONGRATULATIONS_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.FORGOT_PASSWORD_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.HOME_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.INITIAL_LOGIN_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.LOADING_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.LOGIN_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.MOVIMIENTOS_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.RECORDATORIOS_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.REGISTER_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.REGISTER_TRANSACTION_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.USER_PROFILE_SCREEN
import com.example.gastosdiariosjetapckcompose.data.core.Constants.VIEWPAGER_SCREEN

//se usara para manejar las Id de las rutas desde este lugar
sealed class Routes(val route: String) {
    object InitialLoginScreen:Routes(INITIAL_LOGIN_SCREEN)
    object LoginScreen : Routes(LOGIN_SCREEN)
    object RegisterScreen : Routes(REGISTER_SCREEN)
    object HomeScreen : Routes(HOME_SCREEN)
    object ViewPagerScreen : Routes(VIEWPAGER_SCREEN)
    object ForgotPasswordScreen : Routes(FORGOT_PASSWORD_SCREEN)
    object MovimientosScreen : Routes(MOVIMIENTOS_SCREEN)
    object ConfigurationScreen : Routes(CONFIGURATION_SCREEN)
    object RegistroTransaccionesScreen : Routes(REGISTER_TRANSACTION_SCREEN)
    object RecordatorioScreen : Routes(RECORDATORIOS_SCREEN)
    object CategoriaGastosScreen : Routes(CATEGORIA_GASTOS_SCREEN)
    object CategoriaIngresosScreen : Routes(CATEGORIA_INGRESOS_SCREEN)
    object ActualizarMaximoFechaScreen : Routes(ACTUALIZAR_MAXIMO_FECHA_SCREEN)
    object LoadingScreen : Routes(LOADING_SCREEN)
    object AcercaDe : Routes(ACERCA_DE_SCREEN)
    object AjustesScreen : Routes(AJUSTES_SCREEN)
    object CongratulationsScreen : Routes(CONGRATULATIONS_SCREEN)
    object UserProfileScreen:Routes(USER_PROFILE_SCREEN)
}