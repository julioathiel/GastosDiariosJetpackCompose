package com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.components_stadistics

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.BarGraph
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.BarGraphConfig
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.BarType
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.build
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.data
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.dataBar
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.dataToLabel
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.dataToMonth
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.dataToValue
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.etiqueta
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.etiquetaContainer
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.etiquetaEjeX
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.etiquetaEjeY
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.etiquetaTranslateY
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.height
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.lineaBase
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.lineaVertical
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.lineasEjeGraph
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.roundType
import com.example.gastosdiariosjetapckcompose.bar_graph_custom.viewBarText
import com.example.gastosdiariosjetapckcompose.domain.model.BarDataModel
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel

@Composable
fun BarGrahpConfigCustom(regTransaccionesViewModel: RegistroTransaccionesViewModel) {
    val listBarGraph by regTransaccionesViewModel.listBarDataModel.collectAsState()

    val config = BarGraphConfig<BarDataModel>()
        .data(listBarGraph)
        .dataBar(
            selectedBarColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unSelectedBarColor = MaterialTheme.colorScheme.secondaryContainer,
            width = 60.dp,
            espacioEntreBarras = 8.dp
        )
        .height(altura = 200.dp)
        .etiqueta(textSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        .etiquetaContainer(
            visible = true,
            color = MaterialTheme.colorScheme.surfaceVariant,
            cornerRadius = 30f
        )
        .etiquetaTranslateY(y = -5f)
        .roundType(roundType = BarType.TOP_CURVED)
        .viewBarText(
            textSize = 20.sp,
            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            cornerRadius = 100f,
            paddingBottom = 16.dp,
            heigth = 80.dp
        )
        .lineasEjeGraph(
            visible = true,
            color = MaterialTheme.colorScheme.surfaceVariant,
            size = 1.dp,
            cantidadLineas = 5
        )
        .etiquetaEjeY(visible = true, color = MaterialTheme.colorScheme.onSurfaceVariant)
        .lineaBase(visible = true, color = MaterialTheme.colorScheme.surfaceVariant)
        .etiquetaEjeX(
            textSixe = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            rotate = 0f
        )
        .dataToValue { it.value }
        .dataToLabel { it.money }
        .dataToMonth { it.month }
        .lineaVertical(
            lineaVerticalesVisible = true,
            color = MaterialTheme.colorScheme.surfaceVariant,
            witdh = 1.dp,
            heigth = 4.dp
        )
        .build()
    BarGraph(config = config)
}