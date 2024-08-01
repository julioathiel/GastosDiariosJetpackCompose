package com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ajustes_avanzados

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.domain.uiState.EventHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(viewModel: AjustesViewModel, navController: NavController) {


   Scaffold(topBar = { TopAppBar(title = { Text(text = "Ajustes") }) }) { it: PaddingValues ->
        Column(modifier = Modifier.padding(it)) {
            // Recolecta el estado del flujo de booleanos
            val state = viewModel.state
            SwitchItemDarkTheme(
                onCheked = state.isDarkModeActive,
                onCheckedChanged = { viewModel.onEventHandler(EventHandler.SelectedDarkThemeValue(it)) },
                saveDarkThemeValue = {  viewModel.onEventHandler(EventHandler.SaveDarkThemeValue(it))}
            )
        }
   }
}

@Composable
fun SwitchItemDarkTheme(
    onCheked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    saveDarkThemeValue: (Boolean) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onCheckedChanged(!onCheked) }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Modo oscuro")
        Switch(
            checked = onCheked,
            onCheckedChange = {
                onCheckedChanged(it)
                saveDarkThemeValue(it)
            },
            thumbContent = {
                if (onCheked) {
                    // Ícono cuando el modo oscuro está activado
                    Icon(
                        Icons.Filled.Nightlight,
                        contentDescription = "Modo Oscuro Activado"
                    )
                } else {
                    // Ícono cuando el modo oscuro está desactivado
                    Icon(
                        Icons.Filled.WbSunny,
                        contentDescription = "Modo Oscuro Desactivado",
                        tint = Color.Gray
                    )
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Transparent,
                uncheckedThumbColor = Color.Transparent
            )
        )
    }
}
