package com.real.patientcare.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query(
        """
        SELECT *
        FROM history_events
        WHERE timestamp >= :startMillis
        AND timestamp < :endMillis
        ORDER BY timestamp DESC
        """
    )
    fun observeHistoryForDate(
        startMillis: Long,
        endMillis: Long
    ): Flow<List<HistoryEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        events: List<HistoryEventEntity>
    )
}