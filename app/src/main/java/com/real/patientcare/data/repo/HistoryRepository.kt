package com.real.patientcare.data.repo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.real.patientcare.localdb.HistoryDao
import com.real.patientcare.localdb.HistoryEventEntity
import com.real.patientcare.ui.presentation.calender.HistoryEventUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dao: HistoryDao
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeHistory(
        date: String
    ): Flow<List<HistoryEventUi>> {

        val localDate = LocalDate.parse(date)

        val startMillis = localDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val endMillis = localDate
            .plusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return dao.observeHistoryForDate(
            startMillis = startMillis,
            endMillis = endMillis
        ).map { entities ->

            entities
                .filter { it.eventName != "normal" }
                .map {
                    HistoryEventUi(
                        eventId = it.eventId,
                        eventName = when (it.eventName) {
                            "heart_attack" -> "Heart Attack"
                            "extreme_cough" -> "Extreme Cough"
                            else -> it.eventName
                        },
                        status = if (it.status) {
                            "Detected"
                        } else {
                            "Normal"
                        },
                        timestamp = formatTime(it.timestamp)
                    )
                }
        }
    }

    private fun formatTime(
        timestamp: Long
    ): String {

        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(Date(timestamp))
    }

    suspend fun syncHistory() {

        val uid = auth.currentUser?.uid ?: return

        val snapshot = firestore
            .collection("patients")
            .document(uid)
            .collection("health_info")
            .document("history")
            .collection("history_list")
            .get()
            .await()

        val events = snapshot.documents.mapNotNull { doc ->

            Log.d("subhat", doc.toString())

            val eventId = doc.getString("eventId")
            val eventName = doc.getString("eventName")
            val status = doc.getBoolean("status")
            val timestamp = doc.getTimestamp("timestamp")

            if (
                eventId == null ||
                eventName == null ||
                status == null ||
                timestamp == null
            ) {
                null
            } else {

                HistoryEventEntity(
                    eventId = eventId,
                    eventName = eventName,
                    status = status,
                    timestamp = timestamp.toDate().time
                )
            }
        }

        dao.insertAll(events)
    }
}
