package com.real.patientcare.data.repo

import com.real.patientcare.common.toDomain
import com.real.patientcare.data.FirebaseHealthDataSource
import com.real.patientcare.domain.model.HealthStatus
import com.real.patientcare.domain.repo.HealthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HealthRepoImpl @Inject constructor(
    private val firebaseHealthDataSource: FirebaseHealthDataSource
): HealthRepository {

    override fun observeHealth(): Flow<Result<HealthStatus>> {
        return firebaseHealthDataSource
            .observeUserHealthStatus()
            .map { result ->
                result.mapCatching { dto ->
                    dto.toDomain()
                }
            }
    }
}