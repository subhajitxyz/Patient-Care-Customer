package com.real.patientcare.data.model

import com.google.firebase.Timestamp

data class HealthEventDTO(
    val accuracy: String = "",
    val seen: Boolean = false,
    val time: Timestamp? = null
)