package com.example.gastosdiariosjetapckcompose.features.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class RegisterViewModel @Inject constructor():ViewModel() {

    // LiveData para el correo electrónico de registro
    private var _registerEmail = MutableLiveData<String>()
    val registerEmail:LiveData<String>  = _registerEmail

    // LiveData para controlar la habilitación del botón de registro
    private val _isRegisterEnable = MutableLiveData<Boolean>()
    val isRegisterEnable: LiveData<Boolean>  = _isRegisterEnable

    // Método para actualizar el correo electrónico de registro y verificar la habilitación del botón
    fun onRegisterChanged(registerEmail: String) {
        _registerEmail.value = registerEmail
        _isRegisterEnable.value = enabledButtonRegister(registerEmail)
    }
    // Método para verificar si el correo electrónico es válido
    private fun enabledButtonRegister(email: String): Boolean {
        //si la direccion de email es valida, que contenga un @, un punto, etc
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}