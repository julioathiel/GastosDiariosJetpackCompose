package com.example.gastosdiariosjetapckcompose.domain.model

data class GastosPorCategoriaModel(
    val id: Int = 0,
    val title: String,
    val categoriaIcon: String,
    var totalGastado: String
)
