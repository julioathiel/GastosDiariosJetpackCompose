package com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.actualizarMaximoFecha

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
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
        content = { padding ->
            Content(padding, actualizarMaximoFechaViewModel, scafoldState)
        }
    )
}


@Composable
fun Content(
    maxWhitd: PaddingValues,
    viewModel: ActualizarMaximoFechaViewModel,
    scafoldState: ScaffoldState
) {
    val selectedOptionState by viewModel.selectedOption.observeAsState()
    val selectedSwitchOption = viewModel.selectedSwitchOption.value
    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.padding(30.dp))
        Text(
            text = stringResource(R.string.elige_una_opcion),
            style = MaterialTheme.typography.headlineMedium
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
                withStyle(
                    style = SpanStyle(
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.grayCuatro)
                    )
                ) {
                    append(stringResource(R.string._31_d_as_para_cobros_mensuales))
                    append(stringResource(R.string._60_d_as_para_cobros_cada_dos_meses))
                    append(stringResource(R.string._90_d_as_para_cobros_cada_3_meses))
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.size(20.dp))

        var isPrueba by remember { mutableStateOf(false) }
        var selectedSwitchNumber by remember { mutableStateOf(selectedOptionState) }
        SwitchWithText(
            stringResource(R.string.primer_mes),
            numberSwitch = 31,
            selectedSwitchNumber
        ) { isActivated ->
            if (isActivated) {
                selectedSwitchNumber = 31
                isPrueba = true
            }
        }

        SwitchWithText(
            stringResource(R.string.dos_meses),
            numberSwitch = 60,
            selectedSwitchNumber,
        ) { isActivated ->
            if (isActivated) {
                selectedSwitchNumber = 60
                isPrueba = true
            }
        }

        SwitchWithText(
            stringResource(R.string.tres_meses),
            numberSwitch = 90,
            selectedSwitchNumber,
        ) { isActivated ->
            if (isActivated) {
                selectedSwitchNumber = 90
                isPrueba = true
            }
        }

        Spacer(
            modifier = Modifier
                .size(30.dp)
                .weight(1f)
        )
        Button(
            onClick = {
                if (isPrueba) {
                    viewModel.setSelectedOption(selectedSwitchNumber!!, selectedSwitchOption)
                    scope.launch {
                        scafoldState.snackbarHostState.showSnackbar("Opción guardada correctamente")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
        ) {
            Text(text = stringResource(R.string.confirmar))
        }
        Spacer(modifier = Modifier.size(30.dp))
    }
}


// Crear los switches de forma dinámica
@Composable
fun SwitchWithText(
    switchText: String,
    numberSwitch:Int,
    selectedSwitchNumber: Int?,
    isActivated: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = switchText, modifier = Modifier.weight(1f))

        Switch(
            checked = selectedSwitchNumber == numberSwitch,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    isActivated(true)
                }
            },
            modifier = Modifier.height(30.dp)
        )
    }

    Spacer(modifier = Modifier.size(20.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.size(20.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar() {
    TopAppBar(title = { Text(text = stringResource(R.string.toolbar_cambio_fecha)) })
}