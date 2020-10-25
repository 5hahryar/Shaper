package com.sloupycom.shaper.viewmodel

import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import com.sloupycom.shaper.R
import com.sloupycom.shaper.utils.Constant
import com.sloupycom.shaper.utils.ReminderBroadCast
import com.sloupycom.shaper.utils.Util
import com.sloupycom.shaper.view.SupportDialog
import java.util.*


class SettingsViewModel(private val activity: FragmentActivity): AndroidViewModel(activity.application),
    PopupMenu.OnMenuItemClickListener {

    /** Values **/
    private val mUtil = Util()

    var nightMode: ObservableField<String> = ObservableField(mUtil.getNightMode(getApplication()))
    var reminder: ObservableField<String> = ObservableField(mUtil.readStringPreferences(getApplication(), Constant.SHARED_PREFS_REMINDER))

    private fun setNightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        mUtil.writePreference(getApplication(), Constant.SHARED_PREFS_NIGHTMODE, mode)

    }

    fun onSupportClick() {
        SupportDialog().show(activity.supportFragmentManager, "fragment_support")
    }

    fun onReminderClick() {
        val text = reminder.get()!!
        if (text != "Off" && text != "On") openTimePicker(text.substringBefore(":").toInt(),
            text.substringAfter(":").toInt())
        else openTimePicker(0, 0)
    }

    /**
     * Open TimePickerDialog and listen for it's events
     */
    private fun openTimePicker(hour: Int, minute: Int) {
        val timePicker = TimePickerDialog(activity, { _: TimePicker, i: Int, i1: Int ->
            //Time is set
            reminder.set("$i:$i1")
            setReminder(i, i1)
            mUtil.writePreference(activity, Constant.SHARED_PREFS_REMINDER, "$i:$i1")
        }, hour, minute, false)
        timePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Turn Off") { _: DialogInterface, _: Int ->
            //Negative button pressed
            reminder.set(activity.getString(R.string.off))
            cancelReminder()
            mUtil.writePreference(activity, Constant.SHARED_PREFS_REMINDER, activity.getString(R.string.off))
        }
        timePicker.show()
    }

    /**
     * Cancel alarm for task reminder
     */
    private fun cancelReminder() {
        val intent = Intent(activity, ReminderBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, Constant.RC_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = activity.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Set alarm for task reminder
     */
    private fun setReminder(hour: Int, minute: Int) {
        // Set the alarm to start
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val intent = Intent(activity, ReminderBroadCast::class.java)
        val pendingIntent = PendingIntent
            .getBroadcast(activity, Constant.RC_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = activity.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.d(Constant.TAG, "ALARM: Reminder set")
    }

    fun onSetNightModeClick(view: View) {
        val popup = PopupMenu(activity, view)
        popup.menuInflater.inflate(R.menu.menu_night_mode, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener(this)
    }

    /**
     * On night mode menu item clicked
     */
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.night_auto -> {
                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                nightMode.set(activity.getString(R.string.auto))
            }
            R.id.night_on -> {
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                nightMode.set(activity.getString(R.string.on))
            }
            R.id.night_off -> {
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                nightMode.set(activity.getString(R.string.off))
            }
            else -> return false
        }
        return true
    }
}