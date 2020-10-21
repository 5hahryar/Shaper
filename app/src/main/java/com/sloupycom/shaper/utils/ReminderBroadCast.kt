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
import java.util.*

class ReminderBroadCast: BroadcastReceiver() {

    var context: Context? = null
    var mLocal: Local? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
//        DaggerDependencyComponent.create().getRemote().getDueTasksForReminder(this)
        Log.d("TAG", "reminder repo get established")
        this.context = context
        val data = Local.getInstance(context!!)
            .localDao
            .getTodayTasks(Util().getDateIndex(Calendar.getInstance()))?.value
        var notif = ""
        if (data != null) {
            for (task in data) {
                notif += task.title
            }
        }
        createNotification(notif)
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

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onDataChanged(data: ArrayList<Task>, busyDays: List<Int>) {
//        Log.d("TAG", "reminder repo on answer: $data")
//        var text: String = ""
//        if (data.size != 0) {
//            for (task in data) {
//                text += " · ${task.title}"
//            }
//            createNotification(text)
//        } else createNotification("No tasks for today!")
//
//    }

}