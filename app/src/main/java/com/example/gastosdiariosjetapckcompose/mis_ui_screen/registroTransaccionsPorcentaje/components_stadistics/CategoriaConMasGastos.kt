package com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.components_stadistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.GastosPorCategoriaModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel

@Composable
fun CategoriaConMasGastos(
    listTransacciones: List<GastosPorCategoriaModel>,
    vm: RegistroTransaccionesViewModel
) {
    val porcentaje: Int? by vm.porcentajeGasto.collectAsState()

    // obteniendo el maximo total  gastado
    val category: GastosPorCategoriaModel = listTransacciones.maxBy {
        it.totalGastado.toDouble()
    }
    val form = category.totalGastado.toDouble()

    //muestra al usuario el total como $ 30.000,00
    val totalGastado = sharedLogic.formattedCurrency(form)


    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.lo_mas_gastado_este_mes),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {

                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = totalGastado,
                    style = MaterialTheme.typography.labelLarge,
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Text(
            text = "$porcentaje%",
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.headlineSmall,
        )
    }

}