package com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaGastos

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.commons.CommonsIsEmpty
import com.example.gastosdiariosjetapckcompose.domain.enums.IngresosGastosEnum
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryCrear
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatGastoModel
import com.example.gastosdiariosjetapckcompose.domain.model.categorieGastosNuevos
import com.example.gastosdiariosjetapckcompose.domain.uiState.ResultUiState
import com.example.gastosdiariosjetapckcompose.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CategoriaGastosScreen(
    navController: NavHostController, viewModel: CategoriaGastosViewModel
) {
    val onDismiss by viewModel.onDismiss
    val sheetStates = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BackHandler {
        //al presion el boton fisico de retoceso, se dirige a la pantalla de configuracion
        navController.navigate(Routes.ConfigurationScreen.route)
    }

    BottomSheetScaffold(topBar = {
        Toolbar(viewModel) { viewModel.borrandoLista() }
    },
        sheetMaxWidth = 0.dp,
        sheetContent = {
            if (onDismiss) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onDismissSet(false) },
                    sheetState = sheetStates,
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            ContentBottomSheet(
                                onDismiss = { viewModel.onDismissSet(false) },
                                viewModel
                            )
                        }
                    },
                )
            }
        }
    ) {
        //creando el ciclo de vida de la pantalla
        val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle

        val uiState by produceState<ResultUiState>(
            initialValue = ResultUiState.Loading,
            key1 = lifecycle,
            key2 = viewModel
        ) {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.uiState.collect { value = it }
            }
        }
        when (uiState) {
            is ResultUiState.Error -> {}
            ResultUiState.Loading -> {
                sharedLogic.LoaderCircularProgressPantalla()
            }

            is ResultUiState.Success -> {
                //si hay datos sera el unico momento en donde se pinta la pantalla
                // Obtener la lista de transacciones desde el ViewModel
                val categoriaNueva = (uiState as ResultUiState.Success).data
                if (categoriaNueva.isEmpty()) {
                    // Si la lista está vacía, mostrar
                    viewModel.isActivatedFalse()
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BotonGastosIngresosPantallaGastos { tipoClase ->
                            if (tipoClase == IngresosGastosEnum.INGRESOS) {
                                navController.navigate(Routes.CategoriaIngresosScreen.route)
                            } else {
                                //
                            }
                        }
                        VacioGastos(viewModel)
                    }
                } else {
                    viewModel.isActivatedTrue()
                    Box(
                        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            BotonGastosIngresosPantallaGastos { tipoClase ->
                                if (tipoClase == IngresosGastosEnum.INGRESOS) {
                                    navController.navigate(Routes.CategoriaIngresosScreen.route)
                                } else {
                                    //
                                }

                            }
                            ListaNuevaCategoriaGasto(
                                listNueva = (uiState as ResultUiState.Success).data,
                                viewModel
                            )
                        }
                        FloatingActionButton(
                            onClick = { viewModel.onDismissSet(true) },
                            modifier = Modifier
                                .align(alignment = Alignment.BottomEnd)
                                .padding(20.dp)
                                .zIndex(1f)
                        )
                        { Icon(Icons.Default.Add, contentDescription = null) }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
fun ListaNuevaCategoriaGasto(
    listNueva: List<UsuarioCreaCatGastoModel>,
    categoriaGastosViewModel: CategoriaGastosViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
    ) {
        items(listNueva, key = { it.id }) { nuevoItem ->
            ItemGastos(itemModel = nuevoItem, categoriaGastosViewModel)
        }
    }
}


@Composable
fun ContentBottomSheet(
    onDismiss: () -> Unit,
    categoriaGastosViewModel: CategoriaGastosViewModel
) {
    val titulo by categoriaGastosViewModel.tituloBottomSheet.collectAsState("")
    val categoriaSeleccionada by categoriaGastosViewModel.selectedCategoryGastos.collectAsState()
    val isEditarSeleccion by categoriaGastosViewModel.isEditarSeleccion

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.selecciona_un_icono),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        HorizontalDivider()

        CategoryListGastos(
            iconoSeleccionado = categoriaSeleccionada
        ) { icono ->
            categoriaGastosViewModel.IconoSelecionado(icono)
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(R.string.nuevo_titulo),
            style = MaterialTheme.typography.labelMedium
        )
        TextField(
            value = titulo,
            onValueChange = { categoriaGastosViewModel.actualizarTitulo(it) },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        )
        Spacer(modifier = Modifier.padding(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = {

                if (isEditarSeleccion) {
                    categoriaGastosViewModel.actualizandoItem(
                        UsuarioCreaCatGastoModel(
                            nombreCategoria = titulo,
                            categoriaIcon = categoriaSeleccionada!!.icon.toString()
                        )
                    )
                } else {
                    //para crear un item de gastos nuevo
                    categoriaGastosViewModel.crearNuevaCategoriaDeGastos(
                        UsuarioCreaCatGastoModel(
                            nombreCategoria = titulo,
                            categoriaIcon = categoriaSeleccionada!!.icon.toString()
                        )
                    )
                }
                //despues de crear o editar, cierra el bottomsheet
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !(titulo.isEmpty() || categoriaSeleccionada == null)
        ) {
            Text(text = stringResource(id = R.string.guardar))
        }
    }
}


@Composable
fun CategoryListGastos(
    iconoSeleccionado: CategoryCrear?,
    onCategorySelected: (CategoryCrear) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
        modifier = Modifier
            .heightIn(max = 280.dp) // Ajusta la altura máxima para mostrar solo 5 elementos
    ) {
        //categorieGastosNuevos es la lista predeterminada
        items(categorieGastosNuevos) { category ->
            CategoryItemGastos(
                category = category,
                isSelected = category == iconoSeleccionado,
                onCategorySelected = onCategorySelected//enviando icono seleccionado
            )
        }
    }
}

@Composable
fun CategoryItemGastos(
    category: CategoryCrear,
    isSelected: Boolean,
    onCategorySelected: (CategoryCrear) -> Unit
) {
    val iconColor =
        if (isSelected) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onCategorySelected(category) }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = category.icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}


@Composable
fun VacioGastos(categoriaGastosViewModel: CategoriaGastosViewModel) {
    Box {
        CommonsIsEmpty()
        FloatingActionButton(
            onClick = { categoriaGastosViewModel.onDismissSet(true) },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(20.dp)
        )
        { Icon(Icons.Default.Add, contentDescription = null) }
    }
}

@Composable
fun BotonGastosIngresosPantallaGastos(onTipoSeleccionado: (IngresosGastosEnum) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }
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
                Color.Transparent
            }

            val colorText = if (isSelected) {
                when (label) {
                    IngresosGastosEnum.GASTOS -> colorResource(id = R.color.rojoDinero)
                    IngresosGastosEnum.INGRESOS -> colorResource(id = R.color.verdeDinero)
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
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = color,
                    activeContentColor = colorText,
                    inactiveContainerColor = Color.Transparent
                ),
                border = BorderStroke(color = colorText, width = 1.dp)
            ) {
                Text(
                    label.toString(),
                    color = if (isSelected) colorText else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(viewModel: CategoriaGastosViewModel, onClickAction: () -> Unit) {
    val activation = viewModel.isActivated.value
    var isBorrarTodo by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(text = stringResource(R.string.categor_as_nuevas)) },
        actions = {
            if (activation) {
                IconButton(onClick = { isBorrarTodo = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_option),
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = isBorrarTodo,
                    onDismissRequest = { isBorrarTodo = !isBorrarTodo }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.eliminar_todo)) },
                        onClick = { onClickAction() }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

