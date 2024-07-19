package com.example.gastosdiariosjetapckcompose.features.home.components_home

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.features.home.showSnackbar
import kotlinx.coroutines.launch

@Composable
fun MyFAB(
    homeViewModel: HomeViewModel,
    isShowSnackbar: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            if (sharedLogic.diasRestantes.value == 0) {
                scope.launch {
                    showSnackbar(
                        isShowSnackbar,
                        message = "Selecciona una fecha primero"
                    )
                }
            } else {
                homeViewModel.onShowDialogClickTransaction()
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "add",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}