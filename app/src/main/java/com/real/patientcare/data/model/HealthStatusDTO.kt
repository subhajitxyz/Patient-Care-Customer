package com.real.patientcare.data.model

data class HealthStatusDTO(
    val extreme_breath: HealthEventDTO? = null,
    val extreme_cough: HealthEventDTO? = null,
    val heart_attack: HealthEventDTO? = null
)