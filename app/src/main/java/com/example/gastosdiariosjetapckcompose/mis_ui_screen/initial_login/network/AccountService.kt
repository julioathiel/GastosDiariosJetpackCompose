package com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.network

import com.google.firebase.auth.EmailAuthProvider

interface AccountService {
    fun createAnonymousAccount(onResult: (Throwable?) -> Unit)
    fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
}