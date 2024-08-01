package com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.CircularBuffer
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val circularBuffer = CircularBuffer(
        capacity = 12,
        insertBarGraphUseCase = insertBarGraphUseCase,
        updateBarGraphUseCase = updateBarGraphUseCase,
        getAllBarGraphUseCase = getAllBarGraphUseCase,
        deleteBarGraphUseCase = deleteBarGraphUseCase
    )

    private val _porcentajeGasto = MutableStateFlow<Int?>(0)
    val porcentajeGasto: StateFlow<Int?> = _porcentajeGasto

    private var _mostrandoDineroTotalIngresosRegistros = MutableStateFlow<Double?>(null)
    val mostrandoDineroTotalIngresosRegistros: StateFlow<Double?> =
        _mostrandoDineroTotalIngresosRegistros


    private val _listBarDataModel = MutableStateFlow<List<BarDataModel>>(emptyList())
    val listBarDataModel: StateFlow<List<BarDataModel>> get() = _listBarDataModel

    val uiState: StateFlow<RegistroTransaccionesUiState> =
        getGastosPorCategoriaUseCase().map(RegistroTransaccionesUiState::Success)
            .catch { RegistroTransaccionesUiState.Error(it) }
            // .distinctUntilChanged() // Esto asegura que solo se emitan valores diferentes de los que ya han sido emitidos
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                RegistroTransaccionesUiState.Loading
            )


    fun getDatosGastos() {
        viewModelScope.launch {
            try {
                val data = getTotalIngresosUseCase()?.totalIngresos ?: 0.0
                //variable que muestra el progress de cada lista de itemCategory
                _mostrandoDineroTotalIngresosRegistros.value = data

                calcularValorMaximo(data)

            } catch (e: Exception) {
                Log.e("ErrorIngresosTotales", "Error al obtener el total de ingresos: ${e.message}")
            }
        }
    }

    // Función para calcular el valor máximo de la lista de transacciones
    private fun calcularValorMaximo(totalIngresos: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = getGastosPorCategoriaUseCase().firstOrNull()

                val maximoGastos = data?.maxByOrNull {
                    it.totalGastado.toDouble()
                }?.totalGastado?.toDouble() ?: 0.0

                val porcentajeMes: Float =
                    sharedLogic.calcularProgresoRelativo(totalIngresos, maximoGastos)

                val porcentaje: String = sharedLogic.formattedPorcentaje(porcentajeMes)
                _porcentajeGasto.value = porcentaje.toInt()

                insertGraph(maximoGastos, porcentaje.toFloat())
            } catch (e: Exception) {
                Log.e("ExceptionCalcularMaximo", "Ocurrió una excepción: ${e.message}")
                insertGraph(0.0, 0f)
            }
        }
    }


    // Función para insertar datos en la base de datos a través de CircularBuffer
    private fun insertGraph(maximoGastos: Double, porcentajeMes: Float) {
        val calendar = Calendar.getInstance()
        val mesActual = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())

        try {
            viewModelScope.launch {
                // Obtener la lista actual de datos de gráficos
                val listaGuardada = getBarGraph()
                // Verificar si es la primera vez que se insertan datos
                if (listaGuardada.isEmpty()) {
                    // Agregar datos automáticamente todos en 0 la primera vez
                    circularBuffer.addBarGraph(
                        BarDataModel(
                            value = 0f,
                            month = mesActual!!,
                            money = "0"
                        )
                    )
                }
                listaGuardada.forEach {
                    if (it.month == mesActual && it.value < porcentajeMes) {
                        circularBuffer.addBarGraph(
                            BarDataModel(
                                value = porcentajeMes,
                                month = mesActual,
                                money = maximoGastos.toString()
                            )
                        )
                    } else if (mesActual != it.month) {
                        circularBuffer.addBarGraph(
                            BarDataModel(
                                value = 0f,
                                month = mesActual!!,
                                money = "0"
                            )
                        )
                    }
                }
            }

            // Actualizar la lista de datos de gráficos después de la inserción
            updateBarGraphList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Función para obtener y actualizar la lista de datos de gráficos desde CircularBuffer
    private fun updateBarGraphList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data: Flow<List<BarDataModel>> = circularBuffer.getBarGraphList()
                data.collect { barDataList ->
                    _listBarDataModel.value = barDataList
                }
            } catch (e: Exception) {
                Log.e(
                    "ErrorUpdateBarGraphList",
                    "Error al actualizar la lista de datos del gráfico: ${e.message}"
                )
            }
        }
    }

    private suspend fun getBarGraph(): List<BarDataModel> {
        return withContext(Dispatchers.IO) {
            try {
                circularBuffer.getBarGraphList().first()
            } catch (e: Exception) {
                Log.e("Error", "Error al conseguir los datos ${e.message}")
                emptyList()
            }
        }
    }

}

