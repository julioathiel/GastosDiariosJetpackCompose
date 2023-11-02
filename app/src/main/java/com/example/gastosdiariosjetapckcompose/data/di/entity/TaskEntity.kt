package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasksEntity")
data class TaskEntity(
    @PrimaryKey
    val id: Int,
    val task: String,
    val select: Boolean = false
)
