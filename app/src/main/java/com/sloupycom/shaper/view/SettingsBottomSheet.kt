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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.BottomsheetSettingsBinding
import com.sloupycom.shaper.core.util.Constant
import com.sloupycom.shaper.core.util.ReminderBroadCast
import com.sloupycom.shaper.core.util.Util
import com.sloupycom.shaper.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.bottomsheet_settings.*
import org.koin.android.ext.android.inject
import java.util.*

class SettingsBottomSheet : BottomSheetDialogFragment(), PopupMenu.OnMenuItemClickListener {

    /**Values**/
    private lateinit var mBinding: BottomsheetSettingsBinding
    private val mViewModel: SettingsViewModel by inject()

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
        mViewModel.nightMode.set(Util.getNightMode(requireContext()))
        mViewModel.reminder.set(Util.readStringPreferences(requireContext(), Constant.SHARED_PREFS_REMINDER))
        mBinding.viewModel = mViewModel

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

    }

    private fun setupListeners() {
        mViewModel.supportButtonClickedEvent.observe(this, {
            SupportDialog().show(requireActivity().supportFragmentManager, "fragment_support")
        })

        mViewModel.reminderButtonClickedEvent.observe(this, {
            val text = mViewModel.reminder.get()!!
            if (text != getString(R.string.off) && text != getString(R.string.on)) openTimePicker(text.substringBefore(":").toInt(),
                text.substringAfter(":").toInt())
            else openTimePicker(0, 0)
        })

        mViewModel.nightModeButtonClickedEvent.observe(this, {
            onSetNightModeClick(textView_nightMode)
        })
    }

    private fun setNightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        Util.writePreference(requireContext(), Constant.SHARED_PREFS_NIGHTMODE, mode)

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
            mViewModel.reminder.set("$h:$m")
            setReminder(i, i1)
            Util.writePreference(requireContext(), Constant.SHARED_PREFS_REMINDER, "$h:$m")
        }, hour, minute, false)
        timePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Turn Off") { _: DialogInterface, _: Int ->
            //Negative button pressed
            mViewModel.reminder.set(requireContext().getString(R.string.off))
            cancelReminder()
            Util.writePreference(requireContext(), Constant.SHARED_PREFS_REMINDER, requireContext().getString(R.string.off))
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

    private fun onSetNightModeClick(view: View) {
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
                mViewModel.nightMode.set(requireContext().getString(R.string.auto))
            }
            R.id.night_on -> {
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mViewModel.nightMode.set(requireContext().getString(R.string.on))
            }
            R.id.night_off -> {
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mViewModel.nightMode.set(requireContext().getString(R.string.off))
            }
            else -> return false
        }
        return true
    }

}