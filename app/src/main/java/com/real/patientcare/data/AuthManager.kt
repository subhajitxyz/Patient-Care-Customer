package com.real.patientcare.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun loginUser(email: String, password: String): AuthResult? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    fun checkUserLoginStatus(): Boolean {
        return firebaseAuth.currentUser != null
    }

}