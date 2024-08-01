package com.example.gastosdiariosjetapckcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.acerca_de.AcercaDeViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.actualizarMaximoFecha.ActualizarMaximoFechaViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ajustes_avanzados.AjustesViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaGastos.CategoriaGastosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaIngresos.CategoriaIngresosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.notificacion.NotificationViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.viewPagerScreen.ViewPagerViewModel
import com.example.gastosdiariosjetapckcompose.navigation.AppNavigation
import com.example.gastosdiariosjetapckcompose.ui.theme.GastosDiariosJetapckComposeTheme
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.FLEXIBLE

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
    private val ajustesViewModel: AjustesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForAppUpdates()

        setContent {
            // Configurar la alarma por defecto a las 21:00 horas
            notificationViewModel.notificationProgrammed(this@MainActivity)
            val isDarkModeActive = ajustesViewModel.state.isDarkModeActive

            GastosDiariosJetapckComposeTheme(darkTheme = isDarkModeActive) {
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
                        notificationViewModel, acercaDeViewModel, ajustesViewModel
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(updateType == AppUpdateType.IMMEDIATE){
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(info,updateType,this,123)
                }
            }
        }

    }

    private fun checkForAppUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(info, updateType, this, 123)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 123) {
            if (resultCode != RESULT_OK) {
                println("Algo salió mal al actualizar...")
            }
        }
    }
}
