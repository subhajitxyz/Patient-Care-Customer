package com.real.patientcare.localdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CachedVideoEntity::class,  HistoryEventEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao
    abstract fun historyDao(): HistoryDao
}