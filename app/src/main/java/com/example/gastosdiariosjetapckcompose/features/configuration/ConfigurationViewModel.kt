package com.example.gastosdiariosjetapckcompose.features.configuration

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.data_base_manager.DataBaseManager
import com.example.gastosdiariosjetapckcompose.domain.model.OpcionEliminarModel
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.CheckDatabaseGastosPorCategoriaEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.CheckDatabaseFechaGuardadaEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.CheckDatabaseMoneyEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.CheckDatabaseAllExpensesEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.CheckDatabaseTotalGastosEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.CheckDatabaseTotalIngresosEmptyUseCase
import com.example.gastosdiariosjetapckcompose.data.core.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,

    private val checkDatabaseMoneyEmptyUseCase: CheckDatabaseMoneyEmptyUseCase,
    private val checkDatabaseTotalGastosEmptyUseCase: CheckDatabaseTotalGastosEmptyUseCase,
    private val checkDatabaseTotalIngresosEmptyUseCase: CheckDatabaseTotalIngresosEmptyUseCase,
    private val checkDatabaseFechaGuardadaEmptyUseCase: CheckDatabaseFechaGuardadaEmptyUseCase,
    private val checkDatabaseAllExpensesEmptyUseCase: CheckDatabaseAllExpensesEmptyUseCase,
    private val checkDatabaseGastosPorCategoriaEmptyUseCase: CheckDatabaseGastosPorCategoriaEmptyUseCase,

    private val dataBaseManager: DataBaseManager
) : ViewModel() {
    // LiveData para observar cuando la aplicación ha sido reseteada con éxito
    private val _appResetExitoso = MutableLiveData(false)
    val appResetExitoso: LiveData<Boolean> = _appResetExitoso

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog

    private val _showShareApp = mutableStateOf(false)
    val showShareApp: State<Boolean> = _showShareApp

    private val _resetComplete = mutableStateOf(false)
    val resetComplete: State<Boolean> = _resetComplete

    fun setBooleanPagerFalse() {
        viewModelScope.launch {
            dataStorePreferences.setViewPagerShow(false)
        }
    }

    fun setShowShare(value: Boolean) {
        _showShareApp.value = value
    }

    fun setResetComplete(item: Boolean) {
        _resetComplete.value = item
        dismissDialog()
    }

    fun setShowResetDialog(value: Boolean) {
        _showDialog.value = value
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun clearDatabase() {
        deleteData()
    }

    // Método para reiniciar la aplicación
    private fun deleteData() {
        viewModelScope.launch {
            try {
                // Restablecer la opción seleccionada en el DataStore
                dataStorePreferences.setSelectedOption("31", true)
                dataStorePreferences.setHoraMinuto(21,0)

                // Eliminar la lista de transacciones
                dataBaseManager.deleteAllApp()

                // Notificar a los observadores que la aplicación se reinició
                _appResetExitoso.value = true
            } catch (e: Exception) {
                Log.e("ErrorReinicioApp", "Error al reiniciar la aplicación: ${e.message}")
                // Notificar a los observadores si hay un error en el reinicio
                _appResetExitoso.value = false
            }
        }
    }


    fun checkDatabaseEmpty() {
        viewModelScope.launch {
            checkDatabaseMoneyEmptyUseCase()
            checkDatabaseFechaGuardadaEmptyUseCase()
            checkDatabaseTotalIngresosEmptyUseCase()
            checkDatabaseTotalGastosEmptyUseCase()
            checkDatabaseAllExpensesEmptyUseCase()
            checkDatabaseGastosPorCategoriaEmptyUseCase()

            _resetComplete.value = true
        }
    }

    private fun deleteGraphBar() {
        viewModelScope.launch {
            dataBaseManager.deleteAllGraphBar()
        }
    }

    private fun deleteUserCreaCatIngresos(){
        viewModelScope.launch {
            dataBaseManager.deleteAllUserCreaCatIngresos()
        }
    }
    private fun deleteUserCreaCatGastos(){
        viewModelScope.launch {
            dataBaseManager.deleteAllUserCreaCatGastos()
        }
    }

    // Opciones de eliminación adicionales que son opcionales para el usuario
    val opcionesEliminar = listOf(
        OpcionEliminarModel("Eliminar también los datos estadísticos del gráfico", ::deleteGraphBar),
        OpcionEliminarModel("Eliminar también categorías de ingresos creadas",::deleteUserCreaCatIngresos),
        OpcionEliminarModel("Eliminar también categorías de gastos creadas",::deleteUserCreaCatGastos),
    )

}


