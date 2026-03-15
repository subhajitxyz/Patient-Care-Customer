package com.real.patientcare.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.real.patientcare.data.model.PatientBasicInfoDTO
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebasePatientInfoDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun getPatientBasicInfo(): Result<PatientBasicInfoDTO> {

        val userId = firebaseAuth.currentUser?.uid
            ?: return Result.failure(IllegalStateException("User not authenticated"))

        val dataSnapshot = firestore.collection("patients")
            .document(userId)
            .collection("basic_info")
            .document("profile")
            .get()
            .await()

        if(dataSnapshot == null || !dataSnapshot.exists()) {
            IllegalStateException("Unknown Error")
        }

        val dto = dataSnapshot.toObject(PatientBasicInfoDTO::class.java)

        if (dto != null) {
            return Result.success(dto)
        } else {
            return Result.failure(
                    IllegalStateException("Failed to parse DTO")
                )
        }
    }

}