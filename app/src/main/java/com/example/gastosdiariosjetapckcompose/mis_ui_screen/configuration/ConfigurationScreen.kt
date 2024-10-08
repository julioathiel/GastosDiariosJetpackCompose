package com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.commons.CommonsToolbar
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.domain.model.DialogCustom
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.components.ShareSheet
import com.example.gastosdiariosjetapckcompose.navigation.Routes


@Composable
fun ConfigurationScreen(
    navController: NavHostController,
    viewModel: ConfigurationViewModel,
) {
    // Estado para controlar si se está mostrando el diálogo
    val showDialog = viewModel.showDialog.value
    val showShareApp = viewModel.showShareApp.value
    val resetComplete = viewModel.resetComplete.value
    val alertDialog = DialogCustom()
    val context = LocalContext.current
    // Establecer el estado de la pestaña seleccionada como el índice correspondiente a la pestaña "Stadist"
    val seleccionadoTabIndex by rememberSaveable { mutableIntStateOf(2) }
    BackHandler {
        navController.navigate(Routes.HomeScreen.route)
    }

    Scaffold(
        topBar = {
            CommonsToolbar(
                title = stringResource(id = R.string.toolbar_configuracion),
                colors = MaterialTheme.colorScheme.background
            )
        },
        bottomBar = {
            sharedLogic.TabView(
                navController = navController,
                seleccionadoTabIndex = seleccionadoTabIndex
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { it ->

        // Contenido del Scaffold
        ListConf(modifier = Modifier.padding(it),
            items = ItemConfiguration.entries,
            onItemClick = { item ->
                when (item) {
                    ItemConfiguration.ELIMINAR_EDITAR_PERFIL -> navController.navigate(Routes.UserProfileScreen.route)
                    ItemConfiguration.CATEGORIASNUEVAS -> navController.navigate(Routes.CategoriaGastosScreen.route)
                    ItemConfiguration.UPDATEDATE -> navController.navigate(Routes.ActualizarMaximoFechaScreen.route)
                    ItemConfiguration.RECORDATORIOS -> navController.navigate(Routes.RecordatorioScreen.route)
                    ItemConfiguration.RESET -> viewModel.setShowResetDialog(true)
                    ItemConfiguration.TUTORIAL -> {
                        viewModel.setBooleanPagerFalse()
                        navController.navigate(Routes.ViewPagerScreen.route)
                    }

                    ItemConfiguration.COMPARTIR -> viewModel.setShowShare(true)
                    ItemConfiguration.ACERCADE -> navController.navigate(Routes.AcercaDe.route)
                    ItemConfiguration.AJUSTES_AVANZADOS -> navController.navigate(Routes.AjustesScreen.route)
                    else -> {}
                }
            })
        if (showShareApp) {
            val enlace = stringResource(id = R.string.share_app)
            ShareSheet(enlace, onDissmiss = { viewModel.setShowShare(false) })
        }
        // Mostrar el diálogo cuando sea necesario
        if (showDialog) {
            alertDialog.MostrarDialogo(
                context = context,
                "pantallaregistoCategoria",
                onDismiss = { viewModel.dismissDialog() },
                onAccept = { viewModel.clearDatabase() },
                opcionesEliminar = viewModel.opcionesEliminar,
                onConfirm = {
                    //opciones que el usuario eligio a eliminar
                        selectedOptions ->
                    selectedOptions.forEach { option ->
                        option.action()
                    }
                }
            )
        }
        //si esta vacia muestra otro dialogo
        if (resetComplete) {
            navController.navigate(Routes.CongratulationsScreen.route)
        }
    }
}


@Composable
fun ListConf(
    modifier: Modifier,
    items: List<ItemConfiguration>,
    onItemClick: (ItemConfiguration) -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        var lastCategory: String? = null
        items.forEach {
            if (lastCategory != it.category) {
                // Agregar línea divisoria
                if (lastCategory != null) {
                    HorizontalDivider(modifier = Modifier.padding(start = 70.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh)
                }
                // Actualizar la última categoría
                lastCategory = it.category
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(it)
                    }
                    .padding(16.dp),
                verticalAlignment = CenterVertically,

                ) {
                Image(
                    painter = painterResource(id = it.icon),
                    contentDescription = it.title.toString(),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                Column {
                    Text(
                        text = context.getString(it.title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = context.getString(it.description),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

        }

    }
}



