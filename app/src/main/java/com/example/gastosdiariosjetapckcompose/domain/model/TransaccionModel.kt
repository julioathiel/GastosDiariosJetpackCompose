package com.example.gastosdiariosjetapckcompose.domain.model

data class TransaccionModel(
    val cantidad: String,
    val categoria:String,
    val description:String,
    val icono: Int,
    val isSelectedChecked: Boolean
)
