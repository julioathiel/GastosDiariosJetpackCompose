package com.example.gastosdiariosjetapckcompose.domain.model

data class UsuarioCreaCatIngresosModel(
    val id: Int = System.currentTimeMillis().hashCode(),
    var nombreCategoria: String,
    var categoriaIcon: String
)
