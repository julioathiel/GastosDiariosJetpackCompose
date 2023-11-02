package com.example.gastosdiariosjetapckcompose.navigation
//se usara para manejar las Id de las rutas desde este lugar
sealed class Routes(val route:String) {
    //se crea la clase object  y se le pone un nombre que
    // extendera de la misma clase Routes y le ponemos la ruta o ID para poder
    // navegar entre pantallas
    object LoginScreen : Routes("LoginScreen")
    object HomeScreen : Routes("HomeScreen")
    object RegisterScreen : Routes("RegisterScreen")
    object ForgotPasswordScreen : Routes("ForgotPasswordScreen")
    object MovimientosScreen : Routes("MovimientosScreen")
    object TaskScreen : Routes("TaskScreen")
}