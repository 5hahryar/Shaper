package com.sloupycom.shaper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.sloupycom.shaper.model.Task
import java.text.SimpleDateFormat
import java.util.*

class General: android.app.Application(){

    private val mCalendar: Calendar = Calendar.getInstance()

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

    fun writePreference(key: String, value: Int) {
        val prefs = getSharedPreferences("application", Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }
}