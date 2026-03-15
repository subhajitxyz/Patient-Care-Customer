package com.real.patientcare.domain.repo

import android.os.health.HealthStats
import com.real.patientcare.data.model.HealthStatusDTO
import com.real.patientcare.domain.model.HealthStatus
import kotlinx.coroutines.flow.Flow

interface HealthRepository {
    fun observeHealth(): Flow<Result<HealthStatus>>
}