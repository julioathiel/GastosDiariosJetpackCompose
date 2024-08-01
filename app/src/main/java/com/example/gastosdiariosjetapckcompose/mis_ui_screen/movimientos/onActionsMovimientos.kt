package com.example.gastosdiariosjetapckcompose.mis_ui_screen.movimientos

import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel

sealed class OnActionsMovimientos {
    data class DeleteItem(val lisTransactions: List<MovimientosModel>, val item: MovimientosModel) : OnActionsMovimientos()
    data class EditItem(
        val title: String,
        val nuevoValor: String,
        val description: String,
        val item: MovimientosModel
    ) : OnActionsMovimientos()
}