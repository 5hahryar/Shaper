package com.sloupycom.shaper.view

import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.R
import com.sloupycom.shaper.dagger.DaggerDependencyComponent
import com.sloupycom.shaper.databinding.BottomsheetSettingsBinding
import com.sloupycom.shaper.utils.AuthHelper
import com.sloupycom.shaper.utils.Constant
import com.sloupycom.shaper.utils.ReminderBroadCast
import com.sloupycom.shaper.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.bottomsheet_settings.*
import java.sql.Time
import java.util.*

class SettingsBottomSheet : BottomSheetDialogFragment(), PopupMenu.OnMenuItemClickListener {

    /**Values**/
    private var mBinding: BottomsheetSettingsBinding? = null
    private val mUtil = DaggerDependencyComponent.create().getUtil()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.bottomsheet_settings, container, false)
        mBinding?.viewModel = SettingsViewModel(activity!!.application)

        return mBinding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        textView_nightMode.setOnClickListener {
            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_night_mode, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener(this)
        }
        logoutButton.setOnClickListener {
            AuthHelper(activity!!.application).signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }
        supportButton.setOnClickListener {
            SupportDialog().show(activity!!.supportFragmentManager, "fragment_support")
        }
        textView_reminder.setOnClickListener {
            val text = textView_reminder.text.toString()
            if (text != "Off" && text != "On") openTimePicker(text.substringBefore(":").toInt(),
                text.substringAfter(":").toInt())
            else openTimePicker(0, 0)
        }
    }

    /**
     * Open TimePickerDialog and listen for it's events
     */
    private fun openTimePicker(hour: Int, minute: Int) {
        val timePicker = TimePickerDialog(context, { timePicker: TimePicker, i: Int, i1: Int ->
            //Time is set
            textView_reminder.text = "$i:$i1"
            setReminder(i, i1)
            mUtil.writePreference(context!!, Constant.SHARED_PREFS_REMINDER, "$i:$i1")
        }, hour, minute, false)
        timePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Turn Off") { dialogInterface: DialogInterface, i: Int ->
            //Negative button pressed
            textView_reminder.text = getString(R.string.off)
            cancelReminder()
            mUtil.writePreference(context!!, Constant.SHARED_PREFS_REMINDER, getString(R.string.off))
        }
        timePicker.show()
    }

    private fun cancelReminder() {
        val intent = Intent(activity, ReminderBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, 100, intent, 0)
        val alarmManager = activity?.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun setReminder(hour: Int, minute: Int) {
        // Set the alarm to start at approximately 10:00 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val intent = Intent(activity, ReminderBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, Constant.RC_REMINDER, intent, 0)
        val alarmManager = activity?.getSystemService(Service.ALARM_SERVICE) as AlarmManager
//        alarmManager.cancel(pendingIntent)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.night_auto -> {
                mBinding?.viewModel?.setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            R.id.night_on -> {
                mBinding?.viewModel?.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            R.id.night_off -> {
                mBinding?.viewModel?.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> return false
        }
        return true
    }

}