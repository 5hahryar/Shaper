package com.sloupycom.shaper.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.sloupycom.shaper.R

class MyAlarmReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        createNotificationChannel(context!!)
        var builder = NotificationCompat.Builder(context, "AlarmTestChannel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Title for shaper alarm")
            .setContentText("alarm went off")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "AlarmTestChannel"
            val descriptionText = "Test desc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("AlarmTestChannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
