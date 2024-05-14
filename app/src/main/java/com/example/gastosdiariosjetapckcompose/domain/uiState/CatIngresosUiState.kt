package com.example.gastosdiariosjetapckcompose.domain.uiState

import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatIngresosModel


sealed interface CatIngresosUiState{
    object Loading : CatIngresosUiState
    data class Error(val throwable: Throwable) : CatIngresosUiState
    data class Success(val data: List<UsuarioCreaCatIngresosModel>) : CatIngresosUiState
}