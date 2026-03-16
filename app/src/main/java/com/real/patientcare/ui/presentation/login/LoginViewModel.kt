package com.real.patientcare.ui.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.real.patientcare.domain.repo.LoginRepository
import com.real.patientcare.domain.repo.PatientInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val patientInfoRepository: PatientInfoRepository
): ViewModel() {

    private val _state = MutableStateFlow(LoginContracts.State())
    val state: StateFlow<LoginContracts.State> = _state
    private val _effect = Channel<LoginContracts.Effect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: LoginContracts.Intent) {
        when (intent) {
            is LoginContracts.Intent.EmailChanged -> {
                _state.update {
                    it.copy(email = intent.email, emailError = null)
                }
            }

            is LoginContracts.Intent.PasswordChanged -> {
                _state.update {
                    it.copy(password = intent.password, passwordError = null)
                }
            }

            LoginContracts.Intent.LoginClicked -> {
                login()
            }
        }

    }

    private fun login() {
        val email = state.value.email.trim()
        val password = state.value.password.trim()
        if (!checkUserLoginCredentialFormat(email, password)) return

        // we are using viewmodelscope.launch which runs on main thread. beacause firebase is already async.
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                val authResult = loginRepository.loginUser(email, password)
                val fcmToken = loginRepository.getFcmToken()

                if (authResult?.user != null && fcmToken != null) {
                    // add fcm token in firebase
                    patientInfoRepository.updateFcmToken(
                        uid = authResult.user!!.uid,
                        fcmToken = fcmToken
                    )
                    _effect.send(LoginContracts.Effect.NavigateToDashboard)
                } else {
                    _effect.send(
                        LoginContracts.Effect.ShowToast("Login Failed.")
                    )
                }

                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _effect.send(LoginContracts.Effect.ShowToast("Unknown Error."))
            }
        }
    }

    private fun checkUserLoginCredentialFormat(
        email: String,
        password: String
    ): Boolean {

        var isValid = true

        // Reset previous errors
        _state.update {
            it.copy(
                emailError = null,
                passwordError = null
            )
        }

        if (email.isBlank()) {
            _state.update {
                it.copy(emailError = "Email cannot be empty")
            }
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update {
                it.copy(emailError = "Please enter a valid email address")
            }
            isValid = false
        }

        if (password.isBlank()) {
            _state.update {
                it.copy(passwordError = "Password cannot be empty")
            }
            isValid = false
        } else if (password.length < 8) {
            _state.update {
                it.copy(passwordError = "Password must be at least 8 characters")
            }
            isValid = false
        }

        return isValid
    }
}