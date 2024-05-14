package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TotalIngresosEntity")
data class TotalIngresosEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalIngresos: Double
)