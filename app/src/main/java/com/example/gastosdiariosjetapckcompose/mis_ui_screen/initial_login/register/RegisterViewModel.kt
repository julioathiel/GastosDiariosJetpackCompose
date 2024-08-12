package com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.register

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.commons.snackbar.SnackbarManager
import com.example.gastosdiariosjetapckcompose.data.core.Constants.MIN_PASS_LENGTH
import com.example.gastosdiariosjetapckcompose.data.core.Constants.PASS_PATTERN
import com.example.gastosdiariosjetapckcompose.domain.model.LoginUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password


    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick() {
        firebaseAuth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("registro", "registro exitoso")
                } else {
                    Log.d("registro", "registro imposible")
                }
            }
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

    }


    // Método para verificar si el correo electrónico es válido
    fun enabledButtonRegister(): Boolean {
        //si la direccion de email es valida, que contenga un @, un punto, etc
        return email.isValidEmail() && password.isValidPassword()
    }

    private fun String.isValidEmail(): Boolean {
        return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun String.isValidPassword(): Boolean {
        return this.isNotBlank() &&
                this.length >= MIN_PASS_LENGTH &&
                Pattern.compile(PASS_PATTERN).matcher(this).matches()
    }

    private fun String.passwordMatches(repeated: String): Boolean {
        return this == repeated
    }

    fun String.idFromParameter(): String {
        return this.substring(1, this.length - 1)
    }
}