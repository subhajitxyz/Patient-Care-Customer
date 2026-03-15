package com.real.patientcare.data.repo

import com.real.patientcare.common.toDomain
import com.real.patientcare.domain.model.PatientBasicInfo
import com.real.patientcare.domain.repo.PatientInfoRepository
import javax.inject.Inject
import kotlin.mapCatching

class PatientInfoRepoImpl @Inject constructor(
    private val dataSource: FirebasePatientInfoDataSource
): PatientInfoRepository {
    override suspend fun getPatientBasicInfo(): Result<PatientBasicInfo> {
        return dataSource.getPatientBasicInfo()
            .mapCatching {
                it.toDomain()
            }
    }

}