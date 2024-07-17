package pl.poznan.put.pegasus_communityedition.ui.services

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class TrackingApp: Application() {

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "tracking_channel",
            "Tracking Notifications",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}