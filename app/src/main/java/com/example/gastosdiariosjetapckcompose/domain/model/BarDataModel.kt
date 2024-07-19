package com.example.gastosdiariosjetapckcompose.domain.model

import kotlin.reflect.KProperty

data class BarDataModel(
    val id:Int = 0,
    var value: Float, // Valor numérico que representa la altura de la barra
    val month: String,
    var money:String
)