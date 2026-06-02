package com.real.patientcare.di

import android.content.Context
import com.real.patientcare.domain.repo.VideoDownloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DownloaderModule {

    @Provides
    @Singleton
    fun provideVideoDownloader(
        @ApplicationContext context: Context
    ): VideoDownloader {

        return VideoDownloader(context)
    }
}