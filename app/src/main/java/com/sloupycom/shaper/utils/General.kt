package com.sloupycom.shaper.utils

import java.text.SimpleDateFormat
import java.util.*

class General {
    fun getDate(pattern: String): String {
        return SimpleDateFormat(pattern)
            .format(Calendar.getInstance().time)
    }

    fun getDate(pattern: String, time: Date): String {
        return SimpleDateFormat(pattern)
            .format(time)
    }
}