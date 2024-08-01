package com.example.gastosdiariosjetapckcompose.domain.uiState

sealed class EventHandler {
    data class SaveDarkThemeValue(val value: Boolean?) : EventHandler()
    data class SelectedDarkThemeValue(val value: Boolean) : EventHandler()
}