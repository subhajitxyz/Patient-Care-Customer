package com.real.patientcare.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.real.patientcare.data.model.HealthStatusDTO
import com.real.patientcare.ui.presentation.login.LoginScreen
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseHealthDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    fun observeUserHealthStatus(): Flow<Result<HealthStatusDTO>> = callbackFlow {

        val user = firebaseAuth.currentUser

        if (user == null) {
            trySend(Result.failure(IllegalStateException("User not authenticated")))
            close()
            return@callbackFlow
        }

        val listener = firestore
            .collection("patients")
            .document(user.uid)
            .collection("health_info")
            .document("today")
            .addSnapshotListener { snapshot, error ->

                // Firestore error
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                if (snapshot == null || !snapshot.exists()) {
                    trySend(Result.success(HealthStatusDTO()))
                    return@addSnapshotListener
                }

                val dto = snapshot.toObject(HealthStatusDTO::class.java)

                if (dto != null) {
                    trySend(Result.success(dto))
                } else {
                    trySend(
                        Result.failure(
                            IllegalStateException("Failed to parse HealthStatusDTO")
                        )
                    )
                }
            }

        awaitClose {
            listener.remove()
        }
    }

}