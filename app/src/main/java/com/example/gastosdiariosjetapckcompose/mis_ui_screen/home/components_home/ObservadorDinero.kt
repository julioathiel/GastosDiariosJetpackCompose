package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ObservadorDinero(temporal: CharSequence, decimalPart: String) {
//    var dinero by remember { mutableIntStateOf(10) }
//
//    LaunchedEffect(dinero) {
//        val targetValue = 5
//
//        if (dinero > targetValue) {
//            for (i in dinero downTo targetValue) {
//                dinero = i
//                delay(50) // Añade un retraso para simular la animación
//            }
//        }
//    }
//    Text(
//        text = "Dinero: $dinero",
//        style = MaterialTheme.typography.titleMedium,
//        modifier = Modifier.padding(16.dp)
//    )
    Row(verticalAlignment = Alignment.Bottom) {

        Text(
            text = "$",
            modifier = Modifier.offset(y = (-10).dp),
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = temporal.toString(),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.offset(x = (-10).dp)
        )

        Text(
            text = decimalPart,
            //texto que modifica los decimales en la posicion hacia arriba y se ven pequeños
            modifier = Modifier.offset(y = (-20).dp, x = (-10).dp),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}