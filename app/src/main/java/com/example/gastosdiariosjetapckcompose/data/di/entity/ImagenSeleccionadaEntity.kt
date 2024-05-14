package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ImagenSeleccionadaEntity")
data class ImagenSeleccionadaEntity(
    @PrimaryKey
    val id: Int,
    val uri: String, // Aquí se almacena la URI como String
    val isChecked: Boolean = true
)
