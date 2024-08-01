package com.example.gastosdiariosjetapckcompose.mis_ui_screen.notificacion.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Locale

@Composable
fun CountdownTimer(hour: Int, minute: Int, modifier: Modifier, key: String) {
    var elapsedTime by remember { mutableLongStateOf(0L) }
    Log.d("horasSelect", "$hour, $minute")
    LaunchedEffect(Unit) {
        while (true) {
            val currentCalendar = Calendar.getInstance()
            val selectedCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
            if (selectedCalendar.before(currentCalendar)) {
                selectedCalendar.add(Calendar.DATE, 1)
            }
            Log.d(
                "horasSelect",
                "tiempo = ${selectedCalendar.get(Calendar.HOUR_OF_DAY)}, minute = ${
                    selectedCalendar.get(Calendar.MINUTE)
                }"
            )
            val differenceInMillis = selectedCalendar.timeInMillis - currentCalendar.timeInMillis
            elapsedTime = differenceInMillis

            delay(1000) // Actualizar cada segundo
        }
    }


    val seconds = (elapsedTime / 1000).toInt()
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    val hours = minutes / 60
    val remainingMinutes = minutes % 60


    Row(modifier = modifier, horizontalArrangement = Arrangement.Absolute.Center) {
        Text(
            "Faltan: ${formatTime(hours)}:" +
                    "${formatTime(remainingMinutes)}:" +
                    formatTime(remainingSeconds)
        )
    }
}


fun formatTime(value: Int): String {
    return String.format(Locale.US, "%02d", value)
}