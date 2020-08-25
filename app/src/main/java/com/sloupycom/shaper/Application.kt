package com.sloupycom.shaper

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject
import javax.inject.Singleton


object Application {

    private var INSTANCE: App? = null

    fun getInstance(): App{
        if (INSTANCE == null) {
            INSTANCE = App()
        }
        return INSTANCE!!
    }

}

class App: Application() {

    private var prefs: SharedPreferences? = null
    var nightMode: Int? = null

    init {
        onCreate()
    }
    override fun onCreate() {
        super.onCreate()

        prefs = getSharedPreferences("application", Context.MODE_PRIVATE)
        nightMode = prefs?.getInt("night_mode", -1)
        if (nightMode == null) nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    fun getNightMode(): String? {
        when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> return getString(R.string.auto)
            AppCompatDelegate.MODE_NIGHT_YES -> return getString(R.string.on)
            AppCompatDelegate.MODE_NIGHT_NO -> return getString(R.string.off)
            else -> return null
        }
    }
}