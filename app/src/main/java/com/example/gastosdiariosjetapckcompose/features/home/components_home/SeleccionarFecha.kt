package com.example.gastosdiariosjetapckcompose.features.home.components_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel

@Composable
fun CountDate(modifier: Modifier, homeViewModel: HomeViewModel) {
    //observando el estado
    val diasRestantes by sharedLogic.diasRestantes.collectAsState()
    //val fechaElegida = homeViewModel.fechaElegidaBarra.observeAsState().value ?: ""
    val fechaElegida by homeViewModel.fechaElegidaBarra.collectAsState()

    val textDia = if (diasRestantes == 1) "día" else "días"
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card {
                Column(
                    Modifier
                        .width(50.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //muestra cuantos dias faltan desde la fecha elegida a la fecha actual
                    Text(
                        text = diasRestantes.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = textDia,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }
            fechaElegida?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f)
                )
            }

            MyDatePickerDialog(homeViewModel = homeViewModel)
        }
    }
}


