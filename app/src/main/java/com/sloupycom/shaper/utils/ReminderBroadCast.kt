package com.sloupycom.shaper.utils

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.sloupycom.shaper.R
import com.sloupycom.shaper.database.Local
import com.sloupycom.shaper.model.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ReminderBroadCast: BroadcastReceiver() {

    var context: Context? = null
    var mLocal: Local? = null

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
            if (data != null) {
                for (task in data!!) {
                    notif += " · ${task.title}"
                }
            }
            createNotification(notif)
            Log.d(Constant.TAG, "ALARM: notification created with data size: ${data?.size}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(contentText: String) {
        val notification = Notification.Builder(context, Constant.ID_NOTIFICATION_CHANNEL)
            .setContentTitle("Today's Tasks")
            .setSmallIcon(R.drawable.ic_shaper)
            .setStyle(Notification.BigTextStyle().bigText(contentText))
            .build()
        NotificationManagerCompat.from(context!!).notify(Constant.ID_NOTIFICATION, notification)
    }
}