package com.example.gastosdiariosjetapckcompose.features.notificacion

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.data.core.Constants.NOTIFICATION_ID
import com.example.gastosdiariosjetapckcompose.data.core.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val dataStorePreferences: DataStorePreferences) :
    ViewModel() {
    private val _selectedHour = MutableStateFlow(0)
    val selectedHour: StateFlow<Int> = _selectedHour

    private val _selectedMinute = MutableStateFlow(0)
    val selectedMinute: StateFlow<Int> = _selectedMinute

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

    private fun setHoraMinuto(hour: Int, minute: Int) {
        viewModelScope.launch {
            dataStorePreferences.setHoraMinuto(hour, minute)
            getHourMinute()
        }
    }


    fun notificationProgrammed(
        context: Context,
        hour: Int? = null,
        minute: Int? = null
    ) {
        // Verificar si el usuario especificó una hora y un minuto, de lo contrario, usar los predeterminados
        val finalHour = hour ?: 21
        val finalMinute = minute ?: 0
        // Configurar la alarma para la hora especificada por el usuario o para las 21:00 horas si no se especifica ninguna hora y minuto
        settingAlarm(context, finalHour, finalMinute)
    }


    private fun settingAlarm(
        context: Context,
        hour: Int,
        minute: Int
    ) {
        val intent = Intent(context, NotificacionProgramada::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Configurar la alarma para la hora y el minuto seleccionados
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val (selectedCalendar) = adjustSelectedTime(hour, minute)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            //
        } else {
            try {
                // Configurar la alarma para que se repita todos los días a la misma hora
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    selectedCalendar.timeInMillis,
                    pendingIntent
                )
            } catch (e: SecurityException) {
                Log.d("alarma", "Error de seguridad al configurar la alarma: ${e.message}")
            }
        }
    }

    // Función para confirmar la hora seleccionada
    fun confirmSelectedTime(
        context: Context,
        hour: Int,
        minute: Int
    ) {
        //guardando hora y minuto
        setHoraMinuto(hour, minute)
        // Obtener la hora y el minuto de selectedTime
        notificationProgrammed(context, hour, minute)

        val (selectedCalendar, currentCalendar) = adjustSelectedTime(hour, minute)

        val differenceInMillis = selectedCalendar.timeInMillis - currentCalendar.timeInMillis

        val seconds: Long = (differenceInMillis / 1000)
        val minutes: Long = seconds / 60
        val hours: Long = minutes / 60
        val remainingSeconds: Long = seconds % 60
        val remainingMinutes: Long = minutes % 60

        val message: String =
            if (hour == currentCalendar.get(Calendar.HOUR_OF_DAY) && minute == currentCalendar.get(
                    Calendar.MINUTE
                )
            ) {
                "Faltan: $seconds segundos"
            } else if (hour == 0) {
                "Faltan: $remainingMinutes minutos con $seconds segundos"
            } else {
                "Faltan: ${formatTime(hours)}:${formatTime(remainingMinutes)}:${
                    formatTime(
                        remainingSeconds
                    )
                }"
            }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }

    private fun adjustSelectedTime(hour: Int, minute: Int): Pair<Calendar, Calendar> {
        val currentCalendar = Calendar.getInstance()
        val selectedCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (selectedCalendar.before(currentCalendar)) {
            selectedCalendar.add(Calendar.DATE, 1)
        }

        return Pair(selectedCalendar, currentCalendar)
    }

    private fun formatTime(value: Long) = String.format(Locale.US, "%02d", value)
}