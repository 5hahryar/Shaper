package com.sloupycom.shaper.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.sloupycom.shaper.R
import com.sloupycom.shaper.data.source.local.Local
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.view.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ReminderBroadCast: BroadcastReceiver() {

    var context: Context? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(Constant.TAG, "ALARM: Reminder broadcast onReceive")
        this.context = context
        var data: List<Task>? = null

        runBlocking {
            val job = GlobalScope.launch {
                data = Local.getInstance(context!!)
                    .localDao
                    .getReminderTasks(Util().getDateIndex(Calendar.getInstance()))
            }

            job.join()
            var notif = ""
            if (data != null && data!!.isNotEmpty()) {
                for (task in data!!) {
                    notif += " · ${task.title}"
                }
                createNotification(notif)
            }
            Log.d(Constant.TAG, "ALARM: notification created with data size: ${data?.size}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(contentText: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, Constant.RC_MAIN_ACTIVITY, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = Notification.Builder(context, Constant.ID_NOTIFICATION_CHANNEL)
            .setContentTitle("Today's Tasks")
            .setSmallIcon(R.drawable.ic_shaper)
            .setStyle(Notification.BigTextStyle().bigText(contentText))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context!!).notify(Constant.ID_NOTIFICATION, notification)
    }
}