package com.example.gastosdiariosjetapckcompose.features.movimientos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MovimientosViewModel @Inject constructor(
    private val updateCurrentMoneyUsecase: UpdateCurrentMoneyUseCase,
    private val updateMovimientosUsecase: UpdateMovimientosUsecase,
    private val getCurrentMoneyUseCase: GetCurrentMoneyUseCase,
    private val getMovimientosUsecase: GetMovimientosUsecase,
    private val deleteMovimientosUsecase: DeleteMovimientosUsecase
) : ViewModel() {

    val sharedLogic = SharedLogic()

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
                    CurrentMoneyModel(money = 0.00, isChecked = false)
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

                updateCurrentMoneyUsecase(
                    CurrentMoneyModel(
                        money = maxOf(newValor, 0.00),
                        isChecked = false
                    )
                )
            }
        }
    }

    fun actualizandoItem(
        nuevoValor: String,
        description: String,
        movimientosModel: MovimientosModel
    ) {
        viewModelScope.launch {
            // Obtener el valor de isChecked de manera síncrona
            val item = getCurrentMoneyUseCase()

            var dineroActualizado = 0.0
            // Obtener el tipo de transacción (ingreso o gasto)
            val tipoTransaccion = movimientosModel.select
            Log.d("miApp", "tipoTransaccion: $tipoTransaccion")
            try {
                //eliminando el item y actualizandolo en la base de datos para su posterior edicion
                if (tipoTransaccion) {
                    //si la transaccion fue de ingresos, se volvera a
                    // actualizar el dinero total, restandolo de nuevo
                    actualizarDineroElegido(
                        CurrentMoneyModel(money = -movimientosModel.cash.toDouble())
                    ) { it ->
                        dineroActualizado = it
                        //recupera el total
                        Log.d("miApp","dineroActualizado = $dineroActualizado")
                        calculadoraItem(nuevoValor, tipoTransaccion, dineroActualizado)
                    }
                } else {
                    //ANDA PERFECTO
                    //si la transaccion fue de gastos, se volvera a
                    // actualizar el dinero total, sumandolo de nuevo
                    actualizarDineroElegido(
                        CurrentMoneyModel(money = movimientosModel.cash.toDouble())
                    ) { it ->
                        dineroActualizado = it
                        //recupera el total
                        Log.d("miApp","dineroActualizado = $dineroActualizado")
                        calculadoraItem(nuevoValor, tipoTransaccion, dineroActualizado)
                    }
                }
                // actualiza el valor en la lista
                movimientosModel.cash = nuevoValor
                movimientosModel.subTitle = description
                updateBaseDatos(movimientosModel)
                //calculadoraItem(nuevoValor, item.isChecked, dineroActualizado)

                // Log y cálculo del límite por día
                val logMessage =
                    if (item.isChecked) "Insertando" else "Actualizando item"
                // Log.d("miApp", "$logMessage: $nuevoDinero")
//                _limitePorDia.value = calcularLimitePorDia()

            } catch (e: NumberFormatException) {
                Log.e("miApp", "Error al convertir el valor a Double: $nuevoValor")
                // Manejar el error según sea necesario
            }
        }
    }

    private fun actualizarDineroElegido(
        item: CurrentMoneyModel,
        callback: (Double) -> Unit
    ) {
        viewModelScope.launch {
                val dineroGuardado = getCurrentMoneyUseCase().money
            Log.d("miApp", "dineroGuardado: $dineroGuardado")
             val   newValor = dineroGuardado + item.money
            Log.d("miApp", "newValor: $newValor")
                updateCurrentMoneyUsecase(
                    CurrentMoneyModel(
                        money = maxOf(newValor, 0.00),
                        isChecked = false
                    )
                )
            callback(newValor) // Llamar al callback con el nuevo valor
        }
    }


    private fun calculadoraItem(nuevoValor: String, checked: Boolean, dineroActualizado: Double) {
        Log.d("miApp","dineroActualizado de calculadoraItem = $dineroActualizado")
        viewModelScope.launch {
            // Calcula el nuevo dinero según la opción elegida por el usuario
            val nuevoDinero = if(checked){
                agregarDinero(nuevoValor.toDouble(), dineroActualizado ?: 0.0)
            }else{
                maxOf(restarDinero(dineroActualizado,nuevoValor.toDouble()), 0.0)
            }
            Log.d("miApp", "calculadoraItem- nuevoDinero : $nuevoDinero")
            val newValor = getCurrentMoneyUseCase()
            newValor.money = nuevoDinero
            newValor.isChecked = false
            Log.d("miApp", "ACTUALIZANDO de calculadoraItem : $newValor")
            updateCurrentMoneyUsecase(newValor)//para mostrar en la  pantalla principal
            sharedLogic._dineroActual.value = getCurrentMoneyUseCase().money
        }
    }

    private fun restarDinero(dineroActualizado: Double,nuevoValor: Double): Double {
        Log.d("miApp","dineroActualizado en restarDinero = $dineroActualizado")
        Log.d("miApp","nuevoValor en restarDinero = $nuevoValor")
        val resultado =
            BigDecimal(dineroActualizado - nuevoValor).setScale(
                2,
                RoundingMode.HALF_EVEN
            )
        Log.d("miApp","restandoDinero: $resultado")
        return resultado.toDouble()
    }

    private fun agregarDinero(nuevoValor: Double, valorExistente: Double): Double {
        val resultado = BigDecimal(valorExistente + nuevoValor)
            .setScale(2, RoundingMode.HALF_EVEN)
        return resultado.toDouble()
    }

    private fun updateBaseDatos(item: MovimientosModel) {
        viewModelScope.launch {
            getMovimientosUsecase().map { listaMovimientos ->
                val movimientoActualizado = listaMovimientos.map { movimiento ->
                    if (movimiento.id == item.id) {
                        movimiento.cash = item.cash // Actualizar el dinero
                        movimiento.subTitle = item.subTitle // Actualizar la descripción
                    }
                    movimiento // Devolver el movimiento actualizado o sin cambios
                }.find { it.id == item.id } // Encontrar el movimiento actualizado
                movimientoActualizado // Devolver el movimiento actualizado

            }.collect { movimientoActualizado ->
                if (movimientoActualizado != null) {
                    // Si se encontró y actualizó el movimiento, se pasa a la función de actualización individual
                    updateMovimientosUsecase(movimientoActualizado)
                } else {
                    // Manejar el caso en el que no se encuentre el elemento a actualizar
                    // Puede ser útil lanzar una excepción o manejarlo de alguna otra manera
                }
            }
        }
//    fun calcularLimitePorDia(): Double {
//        val dineroActual = _dineroActual.value
//        val limitePorDia = if (_diasRestantes.value != null && dineroActual != null && _diasRestantes.value != 0) {
//            dineroActual / _diasRestantes.value!!.toDouble()
//        } else {
//            0.0
//        }
//        return limitePorDia
//    }
    }
}


