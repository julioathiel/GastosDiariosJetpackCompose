package com.example.gastosdiariosjetapckcompose.features.notificacion

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.data.core.Constants.HORAS_PREDEFINIDAS
import com.example.gastosdiariosjetapckcompose.data.core.Constants.MINUTOS_PREDEFINIDOS
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.features.notificacion.components.TimePickerSample
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import java.time.LocalTime


@Composable
fun RecordatorioScreen(
    navController: NavHostController,
    viewModel: NotificationViewModel
) {
    val context = LocalContext.current

    val selectedTimeHour by viewModel.selectedHour.collectAsState()
    val selectedTimeMinute by viewModel.selectedMinute.collectAsState()
    // Crear un LocalTime a partir de los valores observados
    var selectedTime by remember {
        mutableStateOf(
            LocalTime.of(
                selectedTimeHour,
                selectedTimeMinute
            )
        )
    }
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
                viewModel.confirmSelectedTime(
                    context,
                    selectedTime.hour,
                    selectedTime.minute
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
                viewModel.confirmSelectedTime(
                    context,
                    selectedTime.hour,
                    selectedTime.minute
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
            Text(
                text = stringResource(R.string.resetear),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

