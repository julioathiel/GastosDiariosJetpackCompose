package com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.GetGastosPorCategoriaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.GetTotalIngresosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistroTransaccionesViewModel @Inject constructor(
    private val getGastosPorCategoriaUseCase: GetGastosPorCategoriaUseCase,
    private val getTotalIngresosUseCase: GetTotalIngresosUseCase
) : ViewModel() {
    private val _totalGastos = MutableLiveData<Double>()
    val totalGastos: LiveData<Double> = _totalGastos

    private val _porcentajeGasto = MutableLiveData<Double>()
    val porcentajeGasto: LiveData<Double> = _porcentajeGasto

    private var _mostrandoDineroTotalIngresosRegistros = MutableLiveData<Double>()
    val mostrandoDineroTotalIngresosRegistros: LiveData<Double> =
        _mostrandoDineroTotalIngresosRegistros

    private var _mostrandoProgressActual = MutableLiveData<Double>()
    val mostrandoProgressActual:LiveData<Double> =_mostrandoProgressActual

init{
    ingresosTotalesMaximoProgress()
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


    private fun ingresosTotalesMaximoProgress(){
        viewModelScope.launch {
            _mostrandoDineroTotalIngresosRegistros.value = getTotalIngresosUseCase()?.totalIngresos
            Log.d("miApp", "viewTotalIngresos: ${_mostrandoDineroTotalIngresosRegistros.value}")
        }
    }
}

