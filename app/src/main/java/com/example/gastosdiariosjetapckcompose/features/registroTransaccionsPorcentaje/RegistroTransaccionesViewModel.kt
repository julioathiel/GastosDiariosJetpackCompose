package com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.CircularBuffer
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.SharedLogic
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.BarGraphConfig
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.data
import com.example.gastosdiariosjetapckcompose.domain.model.BarDataModel
import com.example.gastosdiariosjetapckcompose.domain.uiState.RegistroTransaccionesUiState
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.GetGastosPorCategoriaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.DeleteBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.GetAllBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.InsertBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.UpdateBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.GetTotalIngresosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RegistroTransaccionesViewModel @Inject constructor(
    private val getGastosPorCategoriaUseCase: GetGastosPorCategoriaUseCase,
    private val getTotalIngresosUseCase: GetTotalIngresosUseCase,

    insertBarGraphUseCase: InsertBarGraphUseCase,
    updateBarGraphUseCase: UpdateBarGraphUseCase,
    getAllBarGraphUseCase: GetAllBarGraphUseCase,
    deleteBarGraphUseCase: DeleteBarGraphUseCase
) : ViewModel() {

    private val _porcentajeGasto = MutableStateFlow<Int>(0)
    val porcentajeGasto: Flow<Int> = _porcentajeGasto

    private var _mostrandoDineroTotalIngresosRegistros = MutableLiveData<Double>()
    val mostrandoDineroTotalIngresosRegistros: LiveData<Double> =
        _mostrandoDineroTotalIngresosRegistros


    private val _listBarDataModel = MutableStateFlow<List<BarDataModel>>(emptyList())
    val listBarDataModel: StateFlow<List<BarDataModel>> get() = _listBarDataModel

    private val circularBuffer = CircularBuffer(
        capacity = 12,
        insertBarGraphUseCase = insertBarGraphUseCase,
        updateBarGraphUseCase = updateBarGraphUseCase,
        getAllBarGraphUseCase = getAllBarGraphUseCase,
        deleteBarGraphUseCase = deleteBarGraphUseCase
    )

    init {
        ingresosTotalesMaximoProgress()
        updateBarGraphList()
    }


    val uiState: StateFlow<RegistroTransaccionesUiState> =
        getGastosPorCategoriaUseCase().map(RegistroTransaccionesUiState::Success)
            .catch { RegistroTransaccionesUiState.Error(it) }
            // .distinctUntilChanged() // Esto asegura que solo se emitan valores diferentes de los que ya han sido emitidos
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                RegistroTransaccionesUiState.Loading
            )


    private fun ingresosTotalesMaximoProgress() {
        // Manejando LiveData en el hilo principal
        viewModelScope.launch(Dispatchers.Main) {
            try {
                // Muestra el total de ingresos
                val data = withContext(Dispatchers.IO){
                    getTotalIngresosUseCase()?.totalIngresos ?: 0.0
                }

                _mostrandoDineroTotalIngresosRegistros.value = data

                calcularValorMaximo(data)
            } catch (e: Exception) {
                Log.e("ErrorIngresosTotales", "Error al obtener el total de ingresos: ${e.message}")
            }
        }
    }

    // Función para calcular el valor máximo de la lista de transacciones
    private fun calcularValorMaximo(totalIngresos: Double) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO){
                    getGastosPorCategoriaUseCase().firstOrNull()
                }
                val maximoGastos =
                    data?.maxByOrNull { it.totalGastado.toDouble() }?.totalGastado?.toDouble()
                        ?: 0.0
                Log.d("insertGraph", "maximoGastos = $maximoGastos")
                val porcentajeMes = calcularPorcentaje(maximoGastos, totalIngresos) ?: 0
                _porcentajeGasto.value = porcentajeMes // Actualiza el LiveData con el porcentaje calculado
                Log.d("insertGraph", "porcentajeMes = $porcentajeMes")
                insertGraph(maximoGastos, porcentajeMes)
            } catch (e: Exception) {
                Log.e("ExceptionCalcularMaximo", "Ocurrió una excepción: ${e.message}")
                Log.e("ExceptionCalcularMaximo", Log.getStackTraceString(e))
                // Si hay una excepción, inserta valores predeterminados
                insertGraph(0.0, 0)
            }
        }
    }

    // Función para insertar datos en la base de datos a través de CircularBuffer
    private fun insertGraph(maximoGastos: Double, porcentajeMes: Int) {
        val calendar = Calendar.getInstance()
        val mesActual = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())

        try {
            circularBuffer.addBuffer(
                listOf(
                    BarDataModel(
                        value = porcentajeMes.toFloat(),
                        month = mesActual!!,
                        money = maximoGastos.toString()
                    )
                )
            )
            // Actualizar la lista de datos de gráficos después de la inserción
            updateBarGraphList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Función para obtener y actualizar la lista de datos de gráficos desde CircularBuffer
    private fun updateBarGraphList() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO){
                    circularBuffer.getBufferList()
                }
                _listBarDataModel.value = data
            } catch (e: Exception) {
                Log.e(
                    "ErrorUpdateBarGraphList",
                    "Error al actualizar la lista de datos del gráfico: ${e.message}"
                )
            }
        }
    }

    private fun calcularPorcentaje(maximoGastado: Double, totalIngresos: Double): Int {
        if (totalIngresos > 0.0) {
            val porcentaje = (maximoGastado / totalIngresos * 100)
            val bd = BigDecimal(porcentaje)
            val porcentajeRedondeado = bd.setScale(0, RoundingMode.HALF_EVEN).toInt()
            return porcentajeRedondeado.coerceIn(0, 100)
        }
        return 0
    }

}

