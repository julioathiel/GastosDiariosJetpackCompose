package com.example.gastosdiariosjetapckcompose.domain.uiState

import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatGastoModel


sealed interface ResultUiState {
    object Loading : ResultUiState
    data class Error(val throwable: Throwable) : ResultUiState
    data class Success(val data: List<UsuarioCreaCatGastoModel>) : ResultUiState
}
