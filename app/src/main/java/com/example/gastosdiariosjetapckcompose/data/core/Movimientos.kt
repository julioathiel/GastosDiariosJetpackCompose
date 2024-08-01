package com.example.gastosdiariosjetapckcompose.data.core

import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.GetMovimientosUsecase

interface Movimientos {
    suspend fun getMovimientosUseCase():Resource<GetMovimientosUsecase>
}