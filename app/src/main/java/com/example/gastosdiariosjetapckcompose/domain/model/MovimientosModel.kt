package com.example.gastosdiariosjetapckcompose.domain.model

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.DateFormat
import java.util.Date
import java.util.UUID

data class MovimientosModel(
    val id: Int = 0,
    val iconResourceName: String,
    val title: String,
    var subTitle: String,
    var cash: String,
    val select:Boolean,
    val date: String = DateFormat.getDateInstance().format(Date())
)