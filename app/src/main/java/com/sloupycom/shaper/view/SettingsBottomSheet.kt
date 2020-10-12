package com.sloupycom.shaper.view

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.BottomsheetSettingsBinding
import com.sloupycom.shaper.utils.AuthHelper
import com.sloupycom.shaper.utils.Constant
import com.sloupycom.shaper.utils.ReminderBroadCast
import com.sloupycom.shaper.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.bottomsheet_settings.*
import java.util.*

class SettingsBottomSheet : BottomSheetDialogFragment(), PopupMenu.OnMenuItemClickListener {

    /**Values**/
    private var mBinding: BottomsheetSettingsBinding? = null

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
        createNotificationChannel()
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
            setReminder()
//            SupportDialog().show(activity!!.supportFragmentManager, "fragment_support")
        }
    }

    private fun setReminder() {
        // Set the alarm to start at approximately 10:00 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
        }
        val intent = Intent(activity, ReminderBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, 100, intent, 0)
        val alarmManager = activity?.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_HOUR,
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name: CharSequence = "Reminder"
        val description = "Daily reminder of tasks"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Constant.ID_NOTIFICATION_CHANNEL, name, importance)
        channel.description = description
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = context?.getSystemService(
            NotificationManager::class.java
        )
        notificationManager?.createNotificationChannel(channel)
    }

}