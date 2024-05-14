package com.example.gastosdiariosjetapckcompose.features.recordatorio

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.Constants.MY_CHANNEL_ID
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.features.recordatorio.NotificacionProgramada.Companion.NOTIFICATION_ID
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import okhttp3.internal.wait
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale
import kotlin.time.Duration.Companion.hours


@Composable
fun RecordatorioScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    // State for selected time
    var selectedTime by remember { mutableStateOf(LocalTime.of(21, 0)) }
    // State for showing time picker dialog
    var showTimePicker by remember { mutableStateOf(false) }

    BackHandler {
        //al presion el boton fisico de retoceso, se dirige a la pantalla de configuracion
        navController.navigate(Routes.ConfigurationScreen.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_notifications),
            contentDescription = null,
            tint = colorResource(id = R.color.fondoCelesteblue),
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.recordatorio_titulo),
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.black),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.recordatorio_subTitulo),
            fontSize = 14.sp,
            color = colorResource(id = R.color.black),
            modifier = Modifier.padding(vertical = 4.dp)
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
            border = BorderStroke(0.dp, colorResource(id = R.color.blue)), // Borde transparente
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.fondoCelesteblue)),// Fondo transparente
            onClick = { showTimePicker = true }
        ) {
            Text(text = "$selectedTime",color = colorResource(id = R.color.blue))
        }
        if (showTimePicker) {
            TimePickerSample(
                horaPredefinida = selectedTime,
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
               confirmSelectedTime(context, selectedTime.hour, selectedTime.minute)
//
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.fondoCelesteblue)
            )

        ) {
            Text(text = "Confirmar", color = colorResource(id = R.color.blue))
        }

        // Resetear la hora seleccionada
        Button(
            onClick = {
                showTimePicker = false
                selectedTime = LocalTime.of(21, 0)
                confirmSelectedTime(
                    context,
                    selectedTime.hour,
                    selectedTime.minute
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .padding(vertical = 8.dp),
            border = BorderStroke(0.dp, Color.Transparent), // Borde transparente
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),// Fondo transparente
        ) {
            Text(text = "Resetear", color = colorResource(id = R.color.blue))
        }
    }
}

// Función para confirmar la hora seleccionada
fun confirmSelectedTime(
    context: Context,
    hour: Int,
    minute: Int,

    ) {
    // Obtener la hora y el minuto de selectedTime
    notificacionProgramada(context, hour, minute)
    Toast.makeText(context, "Hora seleccionada: ${hour}:${minute}", Toast.LENGTH_SHORT)
        .show()
}

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
                Log.d("horaPredefinida", "defaultTime = $selectedTime")
                onDismiss()
            },
        ) {
            TimeInput(state = state)
        }
    }
}


@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
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

fun notificacionProgramada(context: Context, hour: Int? = null, minute: Int? = null) {
    // Crear canal de notificación si aún no está creado
    crearCanalNotificacion(MY_CHANNEL_ID, context)

    // Configurar la hora y el minuto predeterminados
    val defaultHour = 21
    val defaultMinute = 0

    // Verificar si el usuario especificó una hora y un minuto, de lo contrario, usar los predeterminados
    val finalHour = hour ?: defaultHour
    val finalMinute = minute ?: defaultMinute

    // Configurar la alarma para la hora especificada por el usuario o para las 21:00 horas si no se especifica ninguna hora y minuto
    configurarAlarma(context, finalHour, finalMinute)
}

fun configurarAlarma(context: Context, hour: Int, minute: Int) {

    val intent = Intent(context, NotificacionProgramada::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    Log.d("Hora seleccionada", "$hour:$minute")
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        //
    } else {
        try {
            // Configurar la alarma para que se repita todos los días a la misma hora
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            Log.d("alarma", "Alarma configurada para ${calendar.timeInMillis}")
        } catch (e: SecurityException) {
            // Manejar la excepción SecurityException
            e.printStackTrace()
            Log.d("alarma", "Error de seguridad al configurar la alarma")
        }
    }
}

fun notificacionBasica(
    context: Context,
    idCanal: String,
    idNotificacion: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    crearCanalNotificacion(idCanal, context)

    val builder = NotificationCompat.Builder(context, idCanal)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setPriority(priority)
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notify(idNotificacion, builder.build())
    }

}

//de aca hasta el final datepickerdialog con restriccion de fechas
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerDialogWithRestriction() {
//    val context = LocalContext.current
//    val datePickerState = rememberDatePickerState(
//        selectableDates = createSelectableDates()
//    )
//    var onDismiss by remember { mutableStateOf(false) }
//
//    Column {
//        Button(onClick = {
//            onDismiss = true
//        }) {
//            Text("abrir datePicker")
//        }
//        var fechaSeleccionada by remember { mutableStateOf("") }
//        if (onDismiss) {
//            showDatePickerDialog(
//                onDismiss = { onDismiss = false },
//                onSelectedDate = { dateString ->
//                    fechaSeleccionada = dateString
//                },
//                datePickerState = datePickerState
//            )
//            Log.d("fs", "$fechaSeleccionada")
//        }
//        Text(
//            "fecha seleccionada: ${fechaSeleccionada ?: "no as seleccionado aun"}",
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun showDatePickerDialog(
//    onDismiss: () -> Unit,
//    onSelectedDate: (String) -> Unit,
//    datePickerState: DatePickerState
//) {
//    val state = remember(datePickerState) { datePickerState }
//
//    // Función para obtener la fecha seleccionada en formato String
//    val getSelectedDate = {
//        val dateInMillis = state.selectedDateMillis
//        dateInMillis?.let {
//            val fechaSelected = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
//
//            val dateString = "${String.format("%02d", fechaSelected.dayOfMonth)}/${
//                String.format("%02d", fechaSelected.monthValue)
//            }/${fechaSelected.year}"
//            onSelectedDate(dateString)
//        }
//    }
//
//    DatePickerDialog(
//        onDismissRequest = { onDismiss() },
//        confirmButton = {
//            Button(
//                onClick = {
//                    getSelectedDate()
//                    onDismiss()
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
//            ) {
//                Text(text = "OK", color = Color.Blue) // Cambia el color a tu preferencia
//            }
//        },
//        dismissButton = {
//            Button(
//                onClick = { onDismiss() },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
//            ) {
//                Text(text = "CANCEL", color = Color.Red) // Cambia el color a tu preferencia
//            }
//        }
//    ) {
//        DatePicker(state = state)
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//private fun createSelectableDates(): SelectableDates {
//    return object : SelectableDates {
//        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
//            val currentDate = LocalDate.now()
//            val selectedDate =
//                Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC")).toLocalDate()
//            val maxSelectableDate =
//                currentDate.plusDays(31) // Fecha máxima seleccionable: 31 días en el futuro
//            return selectedDate in currentDate..maxSelectableDate
//            //   return selectedDate >= currentDate
//        }
//
//        override fun isSelectableYear(year: Int): Boolean {
//            return year > 2023 && year < 2025
//        }
//    }
//}