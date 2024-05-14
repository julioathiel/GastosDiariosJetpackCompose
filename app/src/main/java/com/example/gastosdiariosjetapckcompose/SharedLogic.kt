package com.example.gastosdiariosjetapckcompose

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

enum class ClaseEnum {
    GASTOS,
    INGRESOS
}

class SharedLogic {
    var _dineroActual = MutableLiveData<Double>()
    var dineroActual: LiveData<Double> = _dineroActual

    fun formattedCurrency(amount: Double?): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
        val formattedAmount = if (amount != null) {
            currencyFormat.format(amount)
        } else {
            currencyFormat.format(0.0)
        }
        val currencySymbol = currencyFormat.currency?.symbol ?: "$"
        return formattedAmount.replace(currencySymbol, "").trim()
    }

    fun agregarDineroAlTotal(nuevoValor: Double, valorExistente: Double): Double {
        val resultado = BigDecimal(valorExistente + nuevoValor).setScale(2, RoundingMode.HALF_EVEN)
        return resultado.toDouble()
    }

    fun calcularProgresoRelativo(totalIngresos: Double?, totalGastos: Double?): Double {
        val ingresosTotales = totalIngresos ?: 0.0
        val gastosTotales = totalGastos ?: 0.0
        if (ingresosTotales > 0) {
            return gastosTotales / ingresosTotales
        }
        return 0.0
    }

    fun formateandoPorcentaje(progresoRelativo: Double): String {
        return String.format("%.1f", progresoRelativo * 100)
    }

    fun calcularLimitePorDia(diasRestantes: Int?, dineroActual: Double): Double {
        return if (diasRestantes != null && dineroActual != null && diasRestantes > 0) {
            dineroActual / diasRestantes.toDouble()
        } else {
            0.0
        }
    }

    fun convertidorDeTexto(dineroAConvertir: Double): Pair<String, String> {
        val formattedAmount = formattedCurrency(dineroAConvertir)
        // formattedAmount devuelve $667.747,98
        val dinero = formattedAmount.substringBeforeLast(",").replace("$", "")
        var centavos = ""

        val separadorDecimal = if (formattedAmount.contains(".")) "." else ","
        //el decimal de centavos sera despues de la ,
        centavos = if (separadorDecimal == ",") {
            formattedAmount.substringAfterLast(".", "").take(2)
        } else {
            formattedAmount.substringAfterLast(",", "").take(2)
        }

        return Pair(dinero, centavos)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddTransactionDialog(
        transaccion: MovimientosModel,
        showDialogTransaccion: Boolean, onDissmis: () -> Unit,
        movimientosViewModel: MovimientosViewModel,
        listTransacciones: List<MovimientosModel>
    ) {
        var cantidadIngresada by remember { mutableStateOf(transaccion.cash) }
        var description by remember { mutableStateOf(transaccion.subTitle) }

        val Spacer = 16.dp

        if (showDialogTransaccion) {
            //onDismissRequest = { onDissmis() } funcion para que se descarte el dialogo y desaparezca
            Dialog(onDismissRequest = { onDissmis() }) {
                Card(shape = RoundedCornerShape(30.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        Spacer(modifier = Modifier.padding(Spacer))

                        TextFielDineroMovimientos(cantidadIngresada) { nuevoValor ->
                            cantidadIngresada = nuevoValor
                        }
                        Spacer(modifier = Modifier.padding(Spacer))
                        Text(
                            text = "Descripción",
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.lato_bold)),
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 16.dp)
                        )
                        //Description
                        TextFieldDescription(
                            description = description
                        ) { newDescription ->
                            description = newDescription
                        }
                        Spacer(modifier = Modifier.size(30.dp))

                        Button(
                            onClick = {
                                // Actualizar dinero y descripción
                                movimientosViewModel.actualizandoItem(
                                    cantidadIngresada,
                                    description,
                                    transaccion
                                )
                                onDissmis()
                                cantidadIngresada = ""
                                description = ""
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp),
                            enabled = cantidadIngresada.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.blue)
                            )
                        ) {
                            Text(text = "Guardar")
                        }
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun TextFielDineroMovimientos(
        cantidadIngresada: String,
        onTextChanged: (String) -> Unit
    ) {
        TextField(
            value = cantidadIngresada,
            onValueChange = { onTextChanged(it) },
            visualTransformation = CurrencyAmountInputVisualTransformation(
                fixedCursorAtTheEnd = true
            ), placeholder = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        "$0.00",
                        color = colorResource(id = R.color.grayTres),
                        fontSize = 40.sp
                    )
                }

            },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 40.sp)
        )
    }

    @ExperimentalMaterial3Api
    @Composable
    fun TextFieldDescription(
        description: String,
        onTextChanged: (String) -> Unit
    ) {
        TextField(
            value = description,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = colorResource(id = R.color.grayUno)
            ),
            //para mostrar la primer letra de la palabra en mayuscula
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )
    }

    @Composable
    fun IsEmpty() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.white)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.padding(top = 100.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_no_data),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    text = stringResource(R.string.sin_resultados),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 30.dp)
                )

                Text(
                    text = stringResource(R.string.body_vacio),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.grayCuatro),
                    modifier = Modifier.padding(16.dp)
                )
            }

        }
    }

    @Composable
    fun BotonGastosIngresos(enabledBotonGastos:Boolean,botonActivado:Int,onTipoSeleccionado: (ClaseEnum) -> Unit) {
        var selectedIndex by remember { mutableStateOf(botonActivado) }
        val options = listOf(ClaseEnum.GASTOS, ClaseEnum.INGRESOS)
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex
                val color = if (isSelected) {
                    when (label) {
                        ClaseEnum.GASTOS -> Color(0xFFFFEBEE)
                        ClaseEnum.INGRESOS -> colorResource(id = R.color.fondoVerdeDinero)
                        else -> Color.Transparent
                    }
                } else {
                    Color.Transparent
                }

                val colorText = if (isSelected) {
                    when (label) {
                        ClaseEnum.GASTOS -> colorResource(id = R.color.rojoDinero)
                        ClaseEnum.INGRESOS -> colorResource(id = R.color.verdeDinero)
                        else -> Color.Transparent
                    }
                } else {
                    Color.Transparent
                }
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = {
                        selectedIndex = index
                        onTipoSeleccionado(options[index])
                    },
                    selected = index == selectedIndex,
                    colors = SegmentedButtonDefaults.colors(activeContainerColor = color),
                    border = BorderStroke(color = colorText, width = 0.dp),
                    // Deshabilitar el botón de gastos si enabledBotonGastos es falso o habilitar todoo si esta en true
                    enabled = enabledBotonGastos || label != ClaseEnum.GASTOS
                ) {
                    Text(
                        label.toString(),
                        color = if (isSelected) colorText else Color.Black
                    )
                }
            }
        }
    }
}




