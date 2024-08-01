package com.example.gastosdiariosjetapckcompose.data_base_manager

import com.example.Ingresosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatIngreso.ClearAllUserCreaCatIngresosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria.ClearAllGastosPorCatUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatGasto.ClearAllUserCatGastosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.ClearAllBarDataGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.ClearAllFechaUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.imagenSeleccionada.ClearAllImagenSelectedUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.ClearAllCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.ClearAllMovimientosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.ClearAllTotalGastosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.ClearAllTotalIngresosUseCase
import javax.inject.Inject

class DataBaseManager @Inject constructor(
    private val clearAllBarDataGraphUseCase: ClearAllBarDataGraphUseCase,
    private val clearAllFechaUsecase: ClearAllFechaUsecase,
    private val clearAllGastosPorCatUseCase: ClearAllGastosPorCatUseCase,
    private val clearAllMovimientosUseCase: ClearAllMovimientosUseCase,
    private val clearAllTotalIngresosUseCase: ClearAllTotalIngresosUseCase,
    private val clearAllTotalGastosUseCase: ClearAllTotalGastosUseCase,
    private val clearAllCurrentMoneyUseCase: ClearAllCurrentMoneyUseCase,
    private val clearAllUserCreaCatGastosUseCase: ClearAllUserCatGastosUseCase,
    private val clearAllUserCreaCatIngresosUseCase: ClearAllUserCreaCatIngresosUseCase,
    private val clearAllImagenSelectedUseCase: ClearAllImagenSelectedUseCase
) {
    suspend fun deleteAllApp() {
        clearAllFechaUsecase()
        clearAllGastosPorCatUseCase()
        clearAllMovimientosUseCase()
        clearAllTotalIngresosUseCase()
        clearAllTotalGastosUseCase()
        clearAllCurrentMoneyUseCase()
        clearAllImagenSelectedUseCase()
    }

    //FUNCION PARA LA PANTALLA DE MOVIMIENTOS
    suspend fun deleteAllScreenMovimientos(){
        clearAllGastosPorCatUseCase()
        clearAllMovimientosUseCase()
        clearAllTotalIngresosUseCase()
        clearAllTotalGastosUseCase()
        clearAllCurrentMoneyUseCase()
    }

    //----------------------------------------------//
    //  * funciones opcionales para eliminar las tablas completas de la base de datos *  //
    suspend fun deleteAllGraphBar() {
        clearAllBarDataGraphUseCase()
    }
    suspend fun deleteAllUserCreaCatGastos() {
        clearAllUserCreaCatGastosUseCase()
    }
    suspend fun deleteAllUserCreaCatIngresos() {
        clearAllUserCreaCatIngresosUseCase()
    }
    //----------------------------------------------//

    suspend fun deleteAllExpenses(){
        clearAllMovimientosUseCase()
    }

    suspend fun deleteAllGastosPorCategory(){
        clearAllGastosPorCatUseCase()
    }

    suspend fun deleteCurrentMoney(){
        clearAllCurrentMoneyUseCase()
    }
}