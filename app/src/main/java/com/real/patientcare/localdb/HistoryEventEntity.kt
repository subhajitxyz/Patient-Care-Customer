package com.real.patientcare.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_events")
data class HistoryEventEntity(

    @PrimaryKey
    val eventId: String,

    val eventName: String,

    val status: Boolean,

    val timestamp: Long
)