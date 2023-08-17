package com.example.notifyme

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.notifyme.NotificationListener.Companion.notificationsList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var notificationListView: ListView
    private lateinit var adapter: NotificationAdapter

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateListView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationListView = findViewById(R.id.notificationListView)
        adapter = NotificationAdapter(this)
        notificationListView.adapter = adapter

        // Check if the notification listener service is enabled
        if (!isNotificationListenerEnabled()) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        // ... Initialize views and check notification listener service ...

        // Register the broadcast receiver
        val intentFilter = IntentFilter(NotificationListener.ACTION_NOTIFICATION)
        registerReceiver(notificationReceiver, intentFilter)
    }

    private fun isNotificationListenerEnabled(): Boolean {
        val packageName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat?.contains(packageName) == true
    }

    // Other methods...

    private fun updateListView() {
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationReceiver)
    }


    // Custom Adapter for the list view
    private inner class NotificationAdapter(private val context: Context) : BaseAdapter() {

        override fun getCount(): Int {
            return NotificationListener.notificationsList.size
        }

        override fun getItem(position: Int): StatusBarNotification {
            return NotificationListener.notificationsList[position]
        }

        override fun getItemId(p0: Int): Long {
            return 0;
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.list_item_notification, parent, false)

            val sbn = getItem(position)

            val appNameTextView: TextView = view.findViewById(R.id.notificationAppName)
            val notificationTextTextView: TextView = view.findViewById(R.id.notificationText)
            val notificationTimeTextView: TextView = view.findViewById(R.id.notificationTime)

            // Set data to views based on StatusBarNotification (sbn) values
            appNameTextView.text = sbn.packageName
            notificationTextTextView.text = sbn.notification.extras.getString(Notification.EXTRA_TEXT)
            notificationTimeTextView.text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                Date(sbn.postTime)
            )

            return view
        }
    }
}
