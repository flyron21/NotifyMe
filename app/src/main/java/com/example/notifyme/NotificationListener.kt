package com.example.notifyme

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationListener : NotificationListenerService() {
    companion object {
        const val ACTION_NOTIFICATION = "com.example.notifyme.ACTION_NOTIFICATION"
         val notificationsList = mutableListOf<StatusBarNotification>()
    }


    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        // Add the notification to the list
        notificationsList.add(0, sbn) // Insert at the beginning for most recent at the top

        // Update the adapter
        val intent = Intent(ACTION_NOTIFICATION)
        sendBroadcast(intent)
    }

    // Other methods...
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)

        // Handle removed notification
    }


}

