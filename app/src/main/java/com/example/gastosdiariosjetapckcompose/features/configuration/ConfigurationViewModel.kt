package com.example.gastosdiariosjetapckcompose.features.configuration

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.data.di.module.DataBaseCleaner
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import com.example.gastosdiariosjetapckcompose.domain.model.GastosPorCategoriaModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.model.TotalGastosModel
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.CheckDatabaseGastosPorCategoriaEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.DeleteGastosPorCategoriaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.GetGastosPorCategoriaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.CheckDatabaseFechaGuardadaEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.DeleteFechaUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.GetFechaUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.CheckDatabaseMoneyEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.DeleteCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.GetCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.CheckDatabaseAllExpensesEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.DeleteMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.GetMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.CheckDatabaseTotalGastosEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.CheckDatabaseTotalIngresosEmptyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.DeleteTotalGastosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.DeleteTotalIngresosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.GetTotalGastosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.GetTotalIngresosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private var databaseCleaner: DataBaseCleaner,

    private var getMovimientosUsecase: GetMovimientosUsecase,
    private var deleteMovimientosUsecase: DeleteMovimientosUsecase,

    private val getFechaUsecase: GetFechaUsecase,
    private val deleteFechaUseCase: DeleteFechaUsecase,

    private val getGastosPorCategoriaUseCase: GetGastosPorCategoriaUseCase,
    private val deleteGastosPorCategoriaUseCase: DeleteGastosPorCategoriaUseCase,

    private val getTotalIngresosUseCase: GetTotalIngresosUseCase,
    private val deleteTotalIngresosUseCase: DeleteTotalIngresosUseCase,

    private val getTotalGastosUseCase: GetTotalGastosUseCase,
    private val deleteTotalGastosUseCase: DeleteTotalGastosUseCase,

    private val getCurrentMoneyUseCase: GetCurrentMoneyUseCase,
    private val deleteCurrentMoneyUseCase: DeleteCurrentMoneyUseCase,

    private val checkDatabaseMoneyEmptyUseCase: CheckDatabaseMoneyEmptyUseCase,
    private val checkDatabaseTotalGastosEmptyUseCase: CheckDatabaseTotalGastosEmptyUseCase,
    private val checkDatabaseTotalIngresosEmptyUseCase: CheckDatabaseTotalIngresosEmptyUseCase,
    private val checkDatabaseFechaGuardadaEmptyUseCase: CheckDatabaseFechaGuardadaEmptyUseCase,
    private val checkDatabaseAllExpensesEmptyUseCase: CheckDatabaseAllExpensesEmptyUseCase,
    private val checkDatabaseGastosPorCategoriaEmptyUseCase: CheckDatabaseGastosPorCategoriaEmptyUseCase
) : ViewModel() {
    // LiveData para observar cuando la aplicación ha sido reseteada con éxito
    private val _appResetExitoso = MutableLiveData(false)
    val appResetExitoso: LiveData<Boolean> = _appResetExitoso

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog


    private val _resetComplete = mutableStateOf(false)
    val resetComplete: State<Boolean> = _resetComplete

    private val _showProgress = mutableStateOf(false)
    val showProgress: State<Boolean> = _showProgress

    private val _showCongratulationsDialog = mutableStateOf(false)
    val showCongratulationsDialog: State<Boolean> = _showCongratulationsDialog

    fun setResetComplete(item: Boolean) {
        _resetComplete.value = item
    }

    fun showResetDialog() {
        _showDialog.value = true
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun clearDatabase() {
        //elimina la base de datos
        //databaseCleaner.clearDataBase()
        deleteData()
    }

    // Método para reiniciar la aplicación
    private fun deleteData() {
        viewModelScope.launch {
            //elimina la lista de transacciones
            deleteDinreoActual()
            deleteTodosLosMovimientos()
            deleteListGastosPorCategoriasUnicas()
            deleteFecha()
            deleteIngresosTotal()
            deletGastosTotal()

            // Notifica a los observadores que la aplicación se reinició
            _appResetExitoso.value = true
            _showProgress.value = true//activa el progreso  cicrcular
        }
    }

    private fun deleteDinreoActual() {
        viewModelScope.launch {
            deleteCurrentMoneyUseCase(CurrentMoneyModel(money = 0.0, isChecked = true))
        }
    }

    private fun deleteListGastosPorCategoriasUnicas() {
        viewModelScope.launch {
            val listCatUnica: Flow<List<GastosPorCategoriaModel>> = getGastosPorCategoriaUseCase()
            val list = listCatUnica.first()
            for (it in list) {
                deleteGastosPorCategoriaUseCase(it)
            }
        }
    }

    private fun deleteTodosLosMovimientos() {
        viewModelScope.launch {
            val transaccionesFlow: Flow<List<MovimientosModel>> = getMovimientosUsecase()
            val transacciones =
                transaccionesFlow.first() // Obtiene la lista de transacciones del primer valor emitido por el flujo
            // Elimina todas las transacciones de la base de datos
            for (transaccion in transacciones) {
                deleteMovimientosUsecase(transaccion)
            }
        }
    }

    private fun deleteFecha() {
        viewModelScope.launch {
            // Obtener el objeto FechaSaveModel actualmente guardado
            val fechaSaveModel = getFechaUsecase()
            // Eliminar el objeto FechaSaveModel llamando a deleteFechaSaveModel()
            if (fechaSaveModel != null) {
                deleteFechaUseCase(fechaSaveModel)
            }
        }
    }

    private fun deleteIngresosTotal() {
        viewModelScope.launch {
            val item = getTotalIngresosUseCase()
            if (item != null) {
                deleteTotalIngresosUseCase(item)
            }
        }
    }

    private fun deletGastosTotal() {
        viewModelScope.launch {
            val item = getTotalGastosUseCase()
            if (item != null) {
                deleteTotalGastosUseCase(item)
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
}


