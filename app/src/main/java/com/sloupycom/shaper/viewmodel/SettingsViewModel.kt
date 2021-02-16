package com.sloupycom.shaper.viewmodel

import android.app.*
import android.content.Context
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
import androidx.lifecycle.ViewModel
import com.sloupycom.shaper.R
import com.sloupycom.shaper.utils.Constant
import com.sloupycom.shaper.utils.ReminderBroadCast
import com.sloupycom.shaper.utils.Util
import com.sloupycom.shaper.view.SupportDialog
import java.util.*


class SettingsViewModel: ViewModel() {

    /** Values **/
    var nightMode: ObservableField<String> = ObservableField()
    var reminder: ObservableField<String> = ObservableField()

}