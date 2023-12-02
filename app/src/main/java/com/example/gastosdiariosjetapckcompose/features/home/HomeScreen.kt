@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gastosdiariosjetapckcompose.features.home


import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.CardInfoModel
import com.example.gastosdiariosjetapckcompose.domain.model.Category
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesGastos
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesIngresos
import com.example.gastosdiariosjetapckcompose.domain.model.listCardInfo
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    movimientosViewModel: MovimientosViewModel
) {
    val showDialogTransaction: Boolean by homeViewModel.showDialogTransaction.observeAsState(initial = false)
    val isShowSnackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        //al vovler a la pantalla, estos se
        // actualizan segun lo que paso en la pantalla anterior
        homeViewModel.actualizarVistaDineroTotal()
        homeViewModel.actualizarVistaLimitePorDia()
    }

    Scaffold(
        bottomBar = { MyBotonNavigation() },
        scaffoldState = rememberScaffoldState(),
        floatingActionButton = { MyFAB(homeViewModel, isShowSnackbar) },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        backgroundColor = Color.Black,
        snackbarHost = {
            SnackbarHost(hostState = isShowSnackbar)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
        ) {

            Header(usuario = "Julio", Modifier.padding(16.dp))
            BodyHeader(Modifier.padding(16.dp), navController, homeViewModel)
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            CountDate(Modifier.fillMaxWidth(), homeViewModel)
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            ItemCardView(navController)
            AddTransactionDialog(
                showDialogTransaccion = showDialogTransaction,
                onDissmis = { homeViewModel.onDialogClose() },
                movimientosViewModel,
                homeViewModel
            )
        }
    }
}


@Composable
fun Header(usuario: String, modifier: Modifier) {

    Row(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.flash),
            contentDescription = "avatar", contentScale = ContentScale.Crop, modifier = Modifier
                .clip(
                    CircleShape
                )
                .size(45.dp)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Column(Modifier.fillMaxWidth()) {
            Text(text = "Hola $usuario", color = Color.Gray)
            //obteniendo fecha asi   3 oct.2023
            val date = DateFormat.getDateInstance().format(Date())
            Text(text = date.toString(), fontSize = 12.sp, color = Color.White)
        }
    }
}

@Composable
fun CountDate(modifier: Modifier, homeViewModel: HomeViewModel) {
    //observando el estado
    val diasRestantes by homeViewModel.diasRestantes.observeAsState()
    //fechaelegida
    val fechaElegida = homeViewModel.fechaElegida.observeAsState().value

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF21252B))
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Dias restantes",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = diasRestantes.toString(),
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
                Card() {
                    Box(modifier = Modifier.background(Color(0xFF2C3138))) {
                        Text(
                            text = fechaElegida.toString(),
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(start = 4.dp, end = 4.dp)
                                .background(Color(0xFF2C3138))
                        )
                    }
                }
                Spacer(modifier = Modifier.size(4.dp))
            }
            MyDatePicker(homeViewModel)
        }
    }
}

@Composable
fun MyDatePicker(homeViewModel: HomeViewModel) {
    //obteniendo la fecha
    val calendar = Calendar.getInstance()
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

    val myDatePicker = DatePickerDialog(
        LocalContext.current,
        { _, year: Int, month: Int, day: Int ->
            //actualiza la fecha  #### - ## - ##  para saber la diferencia de dias
            homeViewModel.setDateTransformer(year, month, day)
            homeViewModel.setDateTransformerconBarra(day, month, year)
            //calcula el limite de dinero a gastar por dia, pero no lo actualiza en el momento
            homeViewModel.calcularLimitePorDia()
        }, year, month, day
    )
    //manejando el minimo y maximo de dias
    val limiteMaximoDias = 30
    //no se puede selecionar dias anteriores a la fecha actual
    myDatePicker.datePicker.minDate = calendar.timeInMillis
    calendar.set(year, month, day + limiteMaximoDias) //añade dias a la fecha actual
    //el maximo de dias a mostrar seran del limitemaximoDias
    myDatePicker.datePicker.maxDate = calendar.timeInMillis

    Button(modifier = Modifier
        .height(48.dp),
        shape = RoundedCornerShape(15.dp),
        onClick = {
            // Llama al DatePicker aquí
            myDatePicker.show()
        }
    ) {
        Text(text = "Select date", fontSize = 14.sp)
    }

}

@Composable
fun BodyHeader(
    modifier: Modifier, navController: NavController,
    homeViewModel: HomeViewModel
) {
    val observadorLimitePorDia: Double by homeViewModel.limitePorDia.observeAsState(0.0)
    val limitePorDia = homeViewModel.formattedCurrency(observadorLimitePorDia)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Tu dinero actual", color = Color.White)
        Row {
            Text(text = "Ir a movimientos", color = Color(0xFFFFCA28),
                modifier = Modifier.clickable {
                    navController.navigate(route = Routes.MovimientosScreen.route)
                })
            Image(
                painter = painterResource(id = R.drawable.ic_navigate_next),
                contentDescription = "icono de siguiente",
                colorFilter = ColorFilter.tint(Color(0xFFFFCA28))
            )
        }
    }
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.padding(top = 8.dp))

        ViewDinero(homeViewModel = homeViewModel)

        Card(Modifier.padding(top = 8.dp)) {
            Text(
                text = "$limitePorDia por dia",
                color = Color(0xFFFFCA28),
                modifier = Modifier
                    .background(color = Color(0xFF202020))
                    .padding(start = 4.dp, end = 4.dp)
            )
        }
    }
}

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
                    selectedCategory = DropDownIcon(isChecked, Modifier.fillMaxWidth())
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
                            .height(48.dp)
                            .padding(horizontal = 16.dp),
                        enabled = cantidadIngresada.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8247C5)
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text = "Guardar",fontSize = 14.sp)
                    }
                    Spacer(modifier =Modifier.size(Spacer))
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
fun DropDownIcon(isChecked: Boolean, modifier: Modifier): Category {
    val categories: List<Category> =
        //se usa sortedBy para mostrar la lista alfabeticamente por el nombre
        if (isChecked) categoriesIngresos.sortedBy { it.name } else categoriesGastos.sortedBy { it.name }
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

@Composable
fun ViewDinero(homeViewModel: HomeViewModel) {
    FormattedCurrencyCash(homeViewModel)
}

@Composable
fun FormattedCurrencyCash(homeViewModel: HomeViewModel) {
    val mostrandoDinero by homeViewModel.dineroActual.observeAsState(0.00)

    var integerPart = homeViewModel.formattedCurrency(mostrandoDinero)//400.523,69
    integerPart = integerPart.replace("$", "")
    //la variable temporal guardara el valor restandole los ultimos 2
    //numeros, segun el tamaño que se ponga
    val temporal = integerPart.subSequence(0, integerPart.length - 3)
    val decimalPart = integerPart.toString().substringAfter(",")
    ObservadorDinero(temporal, decimalPart)
}

@Composable
fun ObservadorDinero(temporal: CharSequence, decimalPart: String) {
    Row(verticalAlignment = Alignment.Bottom) {
        val size = 40
        Text(
            text = temporal.toString(),
            fontSize = size.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = decimalPart, fontSize = (size / 2).sp,
            //texto que modifica los decimales en la posicion hacia arriba y se ven pequeños
            modifier = Modifier.offset(y = (-20).dp),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MyBotonNavigation() {
    var index by remember { mutableStateOf(0) }

    BottomNavigation(backgroundColor = Color(0xFF21252B), contentColor = Color.White) {
        BottomNavigationItem(selected = index == 0, onClick = { index = 0 }, icon = {
            Image(painter = painterResource(id = R.drawable.ic_home), contentDescription = "home")
        })

        BottomNavigationItem(selected = index == 1, onClick = { index = 1 }, icon = {})

        BottomNavigationItem(selected = index == 2, onClick = { index = 2 }, icon = {
            Image(painter = painterResource(id = R.drawable.ic_home), contentDescription = "home")
        })
    }
}

@Composable
fun MyFAB(homeViewModel: HomeViewModel, isShowSnackbar: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    FloatingActionButton(
        //se aprieta boton que inicia la variable en true
        onClick = {

            if (homeViewModel.fechaElegida.value == "Elige una fecha") {
                scope.launch {
                    isShowSnackbar.showSnackbar(
                        message = "Selecciona la fecha primero",
                        actionLabel = null,
                        duration = SnackbarDuration.Short
                    )
                }
            } else homeViewModel.onShowDialogClickTransaction()
        },
        containerColor = Color(0xFF7E57C2),
        contentColor = Color.Black
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "add", tint = Color.White
        )
    }
}

@Composable
fun ItemCardView(navController: NavController) {

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

        listCardInfo.forEachIndexed { index, it ->
            ItemCard(cardInfo = it, modifier = Modifier.weight(1f), navController, index)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    cardInfo: CardInfoModel,
    modifier: Modifier,
    navController: NavController,
    index: Int,
) {
    Card(
        onClick = {
            when (index) {
                0 -> navController.navigate(Routes.TaskScreen.route)
                1 -> navController.navigate(Routes.TaskScreen.route)
                2 -> navController.navigate(Routes.TaskScreen.route)
                3 -> {}
                else -> {}
            }
        }, modifier = modifier
            .height(120.dp)
            .padding(horizontal = 5.dp), colors = CardDefaults.cardColors(
            containerColor = Color(0xFF21252B)
        )
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Image(
                painter = painterResource(id = cardInfo.icon),
                contentDescription = cardInfo.title,
                colorFilter = ColorFilter.tint(cardInfo.color)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                cardInfo.title,
                color = Color.LightGray,
                fontWeight = FontWeight.Light
            )
        }
    }


}


