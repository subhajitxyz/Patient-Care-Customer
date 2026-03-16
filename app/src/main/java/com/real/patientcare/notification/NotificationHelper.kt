//package com.real.patientcare.notification
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.real.patientcare.MainActivity
//import com.real.patientcare.R
//
//object NotificationHelper {
//
//    private const val CHANNEL_ID = "health_alert_channel"
//
//    fun showHealthAlertNotification(
//        context: Context,
//        title: String,
//        body: String,
//        healthType: String,
//        accuracy: String,
//        time: String
//    ) {
//
//        createChannel(context)
//
//        val intent = Intent(context, MainActivity::class.java).apply {
//            putExtra("healthType", healthType)
//            putExtra("accuracy", accuracy)
//            putExtra("time", time)
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.user_profile_icon)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .build()
//
//        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
//
//        NotificationManagerCompat.from(context)
//            .notify(System.currentTimeMillis().toInt(), notification)
//    }
//
//    private fun createChannel(context: Context) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Health Alerts",
//                NotificationManager.IMPORTANCE_HIGH
//            ).apply {
//                description = "Notifications for detected health problems"
//            }
//
//            val manager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            manager.createNotificationChannel(channel)
//        }
//    }
//}