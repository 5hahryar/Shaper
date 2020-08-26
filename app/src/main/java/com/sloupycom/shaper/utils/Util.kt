package com.sloupycom.shaper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.Task
import java.text.SimpleDateFormat
import java.util.*

class Util(application: android.app.Application): AndroidViewModel(application) {

    private val mCalendar: Calendar = Calendar.getInstance()
    private val context = getApplication<android.app.Application>().applicationContext

    @SuppressLint("SimpleDateFormat")
    fun getTodayDateIndex(): List<Int> {
        return listOf (
            SimpleDateFormat("dd").format(mCalendar.time).toInt(),
            SimpleDateFormat("MM").format(mCalendar.time).toInt(),
            SimpleDateFormat("yyyy").format(mCalendar.time).toInt()
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun getTodayDate(): Date {
        return SimpleDateFormat("dd MM yyyy").parse(mCalendar.time.toString())!!
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(pattern: String): String {
        return SimpleDateFormat(pattern)
            .format(Calendar.getInstance().time)
    }

    @SuppressLint("SimpleDateFormat")
    fun isDateBeforeToday(dateIndex: List<Int>): Boolean {
        val dateString = dateIndex.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
        val dateFormat = SimpleDateFormat("dd MM yyyy").parse(dateString)
        val today = SimpleDateFormat("dd MM yyyy").parse(SimpleDateFormat("dd MM yyyy").format(mCalendar.time))
        return dateFormat.before(today)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(pattern: String, time: Date): String {
        return SimpleDateFormat(pattern)
            .format(time)
    }

    fun getNightMode(): String {
        when (context.getSharedPreferences("application", Context.MODE_PRIVATE)
            .getInt("night_mode", -1)
            ) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> return context.getString(R.string.auto)
            AppCompatDelegate.MODE_NIGHT_YES -> return context.getString(R.string.on)
            AppCompatDelegate.MODE_NIGHT_NO -> return context.getString(R.string.off)
        }
        return "NOT SUPPORTED"
    }

    fun writePreference(key: String, value: Int) {
        val prefs = context.getSharedPreferences("application", Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }
}