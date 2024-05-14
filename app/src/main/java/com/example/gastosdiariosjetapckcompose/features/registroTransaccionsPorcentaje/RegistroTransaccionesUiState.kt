package com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje

import com.example.gastosdiariosjetapckcompose.domain.model.GastosPorCategoriaModel


sealed interface RegistroTransaccionesUiState{
    object Loading: RegistroTransaccionesUiState
    data class Error(val throwable: Throwable): RegistroTransaccionesUiState
    data class  Success(val listGastosPorCat:List<GastosPorCategoriaModel>): RegistroTransaccionesUiState
}