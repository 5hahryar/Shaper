package com.sloupycom.shaper.viewmodel

import android.app.TimePickerDialog
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.utils.Util
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class SettingsViewModel(application: android.app.Application): AndroidViewModel(application),
    TimePickerDialog.OnTimeSetListener {

    private val mRepo: Repo = Repo(application)
    private val mUser: FirebaseUser? = mRepo.getUserCredentials()
    private val mGeneral = Util(application)
    var imageUri: String? = mUser?.photoUrl.toString()
    var name: String? = mUser?.displayName
    var email: String? = mUser?.email
    var nightMode: ObservableField<String> = ObservableField(mGeneral.getNightMode())

    init {
        name = mRepo.getUserCredentials()?.displayName
    }

    fun setNightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        mGeneral.writePreference("night_mode", mode)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        mGeneral.setAlarm(calendar)
    }
}

@BindingAdapter("loadImage")
fun loadImage(imageView: CircleImageView, imageUrl: String?) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_account_circle_24px)
        .into(imageView)
}