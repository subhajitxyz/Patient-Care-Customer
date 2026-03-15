package com.real.patientcare.domain.model

data class HealthEvent(
    val accuracy: String,
    val seen: Boolean,
    val date: String,
    val time: String
)
