package com.real.patientcare.localdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CachedVideoEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao
}