package com.example.gastosdiariosjetapckcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel

class SharedLogic {
    var _dineroActual = MutableLiveData<Double>()
    var dineroActual: LiveData<Double> = _dineroActual

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
                Card(shape = MaterialTheme.shapes.small) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        Spacer(modifier = Modifier.padding(Spacer))
                        Text(
                            text = "Añade el monto",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        TextFielDinero(cantidadIngresada) { nuevoValor ->
                            cantidadIngresada = nuevoValor
                            //  homeViewModel.setCantidadIngresada(nuevoValor = cantidadIngresada)
                        }
                        Spacer(modifier = Modifier.padding(Spacer))

                        Spacer(modifier = Modifier.padding(Spacer))
                        //Description
                        TextFieldDescription(
                            description = description
                        ) { newDescription ->
                            description = newDescription
                        }
                        Spacer(modifier = Modifier.size(30.dp))

                        Spacer(modifier = Modifier.size(Spacer))

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
                                .padding(Spacer),
                            enabled = cantidadIngresada.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8247C5)
                            )
                        ) {
                            Text(text = "Guardar")
                        }

                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextFielDinero(
        cantidadIngresada: String,
        onTextChanged: (String) -> Unit
    ) {
        TextField(
            value = cantidadIngresada,
            onValueChange = { onTextChanged(it) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
            label = { Text(text = "Descripcion", color = Color.LightGray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            //para mostrar la primer letra de la palabra en mayuscula
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )
    }
}