package com.example.gastosdiariosjetapckcompose.data.di.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.Date
@Entity
data class MovimientosEntity (
        @PrimaryKey
        val id:Int,
        val iconResourceName: String,
        val title: String,
        val subTitle: String,
        val cash: String,
        val select:Boolean,
        val date: String = DateFormat.getDateInstance().format(Date())
)