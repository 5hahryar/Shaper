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
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.BottomsheetSettingsBinding
import com.sloupycom.shaper.utils.Constant
import com.sloupycom.shaper.utils.ReminderBroadCast
import com.sloupycom.shaper.utils.Util
import com.sloupycom.shaper.viewmodel.SettingsViewModel
import java.util.*

class SettingsBottomSheet : BottomSheetDialogFragment(), PopupMenu.OnMenuItemClickListener {

    /**Values**/
    private val mUtil = Util()
    private lateinit var mBinding: BottomsheetSettingsBinding
    private lateinit var viewModel: SettingsViewModel



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_settings, container, false)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        viewModel.nightMode.set(mUtil.getNightMode(requireContext()))
        viewModel.reminder.set(mUtil.readStringPreferences(requireContext(), Constant.SHARED_PREFS_REMINDER))
        mBinding.viewModel = viewModel

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.supportButton.setOnClickListener{
            SupportDialog().show(requireActivity().supportFragmentManager, "fragment_support")
        }

        mBinding.textViewReminder.setOnClickListener {
            val text = viewModel.reminder.get()!!
            if (text != "Off" && text != "On") openTimePicker(text.substringBefore(":").toInt(),
                text.substringAfter(":").toInt())
            else openTimePicker(0, 0)
        }

        mBinding.textViewNightMode.setOnClickListener { onSetNightModeClick(it) }
    }

    private fun setNightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        mUtil.writePreference(requireContext(), Constant.SHARED_PREFS_NIGHTMODE, mode)

    }

    /**
     * Open TimePickerDialog and listen for it's events
     */
    private fun openTimePicker(hour: Int, minute: Int) {
        val timePicker = TimePickerDialog(activity, { _: TimePicker, i: Int, i1: Int ->
            //Time is set
            var h = i.toString()
            var m = i1.toString()
            if (i < 10) h = "0$h"
            if (i1 < 10) m = "0$m"
            viewModel.reminder.set("$h:$m")
            setReminder(i, i1)
            mUtil.writePreference(requireContext(), Constant.SHARED_PREFS_REMINDER, "$h:$m")
        }, hour, minute, false)
        timePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Turn Off") { _: DialogInterface, _: Int ->
            //Negative button pressed
            viewModel.reminder.set(requireContext().getString(R.string.off))
            cancelReminder()
            mUtil.writePreference(requireContext(), Constant.SHARED_PREFS_REMINDER, requireContext().getString(R.string.off))
        }
        timePicker.show()
    }

    /**
     * Cancel alarm for task reminder
     */
    private fun cancelReminder() {
        val intent = Intent(activity, ReminderBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, Constant.RC_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = requireContext().getSystemService(Service.ALARM_SERVICE) as AlarmManager
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

        val alarmManager = requireContext().getSystemService(Service.ALARM_SERVICE) as AlarmManager
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
                viewModel.nightMode.set(requireContext().getString(R.string.auto))
            }
            R.id.night_on -> {
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                viewModel.nightMode.set(requireContext().getString(R.string.on))
            }
            R.id.night_off -> {
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                viewModel.nightMode.set(requireContext().getString(R.string.off))
            }
            else -> return false
        }
        return true
    }

}