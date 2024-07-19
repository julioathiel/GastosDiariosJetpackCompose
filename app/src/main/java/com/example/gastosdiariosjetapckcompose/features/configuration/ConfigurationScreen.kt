package com.example.gastosdiariosjetapckcompose.features.configuration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.DialogCustom
import com.example.gastosdiariosjetapckcompose.features.configuration.components.ShareSheet
import com.example.gastosdiariosjetapckcompose.navigation.Routes


@Composable
fun ConfigurationScreen(
    navController: NavHostController,
    configurationViewModel: ConfigurationViewModel,
) {
    // Estado para controlar si se está mostrando el diálogo
    val showDialog = configurationViewModel.showDialog.value
    val showShareApp = configurationViewModel.showShareApp.value
    val resetComplete = configurationViewModel.resetComplete.value
    val alertDialog = DialogCustom()
    val context = LocalContext.current
    // Establecer el estado de la pestaña seleccionada como el índice correspondiente a la pestaña "Stadist"
    val seleccionadoTabIndex by rememberSaveable { mutableIntStateOf(2) }
    BackHandler {
        navController.navigate(Routes.HomeScreen.route)
    }
    var a by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { Toolbar() },
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
                    ItemConfiguration.CATEGORIASNUEVAS -> {
                        navController.navigate(Routes.CategoriaGastosScreen.route)
                    }

                    ItemConfiguration.UPDATEDATE -> {
                        navController.navigate(Routes.ActualizarMaximoFechaScreen.route)
                    }

                    ItemConfiguration.RECORDATORIOS -> {
                        navController.navigate(Routes.RecordatorioScreen.route)
                    }

                    ItemConfiguration.RESET -> {
                        //  println()
                        configurationViewModel.setShowResetDialog(true)
                    }

                    ItemConfiguration.TUTORIAL -> {
                        configurationViewModel.setBooleanPagerFalse()
                        navController.navigate(Routes.ViewPagerScreen.route)
                    }

                    ItemConfiguration.COMPARTIR -> {
                        configurationViewModel.setShowShare(true)
                    }

                    ItemConfiguration.ACERCADE -> {
                        navController.navigate(Routes.AcercaDe.route)
                    }

                    else -> {}
                }
            })
        if (showShareApp) {
            val enlace = stringResource(id = R.string.share_app)
            ShareSheet(enlace, onDissmiss = { configurationViewModel.setShowShare(false) })
        }
        // Mostrar el diálogo cuando sea necesario
        if (showDialog) {
            alertDialog.MostrarDialogo(
                context = context,
                "pantallaregistoCategoria",
                onDismiss = { configurationViewModel.dismissDialog() },
                onAccept = {
                    //eliminando predeterminadamente
                    configurationViewModel.clearDatabase()//borra base de datos
                    configurationViewModel.checkDatabaseEmpty()//verifica si esta vacia
                },
                opcionesEliminar = configurationViewModel.opcionesEliminar,
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
            alertDialog.MostrarDialogoCongratulations(
                context = context,
                pantalla = "reinicioCompletado",
                onDismiss = { configurationViewModel.setResetComplete(false) },
                onAccept = { configurationViewModel.setResetComplete(false) }
            )
        }
    }
}


@Composable
fun ListConf(
    modifier: Modifier, items: List<ItemConfiguration>,
    onItemClick: (ItemConfiguration) -> Unit,
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        items.forEach {

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
                    contentDescription = it.title,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                Column {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = it.descripcion,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(id = R.color.grayCuatro)
                    )
                }
            }
            //Divider()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.toolbar_configuracion)
            )
        }
    )
}



