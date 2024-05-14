package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UsuarioCreaCatGastoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreCategoria: String,
    val categoriaIcon: String
)