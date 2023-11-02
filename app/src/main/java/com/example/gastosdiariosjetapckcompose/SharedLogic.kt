package com.example.gastosdiariosjetapckcompose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.gastosdiariosjetapckcompose.domain.model.Category
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesGastos
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesIngresos
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel

class SharedLogic {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddTransactionDialog(
        showDialogTransaccion: Boolean, onDissmis: () -> Unit,
        movimientosViewModel: MovimientosViewModel,
        homeViewModel: HomeViewModel
    ) {
        val isChecked: Boolean by homeViewModel.isChecked.observeAsState(initial = homeViewModel.dineroActual.value == 0.0)
        var cantidadIngresada by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var selectedCategory: Category
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
                        //DropDown para seleccionar la categoria
                        selectedCategory = DropDownWitchIcon(isChecked, Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.padding(Spacer))
                        //Description
                        TextFieldDescription(description = description) { newDescription ->
                            description = newDescription
                        }
                        Spacer(modifier = Modifier.size(30.dp))

                        ButtonToggleGroup(homeViewModel, isChecked) { homeViewModel.setIsChecked(it) }

                        Spacer(modifier = Modifier.size(Spacer))

                        Button(
                            onClick = {
                                if (homeViewModel.fechaElegida.value == "Selected date") {
                                    homeViewModel.onDialogClose()
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
                                    cantidadIngresada = ""
                                    description = ""
                                }
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
    fun TextFielDinero(cantidadIngresada: String, onTextChanged: (String) -> Unit) {
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
    fun TextFieldDescription(description: String, onTextChanged: (String) -> Unit) {
        TextField(
            value = description,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true,
            maxLines = 1,
            label = { Text(text = "Descripcion (Opcional)", color = Color.LightGray) },
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

    @Composable
    fun ButtonToggleGroup(
        homeViewModel: HomeViewModel, isSelected: Boolean, onOptionSelect: (Boolean) -> Unit
    ) {
        ButtonToggleGroupSelect(
            homeViewModel = homeViewModel, isChecked = isSelected, onToggle = onOptionSelect
        )
        // Llama a onOptionSelect con isSelected para indicar si está seleccionado
        onOptionSelect(isSelected)
    }

    @Composable
    fun ButtonToggleGroupSelect(
        homeViewModel: HomeViewModel,
        isChecked: Boolean,
        onToggle: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(modifier = modifier.padding(16.dp)) {
            val options = listOf("Gasto", "Ingreso")
            val isDineroActualZero = homeViewModel.dineroActual.value!! == 0.0
            options.forEachIndexed { index, option ->
                val selected = isChecked == (index == 1)
                if (isDineroActualZero && index == 1) {
                    // Si el saldo es 0.0, seleccionar automáticamente "Ingreso"
                    onToggle(true)
                }
                val shape = when (index) { // 6
                    0 -> RoundedCornerShape(
                        topStart = 4.dp,
                        bottomStart = 4.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )

                    options.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp, bottomStart = 0.dp,
                        topEnd = 4.dp,
                        bottomEnd = 4.dp
                    )

                    else -> CutCornerShape(0.dp)
                }
                val zIndex = if (selected) 1f else 0f
                val buttonModifier = when (index) { // 7
                    0 -> Modifier.zIndex(zIndex)
                    else -> {
                        val offset = -1 * index
                        Modifier
                            .offset(x = offset.dp)
                            .zIndex(zIndex)
                    }
                }
                OutlinedButton(
                    onClick = {
                        onToggle(index == 1)
                    },
                    border = BorderStroke(width = 1.dp, color = Color.Transparent),
                    shape = shape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = (if (selected) {
                            if (option == "Gasto") {
                                // Fondo rojo si "Gasto" está seleccionado pero no habilitado
                                Color(0xFFED5150)
                            } else {
                                // Fondo verde si "Ingreso" está seleccionado
                                Color(0xFF66BB6A)
                            }
                        } else Color.Transparent) as Color, // Si no está seleccionado, el color de fondo será transparente
                        contentColor = if (selected) {
                            if (option == "Gasto") {
                                // Texto en blanco si "Gasto" está seleccionado pero no habilitado
                                Color.White
                            } else {
                                // Texto en blanco si "Ingreso" está seleccionado
                                Color.White
                            }
                        } else {
                            Color.LightGray // Texto en gris si no está seleccionado
                        }
                    ),
                    modifier = buttonModifier.weight(1f),
                    enabled = if (homeViewModel.dineroActual.value!! == 0.0) {
                        option == "Ingreso"
                    } else {
                        true
                    }
                ) {
                    Text(option)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun DropDownWitchIcon(isChecked: Boolean, modifier: Modifier): Category {
        val categories: List<Category> = if (isChecked) categoriesIngresos else categoriesGastos
        var expanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf(categories.first()) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
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
                            contentDescription = null
                        )
                    },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categories.forEach { itemCategory ->
                        DropdownMenuItem(onClick = {
                            //de esta manera podemos usar lo seleccionado en otro lado, usamos la variable selecrted item
                            selectedItem = itemCategory
                            expanded = false
                        }) {
                            Row(modifier = modifier) {
                                // Icon(imageVector = itemCategory.icon, contentDescription = null)
                                Image(
                                    painter = painterResource(id = itemCategory.icon),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.padding(16.dp))
                                Text(text = itemCategory.name)
                            }
                        }
                    }
                }
            }
        }
        return selectedItem
    }


}