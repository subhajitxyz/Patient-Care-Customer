package com.real.patientcare.domain.model

data class PatientBasicInfo(
    val uid: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String,
    val dob: String,        // formatted date string for UI
    val createdDate: String,
    val createdTime: String
)