package com.sloupycom.shaper.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import com.sloupycom.shaper.R
import java.text.SimpleDateFormat
import java.util.*

class Util(application: Application) : AndroidViewModel(application) {

    /**Values**/
    private val mCalendar: Calendar = Calendar.getInstance()
    private val mContext = getApplication<Application>().applicationContext

    /**
     * Get date in a list, format: [dd, MM, yyyy]
     */
    @SuppressLint("SimpleDateFormat")
    fun getTodayDateIndex(): List<Int> {
        return listOf(
            SimpleDateFormat("dd").format(mCalendar.time).toInt(),
            SimpleDateFormat("MM").format(mCalendar.time).toInt(),
            SimpleDateFormat("yyyy").format(mCalendar.time).toInt()
        )
    }

    /**
     * Get date in a string by custom format
     */
    @SuppressLint("SimpleDateFormat")
    fun getDate(pattern: String): String {
        return SimpleDateFormat(pattern)
            .format(Calendar.getInstance().time)
    }

    /**
     * @input List<Int> format: [dd, MM, yyyy]
     */
    @SuppressLint("SimpleDateFormat")
    fun isDateBeforeToday(dateIndex: List<Int>): Boolean {
        val dateString = dateIndex.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
        val dateFormat = SimpleDateFormat("dd MM yyyy").parse(dateString)
        val today = SimpleDateFormat("dd MM yyyy").parse(
            SimpleDateFormat("dd MM yyyy").format(
                mCalendar.time
            )
        )
        return dateFormat.before(today)
    }

    /**
     * Get date using custom time and format
     */
    @SuppressLint("SimpleDateFormat")
    fun getDate(pattern: String, time: Date): String {
        return SimpleDateFormat(pattern)
            .format(time)
    }

    /**
     * Get night mode string
     */
    fun getNightMode(): String {
        when (mContext.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
            .getInt(Constant.SHARED_PREFS_NIGHTMODE, -1)
            ) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> return mContext.getString(R.string.auto)
            AppCompatDelegate.MODE_NIGHT_YES -> return mContext.getString(R.string.on)
            AppCompatDelegate.MODE_NIGHT_NO -> return mContext.getString(R.string.off)
        }
        return "NOT SUPPORTED"
    }

    /**
     * Get night mode config
     */
    fun getNightModeConfig(): Int {
        return mContext.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
            .getInt(Constant.SHARED_PREFS_NIGHTMODE, -1)
    }

    /**
     * Write data to shared preferences
     */
    fun writePreference(key: String, value: Int) {
        val prefs = mContext.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }

    fun setAlarm(time: Calendar) {
//        val pendingIntent =
//            PendingIntent.getService(mContext, 10,
//                Intent(mContext, AlarmReciever::class.java),
//                PendingIntent.FLAG_NO_CREATE)

//        val alarmIntent = Intent(mContext, MyAlarmReceiver::class.java).let { intent ->
//            PendingIntent.getBroadcast(mContext, 0, intent, 0)
//        }

        val alarmIntent = PendingIntent.getBroadcast(mContext.applicationContext, 11, Intent(mContext.applicationContext, MyAlarmReceiver::class.java), 0)

        cancelAlarm(alarmIntent)

        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent)
    }

    fun cancelAlarm(intent: PendingIntent) {
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(intent)
    }

}