package com.example.gastosdiariosjetapckcompose.features.notificacion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerSample(
    horaPredefinida: LocalTime,
    onSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = horaPredefinida.hour,
        initialMinute = horaPredefinida.minute,
        is24Hour = true
    )
    var finalTime by remember { mutableStateOf("") }
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimePickerDialog(
            onCancel = { onDismiss() },
            onConfirm = {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, state.hour)
                cal.set(Calendar.MINUTE, state.minute)
                cal.isLenient = false
                finalTime = formatter.format(cal.time)
                val selectedTime = LocalTime.of(state.hour, state.minute)
                //enviando la hora seleccionada
                onSelected(selectedTime)

                onDismiss()
            },
        ) {
            TimeInput(state = state)
        }
    }
}