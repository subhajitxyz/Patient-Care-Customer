package com.real.patientcare.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.real.patientcare.MainActivity
import com.real.patientcare.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HealthFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data

        if (data.isEmpty()) return

        val type = data["type"] ?: return
        if (type != "health_alert") return

        val title = data["title"] ?: "Health Alert"
        var body = data["body"] ?: "Abnormal health activity detected"
        val healthType = data["healthType"] ?: ""

        val timeString = data["time"]
        val formattedTime = timeString?.let {
            val timestamp = it.toLongOrNull()
            timestamp?.let { millis ->
                val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                sdf.format(Date(millis))
            }
        }

        if (formattedTime != null) {
            body += " Time: $formattedTime"
        }

        showNotification(title, body, healthType)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveTokenToFirestore(token)
    }

    private fun saveTokenToFirestore(token: String) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        Firebase.firestore
            .collection("patients")
            .document(uid)
            .collection("basic_info")
            .document("profile")
            .update("fcmTokens", FieldValue.arrayUnion(token))
    }


    private fun showNotification(
        title: String,
        message: String,
        healthType: String
    ) {

        val channelId = "health_alert_channel"

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("healthapp://alert/$healthType"),
            this,
            MainActivity::class.java
        )

        val pendingIntent = PendingIntent.getActivity(
            this,
            healthType.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Health Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for detected health problems"
            }

            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.patient_care_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationId = healthType.hashCode()

        notificationManager.notify(notificationId, notification)
    }
}
