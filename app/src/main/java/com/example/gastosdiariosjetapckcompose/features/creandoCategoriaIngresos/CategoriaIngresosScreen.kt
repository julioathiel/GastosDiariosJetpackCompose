package com.example.gastosdiariosjetapckcompose.features.creandoCategoriaIngresos

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.enums.IngresosGastosEnum
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryCrear
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatIngresosModel
import com.example.gastosdiariosjetapckcompose.domain.model.categorieGastosNuevos
import com.example.gastosdiariosjetapckcompose.domain.uiState.CatIngresosUiState
import com.example.gastosdiariosjetapckcompose.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CategoriaIngresosScreen(
    navController: NavHostController, categoriaIngresosViewModel: CategoriaIngresosViewModel
) {
    val onDismiss by categoriaIngresosViewModel.onDismiss
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BackHandler {
        navController.navigate(Routes.ConfigurationScreen.route)
    }


    BottomSheetScaffold(topBar = { Toolbar(categoriaIngresosViewModel) },
        sheetMaxWidth = 0.dp,
        sheetContent = {
            if (onDismiss) {
                ModalBottomSheet(
                    onDismissRequest = { categoriaIngresosViewModel.onDismissSet(false) },
                    sheetState = sheetState,
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Llama a ContentBottomSheet desde aquí
                            ContentBottomSheet(
                                onDismiss = { categoriaIngresosViewModel.onDismissSet(false) },
                                categoriaIngresosViewModel
                            )
                        }
                    },
                )
            }
        }
    ) {
        //pantalla principal
        //creando el ciclo de vida de la pantalla
        val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle

        val uiState by produceState<CatIngresosUiState>(
            initialValue = CatIngresosUiState.Loading,
            key1 = lifecycle,
            key2 = categoriaIngresosViewModel
        ) {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                categoriaIngresosViewModel.uiState.collect { value = it }
            }
        }
        when (uiState) {
            is CatIngresosUiState.Error -> {}
            CatIngresosUiState.Loading -> {
                sharedLogic.LoaderCircularProgressPantalla()
            }

            is CatIngresosUiState.Success -> {
                // Obtener la lista de transacciones desde el ViewModel
                val categoriaNueva = (uiState as CatIngresosUiState.Success).data
                if (categoriaNueva.isEmpty()) {
                    // Si la lista está vacía, mostrar
                    categoriaIngresosViewModel.isActivatedFalse()
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BotonGastosIngresos { tipoClase ->
                            if (tipoClase == IngresosGastosEnum.GASTOS) {
                                navController.navigate(Routes.CategoriaGastosScreen.route)
                            } else {
                                //
                            }
                        }
                        VacioIngresos(categoriaIngresosViewModel)
                    }

                } else {
                    categoriaIngresosViewModel.isActivatedTrue()
                    Box(Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            BotonGastosIngresos { tipoClase ->
                                if (tipoClase == IngresosGastosEnum.GASTOS) {
                                    navController.navigate(Routes.CategoriaGastosScreen.route)
                                } else {
                                    //
                                }
                            }
                            ListaNuevaCategoriaGasto(
                                listNueva = (uiState as CatIngresosUiState.Success).data,
                                categoriaIngresosViewModel
                            )
                        }
                        FloatingActionButton(
                            onClick = { categoriaIngresosViewModel.onDismissSet(true) },
                            modifier = Modifier
                                .align(alignment = Alignment.BottomEnd)
                                .padding(20.dp)
                                .zIndex(1f)
                        )
                        { Icon(Icons.Default.Add, contentDescription = null) }
                    }
                }
            }
        }
    }
}

@Composable
fun ListaNuevaCategoriaGasto(
    listNueva: List<UsuarioCreaCatIngresosModel>,
    categoriaIngresosViewModel: CategoriaIngresosViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
    ) {
        items(listNueva, key = { it.id }) { nuevoItem ->
            ItemIngresos(itemModel = nuevoItem, categoriaIngresosViewModel)
        }
    }
}


@Composable
fun ItemIngresos(
    itemModel: UsuarioCreaCatIngresosModel,
    categoriaIngresosViewModel: CategoriaIngresosViewModel
) {
    // Estado para controlar la visibilidad del menú
    var showMenu by remember { mutableStateOf(false) }


    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = itemModel.categoriaIcon.toInt()),
            contentDescription = null,
            modifier = Modifier.padding(start = 16.dp, end = 32.dp)
        )
        Text(
            text = itemModel.nombreCategoria,
            fontSize = 16.sp,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { showMenu = true }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_option),
                contentDescription = null
            )
        }

    }
    if (showMenu) {
        sharedLogic.EditDeleteAlertDialog(
            onDismiss = { showMenu = false },
            onEditClick = {
                categoriaIngresosViewModel.editarItemSelected(
                    UsuarioCreaCatIngresosModel(
                        id = itemModel.id,
                        nombreCategoria = itemModel.nombreCategoria,
                        categoriaIcon = itemModel.categoriaIcon
                    ),
                    iconoSeleccionado = itemModel.categoriaIcon.toInt()
                )
            }, onDeleteClick = { categoriaIngresosViewModel.eliminarItemSelected(itemModel) })
    }
}

@Composable
fun ContentBottomSheet(
    onDismiss: () -> Unit,
    categoriaIngresosViewModel: CategoriaIngresosViewModel
) {
    val titulo by categoriaIngresosViewModel.tituloBottomSheet.collectAsState("")
    val categoriaSeleccionada by categoriaIngresosViewModel.selectedCategoryIngresos.collectAsState()
    val isEditarSeleccion by categoriaIngresosViewModel.isEditarSeleccion


    Column(Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.selecciona_un_icono),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        HorizontalDivider()

        CategoryListIngresos(
            iconSelected = categoriaSeleccionada,
        ) { icon ->
            categoriaIngresosViewModel.iconoSelecionadoIngresos(icon)
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.nuevo_titulo),
            style = MaterialTheme.typography.labelMedium
        )
        TextField(
            value = titulo,
            onValueChange = { categoriaIngresosViewModel.actualizarTituloIngresos(it) },
            modifier = Modifier.fillMaxWidth(),
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
                    categoriaIngresosViewModel.actualizandoItemIngresos(
                        UsuarioCreaCatIngresosModel(
                            nombreCategoria = titulo,
                            categoriaIcon = categoriaSeleccionada!!.icon.toString()
                        )
                    )
                } else {
                    //para crear un item de gastos nuevo
                    categoriaIngresosViewModel.crearNuevaCategoriaDeIngresos(
                        UsuarioCreaCatIngresosModel(
                            nombreCategoria = titulo,
                            categoriaIcon = categoriaSeleccionada!!.icon.toString()
                        )
                    )
                }
                onDismiss() // Cierra el bottom sheet después de guardar
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
fun CategoryListIngresos(
    iconSelected: CategoryCrear?,
    onCategorySelected: (CategoryCrear) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
        modifier = Modifier
            .heightIn(max = 280.dp) // Ajusta la altura máxima para mostrar solo 5 elementos
    ) {
        items(categorieGastosNuevos) { category ->
            CategoryItemIngresos(
                category = category,
                isSelected = category == iconSelected,
                onCategorySelected = onCategorySelected
            )
        }
    }
}

@Composable
fun CategoryItemIngresos(
    category: CategoryCrear,
    isSelected: Boolean,
    onCategorySelected: (CategoryCrear) -> Unit
) {
    val iconColor =
        if (isSelected) MaterialTheme.colorScheme.inversePrimary
        else MaterialTheme.colorScheme.onSurfaceVariant

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
fun VacioIngresos(categoriaIngresosViewModel: CategoriaIngresosViewModel) {
    Box {
        sharedLogic.IsEmpty()

        FloatingActionButton(
            onClick = { categoriaIngresosViewModel.onDismissSet(true) },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(20.dp)
        )
        { Icon(Icons.Default.Add, contentDescription = null) }
    }
}

@Composable
fun BotonGastosIngresos(onTipoSeleccionado: (IngresosGastosEnum) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(1) }
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
                    IngresosGastosEnum.GASTOS -> MaterialTheme.colorScheme.error
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
                border = BorderStroke(color = colorText, width = 1.dp),
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
fun Toolbar(categoriaGastosViewModel: CategoriaIngresosViewModel) {
    val activation = categoriaGastosViewModel.isActivated.value
    var isBorrarTodo by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(text = "Catagorias nuevas") },
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
                        onClick = { categoriaGastosViewModel.borrandoLista() }
                    )

                }
            }
        }
    )
}

