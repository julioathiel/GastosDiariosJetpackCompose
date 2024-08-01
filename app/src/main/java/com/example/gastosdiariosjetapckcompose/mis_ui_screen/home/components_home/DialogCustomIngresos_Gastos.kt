package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.data.core.CurrencyAmountInputVisualTransformation
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.enums.IngresosGastosEnum
import com.example.gastosdiariosjetapckcompose.domain.model.CategoriesModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    modifier: Modifier,
    showDialogTransaccion: Boolean, onDissmis: () -> Unit,
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val isChecked by homeViewModel.isChecked
    val enabledBotonGastos by homeViewModel.enabledBotonGastos
    val tipoBoton by homeViewModel.botonIngresosActivado.observeAsState()
    var cantidadIngresada by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory: CategoriesModel
    val Spacer = 16.dp

    if (showDialogTransaccion) {
        //onDismissRequest = { onDissmis() } funcion para que se descarte el dialogo y desaparezca
        Dialog(onDismissRequest = { onDissmis() }) {
            Card(shape = RoundedCornerShape(30.dp)) {
                Column(
                    modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(Spacer))

                    TextFielDinero(cantidadIngresada) { nuevoValor ->
                        cantidadIngresada = nuevoValor
                    }
                    Spacer(modifier = Modifier.padding(Spacer))

                    //DropDown para seleccionar la categoria
                    selectedCategory = menuDesplegable(isChecked, modifier)

                    Log.d("tipBoton", "selectedCategory = $selectedCategory")
                    Spacer(modifier = Modifier.padding(Spacer))
                    Text(
                        text = stringResource(R.string.descripcion),
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    //Description
                    TextFieldDescription(description = description) { newDescription ->
                        description = newDescription
                    }
                    Spacer(modifier = Modifier.size(30.dp))

                    sharedLogic.BotonGastosIngresos(
                        homeViewModel = homeViewModel,
                        enabledBotonGastos = enabledBotonGastos,
                        botonActivado = tipoBoton!!,
                        onTipoSeleccionado = { tipClass ->
                            if (tipClass == IngresosGastosEnum.INGRESOS) {
                                homeViewModel.setIsChecked(true)
                            } else {
                                homeViewModel.setIsChecked(false)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.size(Spacer))

                    Button(
                        onClick = {
                            if (homeViewModel.fechaElegidaBarra.value == null) {
                                homeViewModel.onDialogClose()
                                navController.navigateUp()
                            } else {
                                //mandar tarea
                                homeViewModel.cantidadIngresada(cantidadIngresada, isChecked)
                                //creando una transaccion
                                homeViewModel.crearTransaccion(
                                    cantidadIngresada,
                                    selectedCategory.name,
                                    description,
                                    selectedCategory.icon,
                                    isChecked
                                )
                                //si la seleccion del usuario es gastos entonces se crea el registro de gastos individuales
                                if (!isChecked) {
                                    //creando categoria individual
                                    homeViewModel.crearNuevaCategoriaDeGastos(
                                        selectedCategory.name,
                                        selectedCategory.icon,
                                        cantidadIngresada
                                    )
                                }
//                               registroTransaccionesViewModel.getDatosGastos()
                                cantidadIngresada = ""
                                description = ""
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        enabled = cantidadIngresada.isNotEmpty() && selectedCategory.name != stringResource(
                            R.string.elige_una_categoria
                        )
                    ) {
                        Text(text = stringResource(R.string.guardar), fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.size(Spacer))
                }
            }
        }
    }
}

@Composable
fun TextFielDinero(cantidadIngresada: String, onTextChanged: (String) -> Unit) {
    val maxLength = 10

    TextField(
        value = cantidadIngresada.take(maxLength),
        onValueChange = { it ->
            if (it.length <= maxLength) {
                onTextChanged(it)
            }
        },
        singleLine = true,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    stringResource(R.string._0_00),
                    color = colorResource(id = R.color.grayTres),
                    fontSize = 40.sp
                )
            }

        },
        visualTransformation = CurrencyAmountInputVisualTransformation(
            fixedCursorAtTheEnd = true
        ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
fun TextFieldDescription(description: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = description,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        //para mostrar la primer letra de la palabra en mayuscula
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
    )
}

@Composable
fun FormattedCurrencyCash(homeViewModel: HomeViewModel) {
    val mostrandoDinero by homeViewModel.dineroActual.observeAsState(0.00)
    val (integerPart, decimalPart) = sharedLogic.convertidorDeTexto(mostrandoDinero)
    ObservadorDinero(integerPart, decimalPart)
}