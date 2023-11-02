package com.example.gastosdiariosjetapckcompose.domain.model

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.DateFormat
import java.util.Date

data class MovimientosModel(
    val id:Int = System.currentTimeMillis().hashCode() ,
    val iconResourceName: String,
    val title: String,
    val subTitle: String,
    val cash: String,
    val select:Boolean,
    val date: String = DateFormat.getDateInstance().format(Date())
)
