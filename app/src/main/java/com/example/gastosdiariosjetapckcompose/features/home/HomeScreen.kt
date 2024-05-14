@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gastosdiariosjetapckcompose.features.home


import android.app.DatePickerDialog
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.gastosdiariosjetapckcompose.ClaseEnum
import com.example.gastosdiariosjetapckcompose.CurrencyAmountInputVisualTransformation
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.CardInfoModel
import com.example.gastosdiariosjetapckcompose.domain.model.CategoriesModel
import com.example.gastosdiariosjetapckcompose.domain.model.categoriaDefault
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesGastos
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesIngresos
import com.example.gastosdiariosjetapckcompose.features.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import kotlin.system.exitProcess

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    configuracion: ConfigurationViewModel
) {
    BackHandler{ exitProcess(0)
    }

    val showDialogTransaction: Boolean by homeViewModel.showDialogTransaction.observeAsState(initial = false)
    val isShowSnackbar = remember { SnackbarHostState() }
    // Observa el LiveData y redibuja la pantalla cuando cambia
    val isReinicioExitoso by configuracion.appResetExitoso.observeAsState()

    LaunchedEffect(Unit){//para cuando se elimia o edita en la pantalla de movimientos
        homeViewModel.mostrarDineroTotal()
        //limite por dia borrado
        homeViewModel.mostrarLimitePorDiaAlUsuario()
        homeViewModel.mostrandoDineroTotalProgress()
    }

    LaunchedEffect(Unit) {
        // Observa el reinicio de la aplicación y actualiza los valores relevantes
        if (isReinicioExitoso == true) {
            //dineroActual borrado
            homeViewModel.mostrarDineroTotal()
            //limite por dia borrado
            homeViewModel.mostrarLimitePorDiaAlUsuario()
            //totalDinero borrado
            homeViewModel.mostrandoDineroTotalProgress()
            //totalDinero gastado borrado
            homeViewModel.mostrandoDineroTotalProgressGastos()
            //diasrestantes borrado
            homeViewModel.mostrarDiasrestantesBorradaAlUSuario()
            //fecha con barra mostrada al usuario borrada
            homeViewModel.mostrarFechaBorradaAlUSuario()
        }
    }

    Scaffold(
        bottomBar = { MyBotonNavigation(navController) },
        scaffoldState = rememberScaffoldState(),
        floatingActionButton = { MyFAB(homeViewModel, isShowSnackbar) },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        backgroundColor = colorResource(id = R.color.grayUno),
        snackbarHost = {
            SnackbarHost(hostState = isShowSnackbar)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
        ) {
           // Header(usuario = "Julio", homeViewModel)
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            BodyHeader(navController, homeViewModel)
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            CountDate(Modifier.fillMaxWidth(), homeViewModel)
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = stringResource(R.string.gastos),
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.lato_bold)),
                modifier = Modifier.padding(start = 16.dp, bottom = 2.dp)
            )
            CardBotonRegistro(navController, homeViewModel)

            AddTransactionDialog(
                showDialogTransaccion = showDialogTransaction,
                onDissmis = { homeViewModel.onDialogClose() },
                homeViewModel
            )
        }
    }
}


@Composable
fun Header(usuario: String, homeViewModel: HomeViewModel) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            // homeViewModel.guardarRutaImagen(ImagenSeleccionadaModel(uri = it.toString()))
        }
    }

    Row(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
        .clickable { galleryLauncher.launch("image/*") }
    ) {

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Imagen seleccionada",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(45.dp)
            )
        } ?: run {
            Image(
                painter = painterResource(id = R.drawable.ic_persona_circle),
                contentDescription = "imagen por defecto",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(45.dp),
                colorFilter = ColorFilter.tint(Color.LightGray)
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))
        Column(Modifier.fillMaxWidth()) {
            Text(text = "Hola $usuario", color = Color.Gray)
            //obteniendo fecha asi   3 oct.2023
            val date = DateFormat.getDateInstance().format(Date())
            Text(text = date.toString(), fontSize = 12.sp, color = Color.Black)
        }
    }
}

@Composable
fun BodyHeader(
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    val limitePorDiaObserve by homeViewModel.limitePorDia.observeAsState()
    val limitePorDia = sharedLogic.formattedCurrency(limitePorDiaObserve!!)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.tu_dinero_actual), color = Color.Black,
            fontFamily = FontFamily(Font(R.font.lato_bold))
        )

        Button(
            onClick = { navController.navigate(route = Routes.MovimientosScreen.route) },
          //  border = BorderStroke(0.dp, colorResource(id = R.color.blue)), // Borde transparente
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.fondoCelesteblue)),// Fondo transparente
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.ir_a_movimientos), color = colorResource(id = R.color.blue),
                    fontFamily = FontFamily(Font(R.font.lato_bold))
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_next),
                    contentDescription = null,
                    tint = colorResource(id = R.color.blue)
                )
            }
        }
    }
    Column(modifier = Modifier.padding(start = 16.dp)) {
        //muestra el dinero actual al usuario
        FormattedCurrencyCash(homeViewModel)
        Box(
            modifier = Modifier
                .background(
                    color = colorResource(id = R.color.fondoCelesteblue),
                    shape = RoundedCornerShape(16.dp)
                )

        ) {
            Text(
                text = stringResource(R.string.por_d_a, limitePorDia),
                color = colorResource(id = R.color.blue),
                modifier = Modifier.padding(
                    horizontal = 10.dp
                ),
                fontFamily = FontFamily(Font(R.font.lato_bold))
            )
        }
    }
}

@Composable
fun CountDate(modifier: Modifier, homeViewModel: HomeViewModel) {
    //observando el estado
    val diasRestantes by homeViewModel.diasRestantes.observeAsState()
    //fechaelegida
    val fechaElegida = homeViewModel.fechaElegidaBarra.observeAsState().value ?: ""

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        )
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
                //Titulo
                Text(
                    text = stringResource(R.string.dias_restantes),
                    color = colorResource(id = R.color.black),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.lato_bold))
                )
                //muestra cuantos dias faltan desde la fecha elegida a la fecha actual
                Text(
                    text = diasRestantes.toString(),
                    color = colorResource(id = R.color.black),
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.lato_bold)),
                    modifier = Modifier.padding(8.dp)
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.fondoCelesteblue),
                            shape = RoundedCornerShape(16.dp)
                        )
                        // Ocultar el Box cuando fechaElegida es una cadena vacía
                        .requiredWidthIn(
                            min = 0.dp,
                            max = if (fechaElegida.isEmpty()) 0.dp else Dp.Infinity
                        )
                ) {
                    // muestra la fecha elegida opr el usuario
                    Text(
                        text = fechaElegida.toString(),
                        color = colorResource(id = R.color.blue),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp),
                        fontFamily = FontFamily(Font(R.font.lato_bold))
                    )
                }

                Spacer(modifier = Modifier.size(4.dp))
            }
            MyDatePicker(homeViewModel)
        }
    }
}

@Composable
fun MyDatePicker(homeViewModel: HomeViewModel) {
    val fechaMaxima by homeViewModel.selectedOptionFechaMaxima.collectAsState()
    var selectedDate: String? by remember { mutableStateOf(null) }
    val calendar = Calendar.getInstance()

    //obteniendo fecha actual
    val currentYear: Int = calendar.get(Calendar.YEAR)
    val currentMonth: Int = calendar.get(Calendar.MONTH)
    val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

    val myDatePicker = DatePickerDialog(
        LocalContext.current, R.style.CustomDatePickerDialog,
        { _, year: Int, month: Int, day: Int ->
            //actualiza la fecha  #### - ## - ##  para saber la diferencia de dias
            val esFechaActualSeleccionada =
                (year == currentYear && month == currentMonth && day == currentDay)
            if (esFechaActualSeleccionada) {
                // No realizar ninguna acción si se selecciona la fecha actual
            } else {
                //Actualizar la fecha
                selectedDate = "${String.format("%02d", day)}/${
                    String.format("%02d", month.plus(1))
                }/${year}"
                homeViewModel.enviandoFechaElegida(selectedDate!!)
            }
        }, currentYear, currentMonth, currentDay
    )

    //no se puede selecionar dias anteriores a la fecha actual
    myDatePicker.datePicker.minDate = calendar.timeInMillis
    calendar.set(currentYear, currentMonth, currentDay + fechaMaxima)
    //el maximo de dias a mostrar seran del limitemaximoDias
    myDatePicker.datePicker.maxDate = calendar.timeInMillis
//deshabilitar la fecha actual para que el usuario no pueda seleccionarla


    Button(
        modifier = Modifier
            .height(48.dp),
        shape = RoundedCornerShape(15.dp),
        onClick = { myDatePicker.show() },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.grayUno))
    ) {
        Text(
            text = stringResource(R.string.editar), fontSize = 14.sp,
            color = colorResource(id = R.color.blue),
            fontFamily = FontFamily(Font(R.font.lato_bold))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    showDialogTransaccion: Boolean, onDissmis: () -> Unit,
    homeViewModel: HomeViewModel
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
                        .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(Spacer))

                    TextFielDinero(cantidadIngresada) { nuevoValor ->
                        cantidadIngresada = nuevoValor
                    }
                    Spacer(modifier = Modifier.padding(Spacer))
                    //DropDown para seleccionar la categoria
                    selectedCategory =
                        menuDesplegable(isChecked, Modifier.fillMaxWidth(), homeViewModel)
                    Spacer(modifier = Modifier.padding(Spacer))
                    Text(
                        text = stringResource(R.string.descripcion),
                        fontSize = 16.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 16.dp)
                    )
                    //Description
                    TextFieldDescription(description = description) { newDescription ->
                        description = newDescription
                    }
                    Spacer(modifier = Modifier.size(30.dp))

                    sharedLogic.BotonGastosIngresos(enabledBotonGastos, tipoBoton!!) { tipoClase ->
                        if (tipoClase == ClaseEnum.INGRESOS) {
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
                                cantidadIngresada = ""
                                description = ""
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 16.dp),
                        enabled = cantidadIngresada.isNotEmpty() && selectedCategory.name != "Elige una categoria",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8247C5)
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text = "Guardar", fontSize = 14.sp)
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
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        placeholder = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    "$0.00",
                    color = colorResource(id = R.color.grayTres),
                    fontSize = 40.sp
                )
            }

        },
        visualTransformation = CurrencyAmountInputVisualTransformation(
            fixedCursorAtTheEnd = true
        ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
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
        modifier = Modifier.padding(horizontal = 16.dp),
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun menuDesplegable(
    isChecked: Boolean,
    modifier: Modifier,
    homeViewModel: HomeViewModel
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

    LaunchedEffect(selectedItem.icon) {
        // Realizar acciones secundarias al cambiar el icono seleccionado
    }

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
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = colorResource(id = R.color.grayUno),
                    focusedContainerColor = colorResource(id = R.color.grayUno)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
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
                                    modifier = Modifier.size(24.dp)
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
    Log.d("quee","didneroActual$mostrandoDinero")
    val (integerPart, decimalPart) = sharedLogic.convertidorDeTexto(mostrandoDinero)
    Log.d("quee","integerPart $integerPart")
    Log.d("quee","decimalPart $decimalPart")
    ObservadorDinero(integerPart, decimalPart)
}
//667747.98
@Composable
fun ObservadorDinero(temporal: CharSequence, decimalPart: String) {
    Row(verticalAlignment = Alignment.Bottom) {
        val size = 40
        Text(
            text = "$", color = Color.Black,
            fontSize = (size / 2).sp,
            modifier = Modifier.offset(y = (-10).dp),
            fontFamily = FontFamily(Font(R.font.lato_bold))
        )

        Text(
            text = temporal.toString(),
            fontSize = size.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.lato_bold)),
            modifier = Modifier.offset(x = (-10).dp)
        )
        Log.d("quee","decimalPart $decimalPart")
        Text(
            text = decimalPart, fontSize = (size / 2).sp,
            //texto que modifica los decimales en la posicion hacia arriba y se ven pequeños
            modifier = Modifier.offset(y = (-20).dp, x = (-10).dp),
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.lato_bold))
        )
    }
}


@Composable
fun MyFAB(
    homeViewModel: HomeViewModel,
    isShowSnackbar: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            if (homeViewModel.diasRestantes.value == 0) {
                scope.launch {
                    showSnackbar(
                        isShowSnackbar,
                        message = "Selecciona la fecha primero"
                    )
                }
            } else {
                homeViewModel.onShowDialogClickTransaction()
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "add"
        )
    }
}

@Composable
fun MyBotonNavigation(navController: NavController) {
    var index by remember { mutableStateOf(0) }

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            selected = index == 0,
            onClick = {
                if (index != 0) {
                    index = 0
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "home",
                    colorFilter = if (index == 0) ColorFilter.tint(colorResource(id = R.color.blue))
                    else ColorFilter.tint(colorResource(id = R.color.grayCuatro))
                )
            }
        )
        Spacer(modifier = Modifier.width(150.dp)) // Añade un espacio entre los botones
        // Este espacio se dejará vacío

        BottomNavigationItem(
            selected = index == 1,
            onClick = {
                if (index != 1) {
                    index = 1
                    navController.navigate(Routes.ConfigurationScreen.route)
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "home",
                    colorFilter = if (index == 1) ColorFilter.tint(colorResource(id = R.color.blue))
                    else ColorFilter.tint(colorResource(id = R.color.grayCuatro))
                )
            }
        )
    }
}

suspend fun showSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    snackbarHostState.showSnackbar(
        message = message,
        actionLabel = null,
        duration = SnackbarDuration.Short
    )
}

@Composable
fun CardBotonRegistro(navController: NavController, homeViewModel: HomeViewModel) {
    val totalIngresosProgress by homeViewModel.mostrandoDineroTotalIngresos.observeAsState()
    val totalGastosProgress by homeViewModel.mostrandoDineroTotalGastos.observeAsState()

    val progresoRelativo =
        sharedLogic.calcularProgresoRelativo(totalIngresosProgress, totalGastosProgress)
    val porcentaje = sharedLogic.formateandoPorcentaje(progresoRelativo)

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = { navController.navigate(Routes.RegistroTransaccionesScreen.route) },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(0.dp, Color.Transparent), // Borde transparente
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.grayUno)),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ver mas", color = colorResource(id = R.color.blue))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_navigate_next),
                        contentDescription = null,
                        tint = colorResource(id = R.color.blue)
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "$porcentaje%",
                color = colorResource(id = R.color.black),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.lato_bold))
            )

            AnimatedProgressBar(
                progress = progresoRelativo.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sharedLogic.formattedCurrency(totalGastosProgress),
                    color = colorResource(id = R.color.rojoDinero),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.lato_bold))
                )
                VerticalDivider()
                Text(
                    text = sharedLogic.formattedCurrency(totalIngresosProgress),
                    color = colorResource(id = R.color.verdeDinero),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.lato_bold))
                )
            }
        }
    }
}


@Composable
fun AnimatedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier,
    )
}

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
                0 -> navController.navigate(Routes.RegistroTransaccionesScreen.route)
                else -> {}
            }
        }, modifier = modifier
            .height(120.dp)
            .padding(horizontal = 5.dp), colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.bodyCard)
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
                color = colorResource(id = R.color.tituloBlanco),
                fontWeight = FontWeight.Light
            )
        }
    }
}