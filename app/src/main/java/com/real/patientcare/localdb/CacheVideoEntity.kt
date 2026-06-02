package com.real.patientcare.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_videos")
data class CachedVideoEntity(

    @PrimaryKey
    val eventId: String,

    val localPath: String,

    val remoteUrl: String,

    val createdAt: Long,

    val lastAccessed: Long
)