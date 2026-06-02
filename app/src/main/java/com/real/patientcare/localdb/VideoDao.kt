package com.real.patientcare.localdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("SELECT * FROM cached_videos WHERE eventId = :eventId")
    fun observeVideo(eventId: String): Flow<CachedVideoEntity?>

    @Query("SELECT * FROM cached_videos WHERE eventId = :eventId")
    suspend fun getVideo(eventId: String): CachedVideoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: CachedVideoEntity)

    @Query("SELECT COUNT(*) FROM cached_videos")
    suspend fun count(): Int

    @Query(
        "SELECT * FROM cached_videos ORDER BY lastAccessed ASC LIMIT 1"
    )
    suspend fun getLeastRecentlyUsed(): CachedVideoEntity?

    @Delete
    suspend fun delete(video: CachedVideoEntity)

    @Query(
        "UPDATE cached_videos SET lastAccessed = :time WHERE eventId = :eventId"
    )
    suspend fun updateAccessTime(eventId: String, time: Long)
}