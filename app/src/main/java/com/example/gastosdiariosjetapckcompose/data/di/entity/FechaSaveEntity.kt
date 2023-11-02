package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FechaSaveEntity(
    @PrimaryKey
    val id: Int,
    val fechaSave: String,
    val isSelected: Boolean = true
)