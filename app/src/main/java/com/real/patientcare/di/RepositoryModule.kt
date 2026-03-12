package com.real.patientcare.di

import com.real.patientcare.data.repo.LoginRepoImpl
import com.real.patientcare.domain.repo.LoginRepository
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
}