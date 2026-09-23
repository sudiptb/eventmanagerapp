package com.example.eventmanager.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.eventmanager.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class EventMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message);
        val channelId = "event_reminders";
        val nm =
            getSystemService(NotificationManager::class.java); if (Build.VERSION.SDK_INT >= 26) nm.createNotificationChannel(
            NotificationChannel(
                channelId,
                "Event reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        );
        val n = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(message.notification?.title ?: "Event reminder")
            .setContentText(message.notification?.body ?: "You have an upcoming event.")
            .setAutoCancel(true).build(); nm.notify(System.currentTimeMillis().toInt(), n)
    }
}
