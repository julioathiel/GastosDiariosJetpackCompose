package com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login

sealed class EventHandlerlLogin {
    data class ContinuarConPhone(val value:Boolean):EventHandlerlLogin()
    data class ContinuarConGoogle(val value: String):EventHandlerlLogin()
    data class ContinuarConFacebok(val value:Boolean):EventHandlerlLogin()
}