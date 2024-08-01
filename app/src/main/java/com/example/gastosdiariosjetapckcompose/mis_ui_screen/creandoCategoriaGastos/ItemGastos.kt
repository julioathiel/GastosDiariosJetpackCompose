package com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaGastos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatGastoModel

@Composable
fun ItemGastos(
    itemModel: UsuarioCreaCatGastoModel,
    categoriaGastosViewModel: CategoriaGastosViewModel
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = itemModel.categoriaIcon.toInt()),
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp, end = 32.dp)
            )
            Text(
                text = itemModel.nombreCategoria,
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
                    categoriaGastosViewModel.selectedParaEditar(
                        UsuarioCreaCatGastoModel(
                            id = itemModel.id,
                            nombreCategoria = itemModel.nombreCategoria,
                            categoriaIcon = itemModel.categoriaIcon
                        ),
                        iconoSeleccionado = itemModel.categoriaIcon.toInt()
                    )
                },
                onDeleteClick = { categoriaGastosViewModel.eliminarItemSelected(itemModel) }
            )
        }
    }
}