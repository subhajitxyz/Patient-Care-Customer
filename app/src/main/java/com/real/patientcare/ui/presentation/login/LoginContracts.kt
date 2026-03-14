package com.real.patientcare.ui.presentation.login

interface LoginContracts {

    data class State(
        var email: String = "",
        var password: String = "",
        var emailError: String? = null,
        var passwordError: String? = null,
        var isLoading: Boolean = false
    )

    sealed class Intent {
        data class EmailChanged(val email: String): Intent()
        data class PasswordChanged(val password: String): Intent()
        object LoginClicked: Intent()

    }

    sealed class Effect {
        data class ShowToast(val message: String): Effect()
        object NavigateToDashboard: Effect()
    }
}