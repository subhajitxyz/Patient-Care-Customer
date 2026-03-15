package com.real.patientcare.di

import com.real.patientcare.data.repo.HealthRepoImpl
import com.real.patientcare.data.repo.LoginRepoImpl
import com.real.patientcare.data.repo.PatientInfoRepoImpl
import com.real.patientcare.domain.repo.HealthRepository
import com.real.patientcare.domain.repo.LoginRepository
import com.real.patientcare.domain.repo.PatientInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideLoginRepository(
        loginRepoImpl: LoginRepoImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun provideHeathRepository(
        healthRepoImpl: HealthRepoImpl
    ): HealthRepository

    @Binds
    @Singleton
    abstract fun providePatientInfoRepository(
        patientInfoRepoImpl: PatientInfoRepoImpl
    ): PatientInfoRepository
}