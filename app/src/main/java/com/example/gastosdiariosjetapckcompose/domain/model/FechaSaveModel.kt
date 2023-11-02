package com.example.gastosdiariosjetapckcompose.domain.model

data class FechaSaveModel(
    val id: Int = 1,
    var fechaSave: String,
    val isSelected: Boolean = true
)