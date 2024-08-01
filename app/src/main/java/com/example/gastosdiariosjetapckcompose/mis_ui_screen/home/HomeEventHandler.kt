package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home

sealed class HomeEventHandler {
    data class GetCurrentMoney(val money: String) : HomeEventHandler()
    data class SetCurrentMoney(val money: String) : HomeEventHandler()
}