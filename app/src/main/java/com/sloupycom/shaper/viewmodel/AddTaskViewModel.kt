package com.sloupycom.shaper.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.sloupycom.shaper.R
import com.sloupycom.shaper.database.Local
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.utils.Util
import kotlinx.coroutines.launch
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import java.text.SimpleDateFormat

class AddTaskViewModel(private val activity: Activity): AndroidViewModel(activity.application) {

    /** Values **/
    @RequiresApi(Build.VERSION_CODES.N)
    private val mCalendar: Calendar = Calendar.getInstance()
    private val mUtil = Util()
    private val mLocal = Local.getInstance(activity)
    val textDate: ObservableField<String> = ObservableField("")
    val textError: ObservableField<String> = ObservableField()
    private var mDateIndex: List<String>? = null

    private var onTaskAddedListener: OnTaskAddedListener? = null
    private var isRepetitionSelected: Boolean = false
    private var textRepetition: String = ""
    var textTitle: String? = null

    init {
        textDate.set(activity.getString(R.string.today))
    }

    /**
     * Build a Task object
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    private fun buildTask(title: String): Task {
        val creationDate = mUtil.getDate("EEE MMM dd yyyy", mCalendar.time)
        if (mDateIndex == null) {
            mDateIndex = listOf(
                SimpleDateFormat("dd").format(mCalendar.time),
                SimpleDateFormat("MM").format(mCalendar.time),
                SimpleDateFormat("yyyy").format(mCalendar.time)
            )
        }
        val nextDue = "${mDateIndex!![2]}${mDateIndex!![1]}${mDateIndex!![0]}"
        val state: String = if (textDate.get() == SimpleDateFormat("EEEE, MMM dd yyyy")
                .format(java.util.Calendar.getInstance().time)) {
            "DUE"
        } else {
            "ONGOING"
        }
        var repetition: Int? = null
        if (isRepetitionSelected) {
            repetition = if (textRepetition.isNotEmpty()) textRepetition.toInt() else 1
        }

        return Task(title = title, creation_date = creationDate, next_due = nextDue, state = state, repetition = repetition)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addTask(task: Task) {
        viewModelScope.launch {
            mLocal.localDao.insert(task)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun onChooseDate() {
        val picker = DatePickerDialog(activity)
        picker.datePicker.minDate = Calendar.getInstance().timeInMillis
        picker.show()
        picker.setOnDateSetListener { _, year, month, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            textDate.set(SimpleDateFormat("EEEE, MMM dd").format(date.time))
            this.mDateIndex = listOf(
                SimpleDateFormat("dd").format(date.time),
                SimpleDateFormat("MM").format(date.time),
                SimpleDateFormat("yyyy").format(date.time))
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun onAddTask() {
        if (textTitle != null && textTitle != "") {
            textTitle?.let { addTask(buildTask(it)) }
            onTaskAddedListener?.onTaskAdded()
        } else {
            textError.set(activity.getString(R.string.empty_title_error))
            textError.notifyChange()
        }
    }

    fun onRepetitionClick(view: View) {
        isRepetitionSelected = view.isSelected
    }

    fun afterTextRepetitionChanged(editable: Editable) {
        textRepetition = editable.toString()
    }

    fun setOnTaskAddedListener(listener: OnTaskAddedListener) {
        this.onTaskAddedListener = listener
    }
    interface OnTaskAddedListener{
        fun onTaskAdded()
    }
}