package com.example.gastosdiariosjetapckcompose.domain.model

data class NotificationProgramadaModel(
    val idNotification: Int,
    val smallIcon: Int,
    val title: String,
    val text: String,
    val largeIconResId: Int,
    val priority: Int
)