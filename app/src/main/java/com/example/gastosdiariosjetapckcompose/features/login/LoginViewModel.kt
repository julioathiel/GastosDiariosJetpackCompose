package com.example.gastosdiariosjetapckcompose.features.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor():ViewModel() {

    //variable para saber si se esta cargando los datos
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private var _email = MutableLiveData<String>()
    val email: LiveData<String> = _email
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    //controlando el boton del login por si es falso o no
    // para que ingrrese a la siguiente pantalla
    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isLoginEnable.value = enabledLogin(email, password)
    }

    private fun enabledLogin(email: String, password: String): Boolean {
        //si la direccion de email es valida, que contenga un @, un punto, etc
        // y si el valor del password es mayor a 6
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 6
    }


}