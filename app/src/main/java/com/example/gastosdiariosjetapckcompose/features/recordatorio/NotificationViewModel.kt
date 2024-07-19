package com.example.gastosdiariosjetapckcompose.features.recordatorio

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.features.core.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val dataStorePreferences: DataStorePreferences) :
    ViewModel() {
    private val _selectedHour = MutableLiveData(0)
    val selectedHour: LiveData<Int> = _selectedHour

    private val _selectedMinute = MutableLiveData(0)
    val selectedMinute: LiveData<Int> = _selectedMinute

    init {
        getHourMinute()
    }

    // Función para obtener la fecha máxima del DataStore
    private fun getHourMinute() {
        viewModelScope.launch {
            dataStorePreferences.getHoraMinuto().collect { time ->
                _selectedHour.value = time.hour
                _selectedMinute.value = time.minute
            }
        }

    }

    fun setHoraMinuto(hour: Int, minute: Int) {
        viewModelScope.launch {
            dataStorePreferences.setHoraMinuto(hour, minute)
            getHourMinute()
        }
    }
}