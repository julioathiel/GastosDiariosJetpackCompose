package com.example.gastosdiariosjetapckcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gastosdiariosjetapckcompose.features.configuration.ConfigurationScreen
import com.example.gastosdiariosjetapckcompose.features.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.features.configuration.actualizarMaximoFecha.ActualizarMaximoFechaScreen
import com.example.gastosdiariosjetapckcompose.features.configuration.actualizarMaximoFecha.ActualizarMaximoFechaViewModel
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaGastos.CategoriaGastosScreen
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaGastos.CategoriaGastosViewModel
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaIngresos.CategoriaIngresosScreen
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaIngresos.CategoriaIngresosViewModel
import com.example.gastosdiariosjetapckcompose.features.forgotpassword.ForgotPasswordScreen
import com.example.gastosdiariosjetapckcompose.features.forgotpassword.ForgotPasswordViewModel
import com.example.gastosdiariosjetapckcompose.features.home.HomeScreen
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.features.login.LoginScreen
import com.example.gastosdiariosjetapckcompose.features.login.LoginViewModel
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosScreen
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.features.recordatorio.RecordatorioScreen
import com.example.gastosdiariosjetapckcompose.features.recordatorio.notificacionProgramada
import com.example.gastosdiariosjetapckcompose.features.register.RegisterScreen
import com.example.gastosdiariosjetapckcompose.features.register.RegisterViewModel
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesScreen
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import com.example.gastosdiariosjetapckcompose.ui.theme.GastosDiariosJetapckComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val movimientosViewModel: MovimientosViewModel by viewModels()
    private val configurationViewModel: ConfigurationViewModel by viewModels()
    private val registroTransaccionesViewModel: RegistroTransaccionesViewModel by viewModels()
    private val categoriaGastosViewModel: CategoriaGastosViewModel by viewModels()
    private val categoriaIngresosViewModel: CategoriaIngresosViewModel by viewModels()
    private val actualizarMaximoFechaViewModel: ActualizarMaximoFechaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.statusBarColor = getColor(R.color.background)
            window.navigationBarColor = getColor(R.color.background)

            // Configurar la alarma por defecto a las 21:00 horas
            notificacionProgramada(this@MainActivity)

            GastosDiariosJetapckComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HomeScreen.route
                    ) {

                        composable(Routes.LoginScreen.route) {
                            LoginScreen(
                                loginViewModel = LoginViewModel(),
                                navController = navController
                            )
                        }
                        composable(Routes.RegisterScreen.route) {
                            RegisterScreen(
                                registerViewModel = RegisterViewModel(),
                                navController
                            )
                        }
                        composable(Routes.ForgotPasswordScreen.route) {
                            ForgotPasswordScreen(
                                navController = navController,
                                forgotPasswordViewModel = ForgotPasswordViewModel()
                            )
                        }
                        composable(Routes.HomeScreen.route) {
                            //de la pantalla de inicio, al apretar el boton ingresar,
                            // que vaya al homeScreen
                            HomeScreen(
                                homeViewModel = homeViewModel,
                                navController = navController,
                                configuracion = configurationViewModel
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
                                configurationViewModel,
                                navController = navController
                            )
                        }

                        composable(Routes.RecordatorioScreen.route) {
                            RecordatorioScreen(
                                navController = navController
                            )
                        }
                        composable(Routes.CategoriaGastosScreen.route) {
                            CategoriaGastosScreen(
                                navController = navController, categoriaGastosViewModel
                            )
                        }
                        composable(Routes.CategoriaIngresosScreen.route){
                            CategoriaIngresosScreen(
                                navController = navController,
                                categoriaIngresosViewModel = categoriaIngresosViewModel
                            )
                        }
                        composable(Routes.ActualizarMaximoFechaScreen.route) {
                            ActualizarMaximoFechaScreen(
                                NavController = navController,
                                actualizarMaximoFechaViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}