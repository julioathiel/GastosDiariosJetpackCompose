package com.example.gastosdiariosjetapckcompose.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.gastosdiariosjetapckcompose.R


data class CardInfoModel(val title: String, @get:DrawableRes val icon: Int, val color: Color)

val listCardInfo = listOf<CardInfoModel>(
    CardInfoModel(title = "Registro", icon = R.drawable.ic_barra_filled, color = Color.LightGray)
)


