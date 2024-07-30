package com.example.gastosdiariosjetapckcompose.features.movimientos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.uiState.MovimientosUiState
import com.example.gastosdiariosjetapckcompose.domain.uiState.MovimientosUiState.Success
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.DeleteGastosPorCategoriaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.GetGastosPorCategoriaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.UpdateItemGastosPorCategoriaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.GetCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.UpdateCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.DeleteMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.GetMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.UpdateMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.DeleteTotalIngresosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.GetTotalGastosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.GetTotalIngresosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.UpdateTotalGastosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.UpdateTotalIngresosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MovimientosViewModel @Inject constructor(
    private val getCurrentMoneyUseCase: GetCurrentMoneyUseCase,
    private val updateCurrentMoneyUsecase: UpdateCurrentMoneyUseCase,

    private val getMovimientosUsecase: GetMovimientosUsecase,
    private val updateMovimientosUsecase: UpdateMovimientosUsecase,
    private val deleteMovimientosUsecase: DeleteMovimientosUsecase,

    private val getTotalIngresosUseCase: GetTotalIngresosUseCase,
    private val updateTotalIngresosUseCase: UpdateTotalIngresosUseCase,
    private val deleteTotalIngresosUseCase: DeleteTotalIngresosUseCase,

    private val getTotalGastosUseCase: GetTotalGastosUseCase,
    private val updateTotalGastosUseCase: UpdateTotalGastosUseCase,

    private val getGastosPorCategoriaUseCase: GetGastosPorCategoriaUseCase,
    private val updateItemGastosPorCategoriaUseCase: UpdateItemGastosPorCategoriaUseCase,
    private val deleteGastosPorCategoriaUseCase: DeleteGastosPorCategoriaUseCase
) : ViewModel() {
    val uiState: StateFlow<MovimientosUiState> = getMovimientosUsecase().map(::Success)
        .catch { MovimientosUiState.Error(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MovimientosUiState.Loading
        )

    // Declarando un HashSet para almacenar los IDs de los items actualizados
    private val itemsActualizations = HashSet<Int>()
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
                //actualizando el gastos por categorias
                deleteGastosPorCategoria(movimientosModel.title,movimientosModel.cash)
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
        listTransactions: List<MovimientosModel>,
        item: CurrentMoneyModel
    ) {
        viewModelScope.launch {
            if (listTransactions.size == 1) {
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
        title: String,
        nuevoValor: String,
        description: String,
        itemModel: MovimientosModel
    ) {
        //nuevoValor es el nuevo valor que el usuario puso
        // itemModel tiene los valores guardados de item
        viewModelScope.launch(Dispatchers.Main) {
            // Obtener el tipo de transacción (ingreso o gasto)
            val tipoTransaccion = itemModel.select
            try {
                val dineroGuardado = withContext(Dispatchers.IO) {
                    getCurrentMoneyUseCase().money
                }
                val dineroGuardadoTotalGastos = withContext(Dispatchers.IO) {
                    getTotalGastosUseCase()?.totalGastos
                }
                // actualiza el valor en la lista
                updateItemLista(itemModel.copy(cash = nuevoValor, subTitle = description))
                //actualizando el gastos por categorias
                updateGastosPorCategoria(title, nuevoValor, itemModel)


                //eliminando el item y actualizandolo en la base de datos
                if (tipoTransaccion) {
                    //si la transaccion fue de ingresos
                    // actualizar el dinero total, restandolo de nuevo
                    if (nuevoValor > itemModel.cash) {
                        //restando el item de la lista con el nuevo valor asi obtenemos el residuo
                        val total = nuevoValor.toDouble() - itemModel.cash.toDouble()
                        calculadoraItem("suma", true, total, dineroGuardado)
                    } else {
                        // se resta el item con el nuevo valor para no obtener un resultado negativo
                        val total = itemModel.cash.toDouble() - nuevoValor.toDouble()
                        calculadoraItem("resta", false, dineroGuardado, total)
                    }

                } else {
                    //si la transaccion fue de gastos
                    if (nuevoValor > itemModel.cash) {
                        //se resta el nuevoValor con el valor del item para obtener su residuo
                        val total = nuevoValor.toDouble() - itemModel.cash.toDouble()
                        //se resta el residuo con el dineroGuardado
                        calculadoraItem("resta", false, total, dineroGuardado)

                        calculadoraTotalGastos(
                            "suma",
                            dineroGuardadoTotalGastos!!,
                            total
                        )
                    } else {
                        //si el nuevoValor es menor que el item que estaba antes
                        // se resta el item con el nuevo valor para no obtener un resultado negativo
                        val total = itemModel.cash.toDouble() - nuevoValor.toDouble()
                        //   3000 =        5000               -     2000
                        //se suma el residuo con el dineroGuardado
                        calculadoraItem("suma", false, dineroGuardado, total)
                        //actualizando el totalgastos si es menor que el guardado
                        calculadoraTotalGastos(
                            "resta",
                            dineroGuardadoTotalGastos!!,
                            total
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun calculadoraTotalGastos(
        operacion: String,
        valor1: Double,
        dineroItem: Double
    ) {
        val nuevoDinero = if (valor1 > dineroItem) {
            maxOf(operacionDinero(operacion, valor1, dineroItem), 0.0)
        } else {
            operacionDinero(operacion, dineroItem, valor1)
        }
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO) {
                    getTotalGastosUseCase()
                }
                val datoDineroTotal = withContext(Dispatchers.IO) {
                    getCurrentMoneyUseCase().money
                }
                //si el usuario edita un gasto se actualizara el total de gastos
                data?.totalGastos = nuevoDinero
                updateTotalGastosUseCase(data!!)
                //tambien actualizaria el limite por dia
                sharedLogic._limitePorDia.value = sharedLogic.calcularLimitePorDia(datoDineroTotal)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun actualizarDineroTotalUsuario(nuevoDinero: Double) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val dato = withContext(Dispatchers.IO) {
                    getCurrentMoneyUseCase()
                }
                dato.money = nuevoDinero

                updateCurrentMoneyUsecase(
                    dato.copy(money = dato.money, isChecked = false)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun calculadoraItem(
        operacion: String, tipoTransaccion: Boolean,
        valor1: Double, dineroItem: Double
    ) {
        val nuevoDinero = if (tipoTransaccion) {
            operacionDinero(operacion, valor1, dineroItem ?: 0.0)
        } else {
            maxOf(operacionDinero(operacion, dineroItem, valor1), 0.0)
        }
        viewModelScope.launch(Dispatchers.Main) {
            try {
                if (tipoTransaccion) {
                    //si la actualizacion se da en caso de ser ingreso de dinero
                    val dato = withContext(Dispatchers.IO) {
                        getTotalIngresosUseCase()
                    }
                    dato?.totalIngresos = nuevoDinero
                    // actualiza el maximo total del dinero en el progrress
                    updateTotalIngresosUseCase(dato!!)
                } else {

                }
                val dineroActual = withContext(Dispatchers.IO) {
                    getCurrentMoneyUseCase().money
                }
                sharedLogic._dineroActual.value = dineroActual
                sharedLogic._limitePorDia.value = sharedLogic.calcularLimitePorDia(dineroActual)
                actualizarDineroTotalUsuario(nuevoDinero)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun operacionDinero(
        operacion: String,
        valorExistente: Double,
        nuevoValor: Double
    ): Double {
        //nuevo valor es el dinero ya calculado     valor existente es el valor del item
        val resultado = when (operacion) {
            "suma" -> BigDecimal(valorExistente + nuevoValor)
            "resta" -> BigDecimal(valorExistente - nuevoValor)
            else -> throw IllegalArgumentException("Operación no válida")
        }.setScale(2, RoundingMode.HALF_EVEN)

        return resultado.toDouble()
    }

    private fun updateItemLista(item: MovimientosModel) {
        // Verificar si el item ya ha sido actualizado previamente
        if (itemsActualizations.contains(item.id)) {
            // El item ya ha sido actualizado, no realizar la actualización repetida
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val dato = withContext(Dispatchers.IO) {
                    getMovimientosUsecase()
                }
                dato.map { listaMovimientos ->
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
                        // Agregar el ID del item actualizado al registro
                        itemsActualizations.add(movimientoActualizado.id)
                        sharedLogic._itemActualizado.value = true
                    } else {
                        // Manejar el caso en el que no se encuentre el elemento a actualizar
                        // Puede ser útil lanzar una excepción o manejarlo de alguna otra manera
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    private fun updateGastosPorCategoria(title: String, cash: String) {
//        viewModelScope.launch(Dispatchers.Main) {
//            val data = withContext(Dispatchers.IO) { getGastosPorCategoriaUseCase().firstOrNull() }
//            data?.map { category ->
//                // Actualizar el monto en la categoría
//                val diferencia = category.totalGastado.toDouble() - cash.toDouble()
//                category.totalGastado = (category.totalGastado.toDouble() - diferencia).toString()
//
//                if (category.title == title) {
//                    // Eliminar la categoría si el monto actualizado es 0
//                    if (diferencia.toInt() == 0) {
//                        deleteGastosPorCategoriaUseCase(category)
//                    } else {
//                        updateItemGastosPorCategoriaUseCase(category)
//                    }
//                }
//
//            }
//        }
//    }

    private fun updateGastosPorCategoria(title: String, cash: String, itemModel: MovimientosModel) {
        viewModelScope.launch(Dispatchers.Main) {
            val data = withContext(Dispatchers.IO) { getGastosPorCategoriaUseCase().firstOrNull() }
            data?.map { category ->
                // Calcular la diferencia entre el valor anterior y el nuevo valor
                val oldTotalCash = category.totalGastado.toDouble() //ejemplo 25
                val oldItemCash = itemModel.cash // ejemplo 15
                val newCashDouble = cash.toDouble() // ejemplo 10
                val residuo = oldItemCash.toDouble() - newCashDouble //ejemplo 5
                // Actualizar el total gastado
                val diferencia = oldTotalCash - residuo //ejemplo 20

                Log.d("nuevoTotal", "diferencia = $diferencia")

                if (category.title == title) {
                    // Actualizar el totalGastado con el nuevo valor de cash
                    category.totalGastado = diferencia.toString()
                    updateItemGastosPorCategoriaUseCase(category)
                }
            }
        }
    }


    private fun deleteGastosPorCategoria(title: String, cash: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val data = withContext(Dispatchers.IO) { getGastosPorCategoriaUseCase().firstOrNull() }
            data?.map { category ->
                val oldTotalCash: Double = category.totalGastado.toDouble() //ejemplo 25
                val diferencia = oldTotalCash - cash.toDouble()
                Log.d("nuevoTotal", "diferencia delete = $diferencia")
                category.totalGastado = diferencia.toString()
                if (category.title == title) {
                    deleteGastosPorCategoriaUseCase(category)
                }
            }
        }
    }
}


