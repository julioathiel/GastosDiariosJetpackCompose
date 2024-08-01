package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun NuevoMes(viewModel: HomeViewModel) {
    val isShowNuevoMes by viewModel.showNuevoMes

    LaunchedEffect(isShowNuevoMes) {
        if (isShowNuevoMes) {
            // Esperar 3 segundos antes de cambiar el estado a falso
            delay(3000L) // 3000 milisegundos = 3 segundos
            viewModel.setShowNuevoMes(false)
        }
    }

    if (isShowNuevoMes) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nuevo mes", textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}