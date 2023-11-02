package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel

@Entity(tableName = "currentMoneyEntity")
data class CurrentMoneyEntity(
    @PrimaryKey
    val id: Int,
    val money: Double,
    val isChecked:Boolean = true
)
