package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GastosCategoriaNuevaEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val iconName: String,
    val title: String,
)
