package com.example.gastosdiariosjetapckcompose.features.movimientos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.SharedLogic
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.GetCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.UpdateCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.DeleteMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.GetMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.UpdateMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientosViewModel @Inject constructor(
    private val updateCurrentMoneyUsecase: UpdateCurrentMoneyUseCase,
    private val updateMovimientosUsecase: UpdateMovimientosUsecase,
    private val getCurrentMoneyUseCase: GetCurrentMoneyUseCase,
    getMovimientosUsecase: GetMovimientosUsecase,
    private val deleteMovimientosUsecase: DeleteMovimientosUsecase
) : ViewModel() {
    val uiState: StateFlow<MovimientosUiState> = getMovimientosUsecase().map(::Success)
        .catch { MovimientosUiState.Error(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MovimientosUiState.Loading
        )

    fun onItemRemoveMov(
        listTransacciones: List<MovimientosModel>,
        movimientosModel: MovimientosModel
    ) {
        // Obtener el tipo de transacción (ingreso o gasto)
        val tipoTransaccion = movimientosModel.select

        viewModelScope.launch {
            // Verificar si es el último elemento de la lista
            val esUltimoItem = listTransacciones.size == 1
            // Eliminar la transacción
            if (esUltimoItem) {
                // Si es el último elemento, actualizar el saldo a 0 y eliminar la transacción

                deleteMovimientosUsecase(movimientosModel)
                actualizarDineroTotal(
                    listTransacciones,
                    CurrentMoneyModel(money = 0.00)
                )
            } else {
                // Si no es el último elemento, eliminar la transacción y actualizar el saldo según el tipo
                deleteMovimientosUsecase(movimientosModel)
                if (tipoTransaccion) {
                    //si la transaccion fue de ingresos, se volvera a
                    // actualizar el dinero total, restandolo de nuevo
                    actualizarDineroTotal(
                        listTransacciones,
                        CurrentMoneyModel(money = -movimientosModel.cash.toDouble())
                    )
                } else {
                    //si la transaccion fue de gastos, se volvera a
                    // actualizar el dinero total, sumandolo de nuevo
                    actualizarDineroTotal(
                        listTransacciones,
                        CurrentMoneyModel(money = movimientosModel.cash.toDouble())
                    )
                }
            }

        }
    }

    private fun actualizarDineroTotal(
        listTransacciones: List<MovimientosModel>,
        item: CurrentMoneyModel
    ) {
        viewModelScope.launch {
            if (listTransacciones.size == 1) {
                // Si es el último elemento, establecer el saldo directamente a 0.0
                updateCurrentMoneyUsecase(item)
                Log.d("miApp", "actualizando $item")
            } else {
                val dineroGuardado = getCurrentMoneyUseCase().money
                val newValor: Double = dineroGuardado + item.money

                updateCurrentMoneyUsecase(CurrentMoneyModel(money = maxOf(newValor,0.00), isChecked = false))
            }
        }
    }
}


