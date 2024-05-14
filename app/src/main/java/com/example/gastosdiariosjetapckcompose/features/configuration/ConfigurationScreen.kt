package com.example.gastosdiariosjetapckcompose.features.configuration

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.DialogCustom
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ConfigurationScreen(
    configurationViewModel: ConfigurationViewModel,
    navController: NavHostController
) {
    // Estado para controlar si se está mostrando el diálogo
    val showDialog = configurationViewModel.showDialog.value
    var resetComplete = configurationViewModel.resetComplete.value
    var showProgress =configurationViewModel.showProgress.value
    val alertDialog = DialogCustom()
    val context = LocalContext.current

    BackHandler {
        navController.navigate(Routes.HomeScreen.route)
    }

    Scaffold(
        topBar = { Toolbar() },
        containerColor = colorResource(id = R.color.white)
    ) { it ->

        // Contenido del Scaffold
        ListConf(modifier = Modifier.padding(it),
            items = ItemConfiguration.values().toList(),
            onItemClick = { item ->
                when (item) {
                    ItemConfiguration.CATEGORIASNUEVAS -> {
                        navController.navigate(Routes.CategoriaGastosScreen.route)
                    }

                    ItemConfiguration.DIVISA -> {
                        println()
                    }

                    ItemConfiguration.UPDATEDATE -> {
                        navController.navigate(Routes.ActualizarMaximoFechaScreen.route)
                    }

                    ItemConfiguration.RECORDATORIOS -> {
                        navController.navigate(Routes.RecordatorioScreen.route)
                    }

                    ItemConfiguration.RESET -> {
                        println()
                    }

                    ItemConfiguration.ACERCADE -> {

                    }

                    else -> {}
                }
                // Aquí puedes manejar el evento de clic en un elemento de la lista
                // Por ejemplo, mostrar el diálogo cuando se hace clic en un elemento específico
                if (item == ItemConfiguration.RESET) {
                    // configurationViewModel.clearDatabase()
                    // Muestra el diálogo cuando se detecta un reinicio exitoso
                    configurationViewModel.showResetDialog()
                }
            })
        val scope = rememberCoroutineScope()
        // Mostrar el diálogo cuando sea necesario
        if (showDialog) {
            alertDialog.MostrarDialogo(
                context = context,
                "pantallaregistoCategoria",
                onDismiss = { configurationViewModel.dismissDialog() },
                onAccept = {
                    configurationViewModel.clearDatabase()//borra ase de datos
                    configurationViewModel.checkDatabaseEmpty()//verifica si esta vacia
                },
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
            .background(color = colorResource(id = R.color.white))
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
                    contentDescription = it.title
                )
                Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                Column {
                    Text(
                        text = it.title,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.lato_bold))
                    )
                    Text(
                        text = it.descripcion, color = colorResource(id = R.color.grayCuatro),
                        style = MaterialTheme.typography.bodySmall,
                         fontSize = 14.sp
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
                text = stringResource(R.string.toolbar_configuracion),
                color = colorResource(id = R.color.black)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
    )
}



