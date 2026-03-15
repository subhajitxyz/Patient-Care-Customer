package com.real.patientcare.ui.profile

data class ProfileUiState(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "",
    val dob: String = "",        // formatted date string for UI
    val createdDate: String = "",
    val createdTime: String = "",
    val isLoading: Boolean = false
)