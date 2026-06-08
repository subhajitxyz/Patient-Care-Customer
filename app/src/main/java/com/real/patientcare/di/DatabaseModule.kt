package com.real.patientcare.di

import android.content.Context
import androidx.room.Room
import com.real.patientcare.localdb.AppDatabase
import com.real.patientcare.localdb.HistoryDao
import com.real.patientcare.localdb.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "patient_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideVideoDao(
        database: AppDatabase
    ): VideoDao {

        return database.videoDao()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(
        database: AppDatabase
    ): HistoryDao {

        return database.historyDao()
    }
}