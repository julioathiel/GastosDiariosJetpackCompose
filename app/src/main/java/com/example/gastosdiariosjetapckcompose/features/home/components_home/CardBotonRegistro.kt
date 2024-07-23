package com.example.gastosdiariosjetapckcompose.features.home.components_home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.features.home.AnimatedProgressBar
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel

@Composable
fun CardBotonRegistro(
    homeViewModel: HomeViewModel
) {
    val totalIngresosProgress by homeViewModel.mostrandoDineroTotalIngresos.observeAsState()
    val totalGastosProgress by homeViewModel.mostrandoDineroTotalGastos.observeAsState()

    val progresoRelativo =
        sharedLogic.calcularProgresoRelativo(totalIngresosProgress, totalGastosProgress)
    val porcentaje = sharedLogic.formattedPorcentaje(progresoRelativo)


    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surfaceContainerLow
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = Color.Unspecified)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "$porcentaje%",
                fontSize = 12.sp
            )

            AnimatedProgressBar(
                progress = progresoRelativo,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
         //   HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp).padding(8.dp),
               horizontalArrangement = Arrangement.End
            ) {

                Text(
                    text = sharedLogic.formattedCurrency(totalGastosProgress),
                    color = colorResource(id = R.color.rojoDinero),
                    style = MaterialTheme.typography.titleMedium
                )
               Text(text = "/", modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    text = sharedLogic.formattedCurrency(totalIngresosProgress),

                    color = colorResource(id = R.color.verdeDinero),
                    style = MaterialTheme.typography.titleMedium
                )

            }
        }
    }
}