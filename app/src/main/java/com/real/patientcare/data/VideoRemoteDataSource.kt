package com.real.patientcare.data

import com.google.firebase.firestore.FirebaseFirestore
import com.real.patientcare.data.model.VideoMetadata
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VideoRemoteDataSource @Inject constructor(

    private val firestore: FirebaseFirestore
) {


    suspend fun getEmergencyVideo(
        uid: String,
        emergencyName: String,
        eventId: String
    ): VideoMetadata? {

        val snapshot = firestore
            .collection("patients")
            .document(uid)
            .collection("health_info")
            .document("detection_video")
            .get()
            .await()

        val emergencyMap =
            snapshot.get(emergencyName) as? Map<*, *>
                ?: return null

        val remoteEventId =
            emergencyMap["video_event_id"] as? String
                ?: return null

        if (remoteEventId != eventId) {
            return null
        }

        return VideoMetadata(
            video_event_id = remoteEventId,
            videoUrl = emergencyMap["videoUrl"] as? String ?: ""
        )
    }
}