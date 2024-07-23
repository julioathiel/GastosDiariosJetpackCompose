package com.example.gastosdiariosjetapckcompose.features.home.components_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.CurrencyAmountInputVisualTransformation
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.enums.IngresosGastosEnum
import com.example.gastosdiariosjetapckcompose.domain.model.CategoriesModel
import com.example.gastosdiariosjetapckcompose.domain.model.categoriaDefault
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesGastos
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesIngresos
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.features.registroTransaccionsPorcentaje.RegistroTransaccionesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    showDialogTransaccion: Boolean, onDissmis: () -> Unit,
    homeViewModel: HomeViewModel,
    registroTransaccionesViewModel: RegistroTransaccionesViewModel,
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
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(Spacer))

                    TextFielDinero(cantidadIngresada) { nuevoValor ->
                        cantidadIngresada = nuevoValor
                    }
                    Spacer(modifier = Modifier.padding(Spacer))
                    //DropDown para seleccionar la categoria
                    selectedCategory =
                        menuDesplegable(isChecked, Modifier.fillMaxWidth())
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

                    sharedLogic.BotonGastosIngresos(enabledBotonGastos, tipoBoton!!) { tipoClase ->
                        if (tipoClase == IngresosGastosEnum.INGRESOS) {
                            homeViewModel.setIsChecked(true)
                        } else {
                            homeViewModel.setIsChecked(false)
                        }
                    }


                    Spacer(modifier = Modifier.size(Spacer))
                    Button(
                        onClick = {
                            if (homeViewModel.fechaElegidaBarra.value == null) {
                                homeViewModel.onDialogClose()
                                navController.navigateUp()
                            } else {
                                //mandar tarea
                                homeViewModel.calculadora(cantidadIngresada)
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
                        Text(text = stringResource(R.string.guardar), fontSize = 14.sp,)
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun menuDesplegable(
    isChecked: Boolean,
    modifier: Modifier,
): CategoriesModel {
    val categories: List<CategoriesModel> by remember(isChecked) {
        mutableStateOf(
            if (isChecked) {
                categoriaDefault + categoriesIngresos.sortedBy { it.name }
            } else {
                categoriaDefault + categoriesGastos.sortedBy { it.name }
            }
        )
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(categories.first()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        Alignment.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            TextField(
                value = selectedItem.name,
                onValueChange = {},
                leadingIcon = {
                    Image(
                        painter = painterResource(id = selectedItem.icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Column {
                    categories.forEach { itemCategory ->
                        DropdownMenuItem(onClick = {
                            selectedItem = itemCategory
                            expanded = false
                        }) {
                            Row(
                                modifier = modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = itemCategory.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(
                                        24.dp
                                    ), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                                Spacer(Modifier.width(16.dp))
                                Text(
                                    text = itemCategory.name,
                                    modifier = Modifier
                                        .padding(vertical = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    return selectedItem
}

@Composable
fun FormattedCurrencyCash(homeViewModel: HomeViewModel) {
    val mostrandoDinero by homeViewModel.dineroActual.observeAsState(0.00)
    val (integerPart, decimalPart) = sharedLogic.convertidorDeTexto(mostrandoDinero)
    ObservadorDinero(integerPart, decimalPart)
}