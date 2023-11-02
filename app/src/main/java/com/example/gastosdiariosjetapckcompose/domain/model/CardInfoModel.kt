package com.example.gastosdiariosjetapckcompose.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.Color
import com.example.gastosdiariosjetapckcompose.R


data class CardInfoModel(val title: String, @get:DrawableRes val icon: Int, val color: Color)

val listCardInfo = listOf<CardInfoModel>(
    CardInfoModel(title = "Task", icon = R.drawable.ic_add_task, color = Color.Red),
    CardInfoModel(title = "Porcentaje", icon = R.drawable.ic_porcentaje, color = Color.Blue),
    CardInfoModel(title = "Regalos", icon = R.drawable.ic_regalos, color = Color.Green),
    CardInfoModel(title = "Hotel", icon = R.drawable.ic_hotel, color = Color.Yellow)
)


