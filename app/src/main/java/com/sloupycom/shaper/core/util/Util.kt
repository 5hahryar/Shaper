package com.sloupycom.shaper.core.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.sloupycom.shaper.R
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

object Util{

    //TODO: Move unrelated functions from this object

    /**Values**/
    private val mCalendar: Calendar = Calendar.getInstance()

    /**
     * @Output Get today's date in a string, pattern: "yyyyMMdd"
     */
    @SuppressLint("SimpleDateFormat")
    fun getTodayDateIndex(): String {
        val dd = SimpleDateFormat("dd").format(mCalendar.time)
        val mm = SimpleDateFormat("MM").format(mCalendar.time)
        val yyyy = SimpleDateFormat("yyyy").format(mCalendar.time)
        return "$yyyy$mm$dd"
    }

    /**
     * @Input String in a SimpleDateFormat pattern, example: "yyyyMMdd"
     * @Output Date in a string, with the pattern which is provided at input
     */
    @SuppressLint("SimpleDateFormat")
    fun getDate(pattern: String): String {
        return SimpleDateFormat(pattern)
            .format(Calendar.getInstance().time)
    }

    /**
     * @Input Date with a string format, example: "yyyyMMdd"
     * @Output Boolean, true if input date is before today, else false
     */
    @SuppressLint("SimpleDateFormat")
    fun isDateBeforeToday(dateIndex: String): Boolean {
        val todayIndex = SimpleDateFormat("yyyyMMdd").format(mCalendar.time)
        return dateIndex.toInt() < todayIndex.toInt()
    }

    /**
     * @Input String in a SimpleDateFormat pattern, example: "yyyyMMdd"
     * & Custom time as a Date object
     * @Output String containing the date of the time which is provided at input, following the given pattern
     * Get date using custom time and format
     */
    @SuppressLint("SimpleDateFormat")
    fun getDate(pattern: String, time: Date): String {
        return SimpleDateFormat(pattern)
            .format(time)
    }

    /**
     * @Output String containing nightMode status, example: "Auto" | "On" | "Off" | "NOT SUPPORTED"
     */
    fun getNightMode(context: Context): String {
        when (context.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
            .getInt(Constant.SHARED_PREFS_NIGHTMODE, -1)
            ) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> return context.getString(R.string.auto)
            AppCompatDelegate.MODE_NIGHT_YES -> return context.getString(R.string.on)
            AppCompatDelegate.MODE_NIGHT_NO -> return context.getString(R.string.off)
        }
        return "NOT SUPPORTED"
    }

    /**
     * Write data to shared preferences
     */
    fun writePreference(context: Context, key: String, value: Int) {
        val prefs = context.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }

    /**
     * Write data to shared preferences
     */
    fun writePreference(context: Context, key: String, value: String) {
        val prefs = context.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(key, value).apply()
    }

    /**
     * Get string preference
     */
    fun readStringPreferences(context: Context, key: String): String? {
        return context.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
            .getString(key, context.getString(R.string.off))
    }

    /**
     * Get boolean preference
     */
    fun readBooleanPreferences(context: Context, key: String): Boolean {
        return context.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
            .getBoolean(key, false)
    }

    /**
     * @Output String like the following pattern: "yyyyMMdd"
     */
    @SuppressLint("SimpleDateFormat")
    fun getDateIndex(date: Calendar): String {

        val year = SimpleDateFormat("yyyy").format(date.time)
        val month = SimpleDateFormat("MM").format(date.time)
        val day = SimpleDateFormat("dd").format(date.time)

        return "$year$month$day"
    }

    /**
     * @Output DateIndex of the following 7 days starting from the provided instance time
     * pattern: "yyyyMMdd"
     */
    fun getWeekIndex(instance: Calendar): List<String> {
        val weekIndex = mutableListOf<String>()
        for (i in 0..6) {
            weekIndex.add(getDateIndex(instance))
            instance.add(Calendar.DAY_OF_MONTH, 1)
        }
        return weekIndex
    }

    /**
     * @Output Get DAY_OF_MONTH of all the weekDays that contain tasks
     */
    fun getBusyWeekDaysFromDateIndex(list: List<String>?): List<Int> {
        val days = mutableListOf<Int>()
        val todayIndex = getTodayDateIndex()
        if (list != null && list.isNotEmpty()) {
            days.add(todayIndex.substring(6).toInt())
            for (date in list) {
                if (date.substring(0, 6) == todayIndex.substring(0, 6))
                    days.add(date.substring(6).toInt())
            }
        }
        return days
    }

    fun addDayToDate(date: String, day: Int): String {
        val dateCal = Calendar.getInstance()
        dateCal.set(
            date.substring(0..3).toInt(),
            date.substring(4..5).toInt()-1,
            date.substring(6).toInt()
        )
        dateCal.add(Calendar.DAY_OF_MONTH, day)

        return getDate("yyyyMMdd", dateCal.time)
    }

}