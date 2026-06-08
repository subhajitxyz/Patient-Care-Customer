package com.real.patientcare.domain.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.real.patientcare.data.VideoRemoteDataSource
import com.real.patientcare.localdb.CachedVideoEntity
import com.real.patientcare.localdb.VideoDao
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class EmergencyVideoRepository @Inject constructor(
    private val dao: VideoDao,
    private val remoteDataSource: VideoRemoteDataSource,
    private val downloader: VideoDownloader,
    private val firebaseAuth: FirebaseAuth
) {

    fun observeVideo(eventId: String): Flow<CachedVideoEntity?> {
        return dao.observeVideo(eventId)
    }

    suspend fun loadVideo(
        emergencyName: String,
        eventId: String
    ): Result<Unit> {

        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Result.failure(IllegalStateException("User not authenticated"))

            val cached = dao.getVideo(eventId)

            if (cached != null) {

                dao.updateAccessTime(
                    eventId,
                    System.currentTimeMillis()
                )

                return Result.success(Unit)
            }

            val remote = remoteDataSource.getEmergencyVideo(
                userId,
                emergencyName = emergencyName,
                eventId
            ) ?: return Result.failure(
                Exception("Emergency video not found")
            )

            val videoUrl = remote.videoUrl.ifEmpty { return Result.failure(Exception("Video unavailable")) }

            if (remote.video_event_id != eventId) {
                return Result.failure(
                    Exception("Video verification failed")
                )
            }

            val localPath = downloader.downloadVideo(
                videoUrl,
                eventId
            )

            dao.insert(
                CachedVideoEntity(
                    eventId = eventId,
                    localPath = localPath,
                    remoteUrl = videoUrl,
                    createdAt = System.currentTimeMillis(),
                    lastAccessed = System.currentTimeMillis()
                )
            )

            maintainCacheLimit()

            return Result.success(Unit)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    private suspend fun maintainCacheLimit() {

        val count = dao.count()

        if (count <= 10) {
            return
        }

        val lru = dao.getLeastRecentlyUsed() ?: return

        File(lru.localPath).delete()

        dao.delete(lru)
    }
}