package com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.commons.snackbar.SnackbarManager
import com.example.gastosdiariosjetapckcompose.data.core.Constants.MIN_PASS_LENGTH
import com.example.gastosdiariosjetapckcompose.data.core.Constants.PASS_PATTERN
import com.example.gastosdiariosjetapckcompose.data.di.repository.AuthFirebaseRepository
import com.example.gastosdiariosjetapckcompose.domain.model.LoginUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authFirebaseRepository: AuthFirebaseRepository) :
    ViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    private val isLoginEnable
        get() = uiState.value.isLoginEnable

    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> get() = _loginSuccess

    private val _snackbarMessage = MutableStateFlow<Int?>(null)
    val snackbarMessage: StateFlow<Int?> get() = _snackbarMessage

    private val _isEnabledButton = MutableStateFlow<Boolean?>(null)
    val isEnabledButton : StateFlow<Boolean?> get() = _isEnabledButton



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
        if (email.isEmpty() || password.isEmpty()) {
            _snackbarMessage.value = R.string.password_error
            return
        }

        authFirebaseRepository.signInWithEmailAndPassword(
            uiState.value.email,
            uiState.value.password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginSuccess.value = true
                } else {
                    _loginSuccess.value = false
                    _snackbarMessage.value = R.string.email_error
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
        return password.isValidPassword() && email.isValidEmail()
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

    fun resetLoginSuccess() {
        _loginSuccess.value = null
    }

    fun resetSnackbarMessage() {
        _snackbarMessage.value = null
    }

}