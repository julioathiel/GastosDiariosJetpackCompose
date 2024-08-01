package com.example.gastosdiariosjetapckcompose.mis_ui_screen.forgotpassword

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor():ViewModel() {

    // LiveData para el correo electrónico de recuperacion de contraseña
    private var _forgotEmail = MutableLiveData<String>()
    val forgotEmail:LiveData<String> = _forgotEmail

    // LiveData para controlar la habilitación del botón de recuperacion de contraseña
    private var _isForgotEnabled = MutableLiveData<Boolean>()
    val isForgotEnabled:LiveData<Boolean> = _isForgotEnabled


    fun onForgotChanged(emailForgot:String){
        _forgotEmail.value = emailForgot
        _isForgotEnabled.value = enabledButtonForgot(emailForgot)
    }

    private fun enabledButtonForgot(emailForgot: String): Boolean {
        //si la direccion de email es valida, que contenga un @, un punto, etc
        return Patterns.EMAIL_ADDRESS.matcher(emailForgot).matches()
    }
}