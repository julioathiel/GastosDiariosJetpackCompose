package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home

sealed class MainState {
    data class Success(val user:Int): MainState()
    object Loading:MainState()
    object  Error:MainState()
}