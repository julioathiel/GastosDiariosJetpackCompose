package com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.user_profile

import androidx.lifecycle.ViewModel
import com.example.gastosdiariosjetapckcompose.data.di.repository.AuthFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val repository: AuthFirebaseRepository) :
    ViewModel() {
    fun getNameUser(): String? {
        return repository.getCurrentUser()?.email
    }

    fun deleteUser() {
        //elimina la cuenta
        repository.deleteUser()
    }

    fun signOut() {
        //cierra sesion
        repository.signOut()
    }

    fun signGoogle(){
        repository.signInWithGoogle("token")

    }
}