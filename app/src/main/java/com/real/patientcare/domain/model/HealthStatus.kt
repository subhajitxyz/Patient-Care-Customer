package com.real.patientcare.domain.model

data class HealthStatus(
    val extremeBreath: HealthEvent?,
    val extremeCough: HealthEvent?,
    val heartAttack: HealthEvent?
)