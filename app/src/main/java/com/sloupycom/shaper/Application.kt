package com.sloupycom.shaper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.sloupycom.shaper.utils.Constant
import com.sloupycom.shaper.utils.Util
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class Application: Application() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch {
            if (!Util().readBooleanPreferences(applicationContext, Constant.SHARED_PREFS_CHANNEL)) {
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(
                    NotificationChannel(Constant.ID_NOTIFICATION_CHANNEL,
                        getString(R.string.reminder),
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                )
            }
        }
    }
}