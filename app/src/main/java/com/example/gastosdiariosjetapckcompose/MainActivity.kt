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
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.features.home.HomeScreen
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosScreen
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.features.task.TaskScreen
import com.example.gastosdiariosjetapckcompose.features.task.TaskViewModel
import com.example.gastosdiariosjetapckcompose.features.login.LoginScreen
import com.example.gastosdiariosjetapckcompose.features.login.LoginViewModel
import com.example.gastosdiariosjetapckcompose.features.forgotpassword.ForgotPasswordScreen
import com.example.gastosdiariosjetapckcompose.features.forgotpassword.ForgotPasswordViewModel
import com.example.gastosdiariosjetapckcompose.features.register.RegisterScreen
import com.example.gastosdiariosjetapckcompose.features.register.RegisterViewModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import com.example.gastosdiariosjetapckcompose.ui.theme.GastosDiariosJetapckComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel:HomeViewModel by viewModels()
    private val tasksViewModel: TaskViewModel by viewModels()
    private val movimientosViewModel:MovimientosViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.statusBarColor = getColor(R.color.black)
            window.navigationBarColor = getColor(R.color.black)
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
                                movimientosViewModel = movimientosViewModel
                            )
                        }
                        composable(Routes.MovimientosScreen.route) {
                          MovimientosScreen(
                              NavController = navController,
                              movimientosViewModel = movimientosViewModel,
                              homeViewModel = homeViewModel
                          )
                        }
                        composable(Routes.TaskScreen.route) {
                            TaskScreen(
                                taskViewModel = tasksViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}