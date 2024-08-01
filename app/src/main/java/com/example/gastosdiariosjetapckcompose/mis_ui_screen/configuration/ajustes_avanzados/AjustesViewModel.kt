package com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ajustes_avanzados

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.data.core.DataStorePreferences
import com.example.gastosdiariosjetapckcompose.domain.model.DarkModeModel
import com.example.gastosdiariosjetapckcompose.domain.uiState.EventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AjustesViewModel @Inject constructor(private val dataStorePreferences: DataStorePreferences) :
    ViewModel() {

    var state by mutableStateOf(DarkModeModel(false))
        private set

    init {
        getDarkTheme()
    }

    fun onEventHandler(e: EventHandler) {
        when (e) {
            is EventHandler.SaveDarkThemeValue -> updateDarkModeActivated(state.isDarkModeActive)
            is EventHandler.SelectedDarkThemeValue -> state = state.copy(isDarkModeActive = e.value)
        }
    }

    private fun updateDarkModeActivated(darkModeActive: Boolean) {
        viewModelScope.launch {
            Log.d("switch", "switch darkModeActive = $darkModeActive")
            dataStorePreferences.updateIsDarkModeActive(darkModeActive)
        }
    }

    private fun getDarkTheme() {
        viewModelScope.launch {
            dataStorePreferences.darkModeFlow.collect {
                state = state.copy(isDarkModeActive = it.isDarkModeActive)
            }
        }
    }
}