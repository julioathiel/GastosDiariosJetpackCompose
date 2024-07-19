package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BarDataEntity(
@PrimaryKey (autoGenerate = true)
val id:Int = 0,
val value: Float, // Valor numérico que representa la altura de la barra
val month: String,
var money:String
)

