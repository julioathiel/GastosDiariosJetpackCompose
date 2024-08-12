package com.example.gastosdiariosjetapckcompose.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.domain.uiState.AppNavegationUiState
import com.example.gastosdiariosjetapckcompose.data.core.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val dataStorePreferences: DataStorePreferences) : ViewModel() {

    val uiState: StateFlow<AppNavegationUiState> = showViewPager()
        .map(AppNavegationUiState::Success)
        .catch { AppNavegationUiState.Error(it) }
        // .distinctUntilChanged() // Esto asegura que solo se emitan valores diferentes de los que ya han sido emitidos
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppNavegationUiState.Loading
        )

    private fun showViewPager(): Flow<Boolean> {
        return dataStorePreferences.getViewPager()

    }
}