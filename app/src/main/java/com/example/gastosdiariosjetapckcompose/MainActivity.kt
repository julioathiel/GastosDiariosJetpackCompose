package com.example.gastosdiariosjetapckcompose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.acerca_de.AcercaDeViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.actualizarMaximoFecha.ActualizarMaximoFechaViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ajustes_avanzados.AjustesViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.user_profile.UserProfileViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaGastos.CategoriaGastosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaIngresos.CategoriaIngresosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.InitialLoginViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.login.LoginViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.register.RegisterViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.notificacion.NotificationViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.viewPagerScreen.ViewPagerViewModel
import com.example.gastosdiariosjetapckcompose.navigation.AppNavigation
import com.example.gastosdiariosjetapckcompose.ui.theme.GastosDiariosJetapckComposeTheme
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.FLEXIBLE

    private val initialLoginViewModel: InitialLoginViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
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
    private val userProfileViewModel: UserProfileViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        navController = NavHostController(this)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForAppUpdates()

        setContent {
            val navHostController = rememberNavController()
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
                        auth,
                        navHostController,
                        initialLoginViewModel,
                        loginViewModel, registerViewModel,
                        homeViewModel, movimientosViewModel,
                        configurationViewModel, registroTransaccionesViewModel,
                        categoriaGastosViewModel, categoriaIngresosViewModel,
                        actualizarMaximoFechaViewModel, viewPagerViewModel,
                        notificationViewModel, acercaDeViewModel, ajustesViewModel,userProfileViewModel
                    )
                }
            }
        }
    }

//    override fun onStart() {
//        //ESTE METODO SE LLAMA SIEMPRE DESPUES DEL ONCREATE
//        super.onStart()
//        val currentUser = auth.currentUser
//        if (currentUser?.email != null) {
//            // Si el usuario está logueado, navegar a la pantalla de inicio
//            navHostController.navigate(Routes.HomeScreen.route)
//        }
//    }

    override fun onResume() {
        super.onResume()
        if (updateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(info, updateType, this, 123)
                }
            }.addOnFailureListener { e ->
                Log.e("AppUpdate", "Error al verificar actualización inmediata", e)
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
        }.addOnFailureListener { e ->
            Log.e("AppUpdate", "Error al obtener información de actualización", e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                println("Actualización completada con éxito")
            }

            RESULT_CANCELED -> {
                println("Actualización cancelada por el usuario")
            }

            ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                println("La actualización falló")
            }
        }
    }

    private val listener = object : InstallStateUpdatedListener {
        override fun onStateUpdate(state: InstallState) {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                // La actualización se ha descargado, notifica al usuario
                popupSnackbarForCompleteUpdate()
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                // La actualización se ha instalado, limpia la sesión de actualización
                appUpdateManager.unregisterListener(this)
            } else {
                // Otros estados de instalación
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        // Muestra un Snackbar o un diálogo para notificar al usuario que la actualización está lista
//        Snackbar.make(Routes.HomeScreen.route,
//            "Una nueva actualización está lista para ser instalada",
//            Snackbar.LENGTH_INDEFINITE
//        ).setAction("INSTALAR") {
//            appUpdateManager.completeUpdate()
//        }.show()
    }

}
