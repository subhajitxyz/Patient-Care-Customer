package com.real.patientcare.common

import android.util.Log
import com.real.patientcare.data.model.HealthEventDTO
import com.real.patientcare.data.model.HealthStatusDTO
import com.real.patientcare.data.model.PatientBasicInfoDTO
import com.real.patientcare.domain.model.HealthEvent
import com.real.patientcare.domain.model.HealthStatus
import com.real.patientcare.domain.model.PatientBasicInfo
import java.text.SimpleDateFormat
import java.util.Locale

private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
private val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

fun HealthEventDTO.toDomain(): HealthEvent {

    val dateObj = time?.toDate()

    val date = dateObj?.let { dateFormatter.format(it) } ?: ""
    val timeString = dateObj?.let { timeFormatter.format(it) } ?: ""

    return HealthEvent(
        accuracy = accuracy,
        seen = seen,
        date = date,
        time = timeString
    )
}

fun HealthStatusDTO.toDomain(): HealthStatus {
    return HealthStatus(
        extremeBreath = extreme_breath?.toDomain(),
        extremeCough = extreme_cough?.toDomain(),
        heartAttack = heart_attack?.toDomain()
    )
}

fun PatientBasicInfoDTO.toDomain(): PatientBasicInfo {

    val dateObj = createdAt?.toDate()
    val date = dateObj?.let { dateFormatter.format(it) } ?: ""
    val timeString = dateObj?.let { timeFormatter.format(it) } ?: ""

    val dob = dob?.let { dateFormatter.format(it) } ?: ""

    return PatientBasicInfo(
        uid = uid,
        name = name,
        email = email,
        phone = phone,
        role = role,
        dob = dob,
        createdDate = date,
        createdTime = timeString
    )
}