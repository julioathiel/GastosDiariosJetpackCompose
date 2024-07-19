package com.example.gastosdiariosjetapckcompose.features.recordatorio

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.gastosdiariosjetapckcompose.Constants.HORAS_PREDEFINIDAS
import com.example.gastosdiariosjetapckcompose.Constants.MINUTOS_PREDEFINIDOS
import com.example.gastosdiariosjetapckcompose.Constants.MY_CHANNEL_ID
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.features.core.DataStorePreferences
import com.example.gastosdiariosjetapckcompose.features.recordatorio.NotificacionProgramada.Companion.NOTIFICATION_ID
import com.example.gastosdiariosjetapckcompose.features.recordatorio.components.TimePickerSample
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import java.time.LocalTime
import java.util.Calendar


@Composable
fun RecordatorioScreen(
    navController: NavHostController,
    notificationViewModel: NotificationViewModel
) {
    val context = LocalContext.current

    val selectedTimeHour by notificationViewModel.selectedHour.observeAsState(0)
    val selectedTimeMinute by notificationViewModel.selectedMinute.observeAsState(0)
    // Crear un LocalTime a partir de los valores observados
    var selectedTime by remember {
        mutableStateOf(
            LocalTime.of(
                selectedTimeHour,
                selectedTimeMinute
            )
        )
    }

    Log.d("localTimes", "$selectedTime")

    // State for showing time picker dialog
    var showTimePicker by remember { mutableStateOf(false) }

    BackHandler {
        //al presion el boton fisico de retoceso, se dirige a la pantalla de configuracion
        navController.navigate(Routes.ConfigurationScreen.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        sharedLogic.LoaderData(
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp)
                .align(Alignment.CenterHorizontally),
            image = R.raw.notification_lottie,
            repeat = false
        )


        Text(
            text = stringResource(id = R.string.recordatorio_titulo),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.recordatorio_body),
            fontSize = 14.sp,
            color = colorResource(id = R.color.grayCuatro),
            modifier = Modifier.padding(vertical = 16.dp)
        )


        Button(modifier = Modifier
            .fillMaxWidth()
            .height(51.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),// Fondo transparente
            onClick = { showTimePicker = true }
        ) {

            Text(
                text = "$selectedTime",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (showTimePicker) {
            TimePickerSample(
                horaPredefinida = LocalTime.of(selectedTimeHour, selectedTimeMinute),
                onSelected = { time ->
                    selectedTime = time
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Espacio para empujar los botones al final


        //confirmar la hora seleccionada
        Button(
            onClick = {
                // Llamar a la función del ViewModel para establecer la notificaciion
                confirmSelectedTime(
                    context,
                    selectedTime.hour,
                    selectedTime.minute,
                    notificationViewModel
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp),

            ) {
            Text(text = stringResource(id = R.string.confirmar))
        }

        // Resetear la hora seleccionada
        Button(

            onClick = {
                selectedTime = LocalTime.of(HORAS_PREDEFINIDAS, MINUTOS_PREDEFINIDOS)
                confirmSelectedTime(
                    context,
                    selectedTime.hour,
                    selectedTime.minute,
                    notificationViewModel
                )
                //cierra el dialogo
                showTimePicker = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .padding(vertical = 8.dp),
            border = BorderStroke(0.dp, Color.Transparent), // Borde transparente
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),// Fondo transparente
        ) {
            Text(text = stringResource(R.string.resetear), color = MaterialTheme.colorScheme.primary)
        }
    }
}



// Función para confirmar la hora seleccionada
fun confirmSelectedTime(
    context: Context,
    hour: Int,
    minute: Int,
    notificationViewModel: NotificationViewModel
) {
    //guardando hora y minuto
    notificationViewModel.setHoraMinuto(hour, minute)

    // Obtener la hora y el minuto de selectedTime
    notificacionProgramada(context, hour, minute)
    val currentCalendar = Calendar.getInstance()
    val selectedCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }

    // Si la hora seleccionada ya pasó en el día actual, sumar un día
    if (selectedCalendar.before(currentCalendar)) {
        selectedCalendar.add(Calendar.DATE, 1)
    }
    val differenceInMillis = selectedCalendar.timeInMillis - currentCalendar.timeInMillis
    val seconds = differenceInMillis / 1000
    val minutes = seconds / 60
    val remainingHours = minutes / 60
    val remainingMinutes = minutes % 60

    val message =
        "Hora seleccionada: $hour:$minute\nFaltan: $remainingHours horas y $remainingMinutes minutos"
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .show()
}


fun crearCanalNotificacion(idCanal: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val nombre = "gastosDiarios"
        val descripcion = "Canal de notificaciones"
        val imortancia = NotificationManager.IMPORTANCE_DEFAULT
        val canal = NotificationChannel(idCanal, nombre, imortancia).apply {
            description = descripcion
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(canal)
    }
}

fun notificacionProgramada(
    context: Context,
    hour: Int? = null,
    minute: Int? = null
) {
    // Crear canal de notificación si aún no está creado
    crearCanalNotificacion(MY_CHANNEL_ID, context)

    // Verificar si el usuario especificó una hora y un minuto, de lo contrario, usar los predeterminados
    val finalHour = hour ?: 21
    val finalMinute = minute ?: 0
    // Configurar la alarma para la hora especificada por el usuario o para las 21:00 horas si no se especifica ninguna hora y minuto
    configurarAlarma(context, finalHour, finalMinute)
}

fun configurarAlarma(
    context: Context,
    hour: Int,
    minute: Int
) {
    val dataStorePreferences = DataStorePreferences(context)

    // Configurar la alarma para la hora y el minuto seleccionados
    val intent = Intent(context, NotificacionProgramada::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        Calendar.getInstance().timeInMillis + 10000,
        pendingIntent
    )

    Log.d("Hora seleccionada", " configuracionAlarma = $hour:$minute")
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        //
    } else {
        val dataStore = DataStorePreferences(context)
        try {
            // Configurar la alarma para que se repita todos los días a la misma hora
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (e: SecurityException) {
            // Manejar la excepción SecurityException
            e.printStackTrace()
            Log.d("alarma", "Error de seguridad al configurar la alarma")
        }
    }
}