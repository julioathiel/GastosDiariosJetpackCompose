package com.example.gastosdiariosjetapckcompose.features.configuration.actualizarMaximoFecha

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.R
import kotlinx.coroutines.launch

@Composable
fun ActualizarMaximoFechaScreen(
    NavController: NavController,
    actualizarMaximoFechaViewModel: ActualizarMaximoFechaViewModel
) {
    val scafoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scafoldState,
        topBar = { Toolbar() },
        content = { myPadding ->
            Content(myPadding, actualizarMaximoFechaViewModel, scafoldState)
        }
    )
}


@Composable
fun Content(
    myPadding: PaddingValues,
    actualizarMaximoFechaViewModel: ActualizarMaximoFechaViewModel,
    scafoldState: ScaffoldState
) {
    val selectedOptionState by actualizarMaximoFechaViewModel.selectedOption.observeAsState()
    val selectedSwitchOption = actualizarMaximoFechaViewModel.selectedSwitchOption.value


    val scope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = "Elige una opcion",
            fontFamily = FontFamily(Font(R.font.lato_bold)),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 24.sp
                    )
                ) {
                }
                withStyle(style = SpanStyle(fontSize = 16.sp)) {
                    append("Never miss a product drop.\n")
                    append("Set up your default Notify Me product\n")
                    append("release reminders.")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.size(20.dp))

        SwitchWithText(
            "31 dias",
            31,
            selectedOptionState,
            selectedSwitchOption,
            actualizarMaximoFechaViewModel
        )

        SwitchWithText(
            "60 dias",
            60,
            selectedOptionState,
            selectedSwitchOption,
            actualizarMaximoFechaViewModel
        )

        SwitchWithText(
            "90 dias",
            90,
            selectedOptionState,
            selectedSwitchOption,
            actualizarMaximoFechaViewModel
        )

        Spacer(modifier = Modifier.size(30.dp))
        Text(
            text = "You can manage your app notification",
            fontFamily = FontFamily.SansSerif,
            color = Color.Gray,
            fontSize = 16.sp
        )
        Text(
            text = "permissions in your Phone settings.",
            fontFamily = FontFamily.SansSerif,
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                selectedOptionState?.let {
                    actualizarMaximoFechaViewModel.setSelectedOption(
                        it,
                        selectedSwitchOption
                    )
                }
                scope.launch {
                    scafoldState.snackbarHostState.showSnackbar("Opción guardada correctamente")
                }
            },
            // Resto de la configuración del botón
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blue)),
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Confirm",
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.size(30.dp))
    }
}

// Función para manejar el cambio de estado de los switches
fun onSwitchCheckedChange(
    switchNumber: Int,
    isChecked: Boolean,
    actualizarMaximoFechaViewModel: ActualizarMaximoFechaViewModel
) {
    if (isChecked) {
        actualizarMaximoFechaViewModel.updateSelectedSwitchOption(isChecked)
        actualizarMaximoFechaViewModel.updateSelectedOption(switchNumber)
    } else {
        actualizarMaximoFechaViewModel.updateSelectedSwitchOption(isChecked)
        actualizarMaximoFechaViewModel.updateSelectedOption(null)
    }
}

// Crear los switches de forma dinámica
@Composable
fun SwitchWithText(
    switchText: String,
    switchNumber: Int,
    selectedOption: Int?,
    selectedSwitchOption: Boolean,
    actualizarMaximoFechaViewModel: ActualizarMaximoFechaViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = switchText, modifier = Modifier.weight(1f),
            fontFamily = FontFamily(Font(R.font.lato_bold)),
            fontSize = 16.sp
        )
        Switch(
            checked = selectedOption == switchNumber && selectedSwitchOption,
            onCheckedChange = { isChecked ->
                onSwitchCheckedChange(switchNumber, isChecked, actualizarMaximoFechaViewModel)
            },
            modifier = Modifier.height(30.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(id = R.color.blue), // Cambiar el color de fondo cuando está seleccionado (en este caso, rojo)
                checkedTrackColor = colorResource(id = R.color.fondoCelesteblue)
            )
        )
    }
    Spacer(modifier = Modifier.size(20.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.size(20.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.toolbar_cambio_fecha),
                color = colorResource(id = R.color.black)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}