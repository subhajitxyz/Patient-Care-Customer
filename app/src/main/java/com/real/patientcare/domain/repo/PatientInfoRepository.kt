package com.real.patientcare.domain.repo

import com.real.patientcare.domain.model.PatientBasicInfo

interface PatientInfoRepository {
    suspend fun getPatientBasicInfo(): Result<PatientBasicInfo>
}