package com.example.gastosdiariosjetapckcompose.domain.model

data class MovimientosModel(
    val id: Int = 0,
    val iconResourceName: String,
    val title: String,
    var subTitle: String,
    var cash: String,
    val select:Boolean,
    val date: String
)