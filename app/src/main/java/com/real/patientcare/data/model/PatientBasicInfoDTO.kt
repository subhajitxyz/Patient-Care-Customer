package com.real.patientcare.data.model

import com.google.firebase.Timestamp

data class PatientBasicInfoDTO(
    val createdAt: Timestamp? = null,
    val dob: Long = 0L,
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val role: String = "",
    val uid: String = ""
)