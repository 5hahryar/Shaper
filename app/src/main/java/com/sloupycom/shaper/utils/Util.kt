package com.sloupycom.shaper.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.sloupycom.shaper.R
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Util @Inject constructor(){

    /**Values**/
    private val mCalendar: Calendar = Calendar.getInstance()

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
     * Get night mode config
     */
    fun getNightModeConfig(context: Context): Int {
        return context.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
            .getInt(Constant.SHARED_PREFS_NIGHTMODE, -1)
    }

    /**
     * Write data to shared preferences
     */
    fun writePreference(context: Context, key: String, value: Int) {
        val prefs = context.getSharedPreferences(Constant.SHARED_PREFS, Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }

}