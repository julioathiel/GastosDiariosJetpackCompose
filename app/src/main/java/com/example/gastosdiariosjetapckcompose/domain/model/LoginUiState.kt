package com.example.gastosdiariosjetapckcompose.domain.model

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword:String = "",
    val isLoginEnable:Boolean = false
)