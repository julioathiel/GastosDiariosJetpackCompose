package com.example.gastosdiariosjetapckcompose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.gastosdiariosjetapckcompose.domain.enums.IngresosGastosEnum
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.features.home.components_home.tabBarItems
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

class SharedLogic {
    var _dineroActual = MutableLiveData<Double>()
    var dineroActual: LiveData<Double> = _dineroActual

    val _itemActualizado = MutableLiveData(false)
    val itemActualizado: LiveData<Boolean> = _itemActualizado

    val _diasRestantes = MutableStateFlow(0)
    val diasRestantes: StateFlow<Int> = _diasRestantes

    val _limitePorDia = MutableStateFlow(0.0)
    val limitePorDia: StateFlow<Double> = _limitePorDia
    fun calcularLimitePorDia(dineroActual: Double): Double {
        val limitePorDia =
            if (_diasRestantes.value != null && dineroActual != null && _diasRestantes.value != 0) {
                dineroActual / _diasRestantes.value.toDouble()
            } else {
                0.0
            }
        _limitePorDia.value = limitePorDia
        return limitePorDia
    }


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
        return String.format("%.0f", progresoRelativo * 100)
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

        val spaciness = 16.dp

        if (showDialogTransaccion) {
            //onDismissRequest = { onDissmis() } funcion para que se descarte el dialogo y desaparezca
            Dialog(onDismissRequest = { onDissmis() }) {
                Card(shape = RoundedCornerShape(30.dp)) {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.padding(spaciness))

                        TextFielDineroMovimientos(cantidadIngresada) { nuevoValor ->
                            cantidadIngresada = nuevoValor
                        }
                        Spacer(modifier = Modifier.padding(spaciness))
                        Text(
                            text = stringResource(id = R.string.descripcion),
                            fontSize = 18.sp,
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
                                    title = transaccion.title,
                                    nuevoValor = cantidadIngresada,
                                    description = description,
                                   itemModel =  transaccion
                                )
                                onDissmis()
                                cantidadIngresada = ""
                                description = ""
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp),
                            enabled = cantidadIngresada.isNotEmpty()
                        ) {
                            Text(text = stringResource(id = R.string.guardar))
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
                        stringResource(id = R.string._0_00),
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
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            //para mostrar la primer letra de la palabra en mayuscula
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )
    }

    @Composable
    fun IsEmpty() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_data),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.sin_resultados),
                modifier = Modifier.padding(top = 30.dp),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }

    @Composable
    fun BotonGastosIngresos(
        enabledBotonGastos: Boolean,
        botonActivado: Int,
        onTipoSeleccionado: (IngresosGastosEnum) -> Unit
    ) {
        var selectedIndex by remember { mutableIntStateOf(botonActivado) }
        val options = listOf(IngresosGastosEnum.GASTOS, IngresosGastosEnum.INGRESOS)
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex
                val color = if (isSelected) {
                    when (label) {
                        IngresosGastosEnum.GASTOS -> Color(0xFFFFEBEE)
                        IngresosGastosEnum.INGRESOS -> colorResource(id = R.color.fondoVerdeDinero)
                    }
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }

                val colorText = if (isSelected) {
                    when (label) {
                        IngresosGastosEnum.GASTOS -> colorResource(id = R.color.rojoDinero)
                        IngresosGastosEnum.INGRESOS -> colorResource(id = R.color.verdeDinero)
                    }
                } else {
                    Color.Transparent //los bordes no se veran
                }
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = {
                        selectedIndex = index
                        onTipoSeleccionado(options[index])
                    },
                    selected = index == selectedIndex,
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = color,
                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    border = BorderStroke(color = colorText, width = 0.dp),
                    // Deshabilitar el botón de gastos si enabledBotonGastos es falso o habilitar todoo si esta en true
                    enabled = enabledBotonGastos || label != IngresosGastosEnum.GASTOS
                ) {
                    Text(
                        label.toString(),
                        color = if (isSelected) colorText else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    @Composable
    fun EditDeleteAlertDialog(
        onDismiss: () -> Unit,
        onEditClick: () -> Unit,
        onDeleteClick: () -> Unit
    ) {

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Elige una opcion") },
            text = { Text(text = "Si presionas eliminar, se hara de inmediato.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEditClick()
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(id = R.string.editar))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(id = R.string.eliminar))
                }
            }
        )
    }

    // Esta es una vista de envoltura que nos permite de manera fácil y limpia
// reutilizar este componente en cualquier proyecto futuro
    @Composable
    fun TabView(
        navController: NavController,
        seleccionadoTabIndex: Int
    ) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
            tabBarItems.fastForEachIndexed { index, tabBarItem ->
                NavigationBarItem(
                    selected = seleccionadoTabIndex == index,
                    onClick = {
                        when (index) {
                            0 -> {
                                navController.navigate(Routes.HomeScreen.route)
                            }

                            1 -> {
                                navController.navigate(Routes.RegistroTransaccionesScreen.route)
                            }

                            2 -> {
                                navController.navigate(Routes.ConfigurationScreen.route)
                            }
                        }
                    },
                    icon = {
                        TabBarIconView(
                            esSeleccionado = seleccionadoTabIndex == index,
                            selectedIcon = tabBarItem.selectedIcon,
                            unselectedIcono = tabBarItem.unSelectedIcon,
                            titulo = tabBarItem.title
                        )
                    },
                    label = {
                        Text(
                            text = tabBarItem.title,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(selectedTextColor = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }

// Este componente ayuda a limpiar la llamada API de nuestra TabView anterior,
// pero podría agregarse fácilmente dentro de TabView sin crear este componente personalizado

    @Composable
    fun TabBarIconView(
        esSeleccionado: Boolean, selectedIcon: Int,
        unselectedIcono: Int, titulo: String,
        badgeMontar: Int? = null
    ) {
        BadgedBox(badge = {
            if (badgeMontar != null) {
                Badge {
                    Text(badgeMontar.toString())
                }
            }
        }) {
            Icon(
                painter = if (esSeleccionado) {
                    painterResource(id = selectedIcon)
                } else {
                    painterResource(id = unselectedIcono)
                }, contentDescription = titulo,
                tint = if (esSeleccionado) {
                    MaterialTheme.colorScheme.primary // Cambia el color del icono cuando está seleccionado
                } else {
                    colorResource(id = R.color.grayCuatro) // Color predeterminado del icono cuando no está seleccionado
                }
            )
        }
    }

    @Composable
    fun LoaderData(modifier: Modifier, image: Int, repeat: Boolean) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(image))
        val iterations = if (repeat) LottieConstants.IterateForever else 1
        LottieAnimation(
            composition = composition,
            iterations = iterations,
            modifier = modifier
        )
    }

    @Composable
    fun LoaderCircularProgressPantalla() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                Modifier
                    .size(30.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

