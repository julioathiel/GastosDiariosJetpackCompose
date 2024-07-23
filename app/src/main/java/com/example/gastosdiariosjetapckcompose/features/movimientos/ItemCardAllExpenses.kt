package com.example.gastosdiariosjetapckcompose.features.movimientos

import android.webkit.WebSettings.TextSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel

@Composable
fun ItemCardAllExpenses(
    listTransacciones: List<MovimientosModel>,
    transaccion: MovimientosModel,
    movimientosViewModel: MovimientosViewModel
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showConfirmationEditar by remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }
    var isLongPressed by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    val textColor = if (transaccion.select) {
        //si el usuario eligio ingreso, el color de los numeros sera verde
        colorResource(id = R.color.verdeDinero)
    } else MaterialTheme.colorScheme.onSurfaceVariant//sin color

    LaunchedEffect(isClicked) {
        isClicked = false
        isLongPressed = false
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                when {
                    isLongPressed -> Color.LightGray // Color de fondo cuando se produce un clic prolongado
                    isClicked -> Color.Gray // Color de fondo cuando se produce un clic normal
                    else -> MaterialTheme.colorScheme.background
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isExpanded = !isExpanded
                }, onLongPress = {
                    // Manejar el clic prolongado
                    isClicked = true
                    isLongPressed = true
                    showConfirmationDialog = true

                })
            }
    ) {

        Spacer(modifier = Modifier.padding(start = 16.dp))

        //contenedor de icono
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Icono dentro del círculo
            Image(
                painter = painterResource(id = transaccion.iconResourceName.toInt()),
                contentDescription = null,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }

        Spacer(modifier = Modifier.padding(start = 16.dp))
        //contenedor de titulo y subtitulo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaccion.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    // Mostrar "..." si el texto excede una línea
                    overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                )
                if (transaccion.subTitle.isNotEmpty()) {
                    Text(
                        text = transaccion.subTitle,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.grayCuatro),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        // Mostrar "..." si el texto excede una línea
                        overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                    )
                }
            }
            //contenedor de dinero y fecha
            Column(Modifier.padding(end = 8.dp), horizontalAlignment = Alignment.End) {
                Text(
                    text = sharedLogic.formattedCurrency(transaccion.cash.toDouble()),
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        //espaco entre cada item
        Spacer(modifier = Modifier.padding(36.dp))

        if (showConfirmationDialog) {
            sharedLogic.EditDeleteAlertDialog(
                onEditClick = {
                    showConfirmationEditar = true
                },
                onDeleteClick = {
                    movimientosViewModel.onItemRemoveMov(
                        listTransacciones,
                        transaccion
                    )
                },
                onDismiss = { showConfirmationDialog = false }
            )

        }
        //si presiona editar se abira otro dialogo para editar
        if (showConfirmationEditar) {
            sharedLogic.AddTransactionDialog(
                transaccion,
                showDialogTransaccion = showConfirmationEditar,
                onDissmis = {
                    showConfirmationEditar = false
                    showConfirmationDialog = false
                },
                movimientosViewModel = movimientosViewModel,
                listTransacciones
            )
        }
    }
}