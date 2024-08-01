package com.example.gastosdiariosjetapckcompose.mis_ui_screen.movimientos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.data_base_manager.DataBaseManager
import com.example.gastosdiariosjetapckcompose.domain.model.BarDataModel
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.model.TotalGastosModel
import com.example.gastosdiariosjetapckcompose.domain.model.TotalIngresosModel
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

    private val getTotalGastosUseCase: GetTotalGastosUseCase,
    private val updateTotalGastosUseCase: UpdateTotalGastosUseCase,

    private val getGastosPorCategoriaUseCase: GetGastosPorCategoriaUseCase,
    private val updateItemGastosPorCategoriaUseCase: UpdateItemGastosPorCategoriaUseCase,
    private val deleteGastosPorCategoriaUseCase: DeleteGastosPorCategoriaUseCase,

    private val dataManager: DataBaseManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovimientosUiState>(MovimientosUiState.Loading)
    var uiStates: StateFlow<MovimientosUiState> = _uiState

    val uiState: StateFlow<MovimientosUiState> = getMovimientosUsecase().map(::Success)
        .catch { MovimientosUiState.Error(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MovimientosUiState.Loading
        )

    fun onEventHandler(e: OnActionsMovimientos) {
        when (e) {
            is OnActionsMovimientos.DeleteItem -> onItemRemoveMov(e.lisTransactions, e.item)
            is OnActionsMovimientos.EditItem -> updateItem(
                e.title,
                e.nuevoValor,
                e.description,
                e.item
            )
        }
    }

    private fun onItemRemoveMov(
        list: List<MovimientosModel>,
        movimientosModel: MovimientosModel
    ) {
        // Obtener el tipo de transacción (ingreso o gasto)
        val tipoTransaccion = movimientosModel.select

        viewModelScope.launch {
            // Verificar si es el último elemento de la lista
            val esUltimoItem = list.size == 1
            // Verificar si es el primer elemento de la lista
            val esPrimerItem = list.indexOf(movimientosModel) == 0

            // Eliminar la transacción
            if (esUltimoItem || esPrimerItem) {
                // Si es el último elemento, update el saldo a 0 y eliminar la transacción
                dataManager.deleteAllScreenMovimientos()
            } else {
                //actualizando el gastos por categorias
                deleteGastosPorCategoria(movimientosModel.title, movimientosModel.cash)
                // Si no es el último elemento, eliminar la transacción y update el saldo según el tipo
                deleteMovimientosUsecase(movimientosModel)

                when (tipoTransaccion) {
                    //si la transaccion fue de ingresos, se volvera a
                    // update el dinero total, restandolo de nuevo
                    true -> {
                        updateCurrentMoney(
                            list,
                            CurrentMoneyModel(money = -movimientosModel.cash.toDouble())
                        )
                        updateIngresosTotales(TotalIngresosModel(totalIngresos = movimientosModel.cash.toDouble()))
                    }
                    //si la transaccion fue de gastos, se volvera a
                    // update el dinero total, sumandolo de nuevo
                    false -> {
                        updateCurrentMoney(
                            list,
                            CurrentMoneyModel(money = movimientosModel.cash.toDouble())
                        )
                        updateGastos(TotalGastosModel(totalGastos = movimientosModel.cash.toDouble()))
                    }
                }
            }

        }
    }

    private fun updateItem(
        title: String,
        nuevoValor: String,
        description: String,
        itemModel: MovimientosModel
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val dataCurrentMoney = withContext(Dispatchers.IO) { getCurrentMoneyUseCase().money }
                val dataTotalGastos = withContext(Dispatchers.IO) { getTotalGastosUseCase()?.totalGastos }
                val dataTotalIngresos = withContext(Dispatchers.IO) { getTotalIngresosUseCase()?.totalIngresos }
                // actualiza el valor en la lista
                updateItemList(itemModel.copy(cash = nuevoValor, subTitle = description))
                //actualizando el gastos por categorias
                updateGastosCategory(title, nuevoValor, itemModel)

                //eliminando el item y actualizandolo en la base de datos
                when (itemModel.select) {
                    true -> {
                        if (nuevoValor > itemModel.cash) {
                            val total = nuevoValor.toDouble() - itemModel.cash.toDouble()
                            calculatorItem("suma", true, total, dataTotalIngresos!!)
                        } else if (nuevoValor < itemModel.cash) {
                            val total = itemModel.cash.toDouble() - nuevoValor.toDouble()
                            calculatorItem("resta", false, total, dataTotalIngresos!!)
                        }
                    }

                    false -> {
                        if (nuevoValor > itemModel.cash) {
                            val total = nuevoValor.toDouble() - itemModel.cash.toDouble()
                            calculatorItem("resta", false, total, dataCurrentMoney)
                            calculatorTotalGastos("suma", dataTotalGastos!!, total)
                        } else if (nuevoValor < itemModel.cash) {
                            val total = itemModel.cash.toDouble() - nuevoValor.toDouble()
                            calculatorItem("suma", false, dataCurrentMoney, total)
                            calculatorTotalGastos("resta", dataTotalGastos!!, total)
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //FUNCION QUE SE USA CUANDO SE ELIMINA UN GASTO
    private fun updateGastos(item: TotalGastosModel) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO) { getTotalGastosUseCase() }
                data?.totalGastos = data?.totalGastos?.minus(item.totalGastos)!!
                updateTotalGastosUseCase(data)
            } catch (e: Exception) {
                Log.e(
                    "updateGastos",
                    "Error: No se pudo obtener los datos de los gastos ${e.message}"
                )
            }
        }
    }

    //FUNCION QUE SE USA CUANDO SE ELIMINA UN INGRESO
    private fun updateIngresosTotales(item: TotalIngresosModel) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO) { getTotalIngresosUseCase() }
                data?.totalIngresos = data?.totalIngresos?.minus(item.totalIngresos)!!
                updateTotalIngresosUseCase(data)
            } catch (e: Exception) {
                Log.e(
                    "updateIngresos",
                    "Error: No se pudo obtener los datos de los ingresos ${e.message}"
                )
            }
        }
    }

    private fun updateCurrentMoney(
        listTransactions: List<MovimientosModel>,
        item: CurrentMoneyModel
    ) {
        viewModelScope.launch {
            if (listTransactions.size == 1) {
                // Si es el último elemento, establecer el saldo directamente a 0.0
                updateCurrentMoneyUsecase(item)
                Log.d("miApp", "actualizando $item")
            } else {
                val data = getCurrentMoneyUseCase().money
                val newValue: Double = data + item.money

                updateCurrentMoneyUsecase(
                    CurrentMoneyModel(
                        money = maxOf(newValue, 0.00),
                        isChecked = false
                    )
                )
            }
        }
    }

    private fun updateTotalGastos(nuevoDinero: Double) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO) {
                    getTotalGastosUseCase()
                }
                val datoDineroTotal = withContext(Dispatchers.IO) {
                    getCurrentMoneyUseCase().money
                }
                //si el usuario edita un gasto se updatea el total de gastos
                data?.totalGastos = nuevoDinero
                updateTotalGastosUseCase(data!!)
                //tambien updateia el limite por dia
                sharedLogic._limitePorDia.value = sharedLogic.calcularLimitePorDia(datoDineroTotal)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateDineroTotalUsuario(nuevoDinero: Double) {
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

    private fun updateItemList(item: MovimientosModel) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO) { getMovimientosUsecase().first() }
                val itemExisting = data.find { it.id == item.id }
                if (itemExisting != null) {
                    val updateItem = itemExisting.copy(cash = item.cash, subTitle = item.subTitle)
                    updateMovimientosUsecase(updateItem)
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    private fun updateGastosCategory(title: String, cash: String, itemModel: MovimientosModel) {
        viewModelScope.launch(Dispatchers.Main) {
            val data = withContext(Dispatchers.IO) { getGastosPorCategoriaUseCase().firstOrNull() }
            data?.map { category ->
                // Calcular la diferencia entre el valor anterior y el nuevo valor
                val oldTotalCash = category.totalGastado.toDouble() //ejemplo 25
                val oldItemCash = itemModel.cash // ejemplo 15
                val newCashDouble = cash.toDouble() // ejemplo 10
                val residuo = oldItemCash.toDouble() - newCashDouble //ejemplo 5
                // update el total gastado
                val diferencia = oldTotalCash - residuo //ejemplo 20

                if (category.title == title) {
                    // update el totalGastado con el nuevo valor de cash
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
                val oldTotalCash: Double = category.totalGastado.toDouble()
                val difference = oldTotalCash - cash.toDouble()

                category.totalGastado = difference.toString()
                if (category.title == title) {
                    deleteGastosPorCategoriaUseCase(category)
                }
            }
        }
    }

    private fun calculatorTotalGastos(
        operation: String,
        data: Double,
        dinerItem: Double
    ) {
        val nuevoDiner = if (data > dinerItem) {
            maxOf(operationDiner(operation, data, dinerItem), 0.0)
        } else { operationDiner(operation, dinerItem, data) }
        updateTotalGastos(nuevoDiner)
    }

    private fun calculatorItem(
        operation: String,
        tipTransaction: Boolean,
        valor1: Double,
        dinerItem: Double
    ) {
        val newDiner = when (tipTransaction) {
            true -> operationDiner(operation, valor1, dinerItem)
            false -> maxOf(operationDiner(operation, dinerItem, valor1), 0.0)
        }

        viewModelScope.launch(Dispatchers.Main) {
            try {
                //si la actualizacion se da en caso de ser ingreso de dinero
                val dato = withContext(Dispatchers.IO) {
                    getTotalIngresosUseCase()
                }
                dato?.totalIngresos = newDiner
                // actualiza el maximo total del dinero en el progrress
                updateTotalIngresosUseCase(dato!!)

                val dineroActual = withContext(Dispatchers.IO) {
                    getCurrentMoneyUseCase().money
                }
                sharedLogic._dineroActual.value = dineroActual
                sharedLogic._limitePorDia.value = sharedLogic.calcularLimitePorDia(dineroActual)
                updateDineroTotalUsuario(newDiner)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun operationDiner(
        operation: String,
        valorExistente: Double,
        nuevoValor: Double
    ): Double {
        //nuevo valor es el dinero ya calculado     valor existente es el valor del item
        val resultado = when (operation) {
            "suma" -> BigDecimal(valorExistente + nuevoValor)
            "resta" -> BigDecimal(valorExistente - nuevoValor)
            else -> throw IllegalArgumentException("Operación no válida")
        }.setScale(2, RoundingMode.HALF_EVEN)

        return resultado.toDouble()
    }
}


