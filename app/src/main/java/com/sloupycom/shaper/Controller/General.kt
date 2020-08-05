package com.sloupycom.shaper.Controller

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

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