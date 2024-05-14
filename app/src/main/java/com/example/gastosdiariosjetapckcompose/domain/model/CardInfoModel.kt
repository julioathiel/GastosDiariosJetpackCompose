package com.example.gastosdiariosjetapckcompose.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.gastosdiariosjetapckcompose.R


data class CardInfoModel(val title: String, @get:DrawableRes val icon: Int, val color: Color)

val listCardInfo = listOf<CardInfoModel>(
    CardInfoModel(title = "Registro", icon = R.drawable.ic_tabla, color = Color.LightGray)
)


