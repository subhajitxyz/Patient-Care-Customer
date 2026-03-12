package com.real.patientcare.data.repo

import com.google.firebase.auth.AuthResult
import com.real.patientcare.data.AuthManager
import com.real.patientcare.domain.repo.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepoImpl @Inject constructor(
    private val authManager: AuthManager
): LoginRepository {
    override suspend fun loginUser(email: String, password: String): AuthResult? {
        return authManager.loginUser(email, password)
    }

    override fun checkUserLoginStatus(): Boolean {
        return authManager.checkUserLoginStatus()
    }
}