package com.sloupycom.shaper.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sloupycom.shaper.R
import com.sloupycom.shaper.core.util.Event


class SettingsViewModel: ViewModel() {

    /** Values **/
    var nightMode: ObservableField<String> = ObservableField()
    var reminder: ObservableField<String> = ObservableField()

    private val _supportButtonClickedEvent = MutableLiveData<Event<Unit>>()
    val supportButtonClickedEvent: LiveData<Event<Unit>> = _supportButtonClickedEvent

    private val _reminderButtonClickedEvent = MutableLiveData<Event<Unit>>()
    val reminderButtonClickedEvent: LiveData<Event<Unit>> = _reminderButtonClickedEvent

    private val _nightModeButtonClickedEvent = MutableLiveData<Event<Unit>>()
    val nightModeButtonClickedEvent: LiveData<Event<Unit>> = _nightModeButtonClickedEvent

    fun onClick(view: View) {
        when (view.id) {
            R.id.supportButton -> {
                _supportButtonClickedEvent.value = Event(Unit)
            }
            R.id.textView_reminder -> {
                _reminderButtonClickedEvent.value = Event(Unit)
            }
            R.id.textView_nightMode -> {
                _nightModeButtonClickedEvent.value = Event(Unit)
            }
        }
    }
}