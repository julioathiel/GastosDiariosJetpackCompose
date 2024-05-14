package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TotalGastosEntity")
data class TotalGastosEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalGastos: Double
)