package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("GastosPorCategoriaEntity")
class GastosPorCategoriaEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val title: String,
    val categoriaIcon: String,
    val totalGastado: String
)
