package com.example.gastosdiariosjetapckcompose.features.movimientos

import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel

sealed interface MovimientosUiState{
    object Loading: MovimientosUiState
    data class Error(val throwable: Throwable): MovimientosUiState
    data class  Success(val movimientos:List<MovimientosModel>): MovimientosUiState
}