package com.example.gastosdiariosjetapckcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gastosdiariosjetapckcompose.features.acerca_de.AcercaDeViewModel
import com.example.gastosdiariosjetapckcompose.features.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.features.configuration.actualizarMaximoFecha.ActualizarMaximoFechaViewModel
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaGastos.CategoriaGastosViewModel
import com.example.gastosdiariosjetapckcompose.features.creandoCategoriaIngresos.CategoriaIngresosViewModel
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.features.recordatorio.NotificationViewModel
import com.example.gastosdiariosjetapckcompose.features.recordatorio.notificacionProgramada
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel
import com.example.gastosdiariosjetapckcompose.features.viewPagerScreen.ViewPagerViewModel
import com.example.gastosdiariosjetapckcompose.navigation.AppNavigation
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
    private val viewPagerViewModel: ViewPagerViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()
    private val acercaDeViewModel: AcercaDeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Configurar la alarma por defecto a las 21:00 horas
            notificacionProgramada(this@MainActivity)

            GastosDiariosJetapckComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    AppNavigation(
                        homeViewModel, movimientosViewModel,
                        configurationViewModel, registroTransaccionesViewModel,
                        categoriaGastosViewModel, categoriaIngresosViewModel,
                        actualizarMaximoFechaViewModel, viewPagerViewModel,
                        notificationViewModel,acercaDeViewModel
                    )
                }
            }
        }
    }
}
