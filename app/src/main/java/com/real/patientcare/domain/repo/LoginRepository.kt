package com.real.patientcare.domain.repo

import com.google.firebase.auth.AuthResult

interface LoginRepository {
    suspend fun loginUser(email: String, password: String): AuthResult?

    fun checkUserLoginStatus(): Boolean

    suspend fun getFcmToken(): String?
}