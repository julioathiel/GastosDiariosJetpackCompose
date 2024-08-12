package com.example.gastosdiariosjetapckcompose.data.di.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.TwitterAuthProvider
import javax.inject.Inject

class AuthFirebaseRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {
    /*
    Permite autenticar al usuario utilizando un token de Google.
     Este método se utiliza cuando un usuario elige iniciar sesión con su cuenta de Google en tu aplicación.
     */
    fun signInWithGoogle(googleToken: String): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(googleToken, null)
        return firebaseAuth.signInWithCredential(credential)
    }

    /*
    Permite autenticar al usuario utilizando un token de Facebook.
     Se usa cuando un usuario decide iniciar sesión con su cuenta de Facebook en tu aplicación.
    */
    fun signInWithFacebook(facebookToken: String): Task<AuthResult> {
        val credential = FacebookAuthProvider.getCredential(facebookToken)
        return firebaseAuth.signInWithCredential(credential)
    }

    /*
    Permite autenticar al usuario utilizando tokens de Twitter.
     Se utiliza cuando un usuario elige iniciar sesión con su cuenta de Twitter en tu aplicación.
     */
    fun signInWithTwitter(twitterToken: String, twitterSecret: String): Task<AuthResult> {
        val credential = TwitterAuthProvider.getCredential(twitterToken, twitterSecret)
        return firebaseAuth.signInWithCredential(credential)
    }

    /*
    Permite al usuario iniciar sesión de forma anónima, sin la necesidad de crear una cuenta.
     Es útil para permitir a los usuarios acceder a ciertas funciones de la aplicación sin registrarse.
     */
    fun signInAnonymously(): Task<AuthResult> {
        return firebaseAuth.signInAnonymously()
    }

    /*
    Crea un usuario en Firebase Authentication utilizando una dirección de correo electrónico y una contraseña.
     Este método es para el registro de nuevos usuarios en tu aplicación.
     */
    fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }


    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    /*
    Envía un correo electrónico al usuario con un enlace para restablecer su contraseña.
     Es útil cuando un usuario olvida su contraseña y necesita restablecerla.
     */
    fun sendPasswordResetEmail(email: String): Task<Void> {
        return firebaseAuth.sendPasswordResetEmail(email)
    }

    /*
    Permite autenticar al usuario utilizando un token personalizado.
     Este método se usa en casos de autenticación personalizada o específica de la aplicación.
     */
    fun signInWithCustomToken(customToken: String): Task<AuthResult> {
        return firebaseAuth.signInWithCustomToken(customToken)
    }

    //Cierra la sesión del usuario actual, lo desconecta de la aplicación.
    fun signOut() {
        firebaseAuth.signOut()
    }

    /*
    Elimina la cuenta del usuario actual.
    Es importante tener en cuenta que esta acción es irreversible y eliminará permanentemente la cuenta del usuario
     */
    fun deleteUser(): Task<Void>? {
        val user = firebaseAuth.currentUser
        return user?.delete()?.addOnSuccessListener {
            signOut()
            Log.d("deleteUser","Usuario eliminado correctamente")
        }?.addOnFailureListener { exception ->
            when (exception) {
                is FirebaseAuthInvalidUserException -> {
                    // El usuario no está autenticado o la cuenta ya ha sido eliminada
                    Log.d("deleteUser","El usuario no está autenticado o la cuenta ya ha sido eliminada")
                }
                is FirebaseAuthRecentLoginRequiredException -> {
                    // Se requiere una autenticación reciente para eliminar la cuenta
                    Log.d("deleteUser","Se requiere una autenticación reciente para eliminar la cuenta")
                }
                else -> {
                    // Otro tipo de error
                    Log.d("deleteUser","Error al eliminar la cuenta: ${exception.message}")
                }
            }
        }
    }

    // Otros métodos de autenticación personalizada o específica
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}