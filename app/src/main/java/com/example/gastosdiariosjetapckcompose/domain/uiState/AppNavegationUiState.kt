package com.example.gastosdiariosjetapckcompose.domain.uiState

sealed interface AppNavegationUiState {
    object Loading : AppNavegationUiState
    data class Error(val throwable: Throwable) : AppNavegationUiState
    data class Success(val data: Boolean) : AppNavegationUiState
}