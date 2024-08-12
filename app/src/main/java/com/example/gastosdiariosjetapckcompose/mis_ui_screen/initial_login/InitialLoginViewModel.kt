package com.example.gastosdiariosjetapckcompose.mis_ui_screen.initial_login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.data.core.Constants.RC_SIGN_IN
import com.example.gastosdiariosjetapckcompose.data.di.repository.AuthFirebaseRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class InitialLoginViewModel @Inject constructor(
    private val authFirebaseRepository: AuthFirebaseRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(false)
    val state: StateFlow<Boolean> = _state

    fun eventHandler(e: EventHandlerlLogin) {
        when (e) {
            is EventHandlerlLogin.ContinuarConPhone -> iniciarConPhone()
            is EventHandlerlLogin.ContinuarConGoogle -> iniciarConGoogle(e.value)
            is EventHandlerlLogin.ContinuarConFacebok -> iniciarConFacebook()
        }
    }

    private fun iniciarConPhone() {
        Log.d("continuarCon", "Phone")
    }

    private fun iniciarConGoogle(googleToken:String) {
    authFirebaseRepository.signInWithGoogle(googleToken)
    }

    private fun iniciarConFacebook() {
        Log.d("continuarCon", "Facebook")
    }
}