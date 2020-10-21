package com.sloupycom.shaper.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.sloupycom.shaper.R
import com.sloupycom.shaper.dagger.DaggerDependencyComponent
import com.sloupycom.shaper.database.Local
import com.sloupycom.shaper.model.Task
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.collections.HashMap

class AddTaskViewModel(private val activity: Activity): AndroidViewModel(activity.application) {

    /** Values **/
    @RequiresApi(Build.VERSION_CODES.N) private val mCalendar: Calendar = Calendar.getInstance()
    private val mComponent = DaggerDependencyComponent.create()
//    private val mRemote: Remote = mComponent.getRemote()
    private val mUtil = mComponent.getUtil()

    private var mDateIndex: List<Int>? = null
    private var onTaskAddedListener: OnTaskAddedListener? = null
    var textDate: ObservableField<String> = ObservableField("")
    var textTitle: String? = null

    val mLocal = Local.getInstance(activity)

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
                SimpleDateFormat("dd").format(mCalendar.time).toInt(),
                SimpleDateFormat("MM").format(mCalendar.time).toInt(),
                SimpleDateFormat("yyyy").format(mCalendar.time).toInt()
            )
        }
        val nextDue = "${mDateIndex!![2]}${mDateIndex!![1]}${mDateIndex!![0]}"
        val id = mCalendar.timeInMillis.toString()
        val state: String = if (textDate.get() == SimpleDateFormat("EEEE, MMM dd yyyy")
                .format(java.util.Calendar.getInstance().time)) {
            "DUE"
        } else {
            "ONGOING"
        }
        return Task(title = title, creation_date = creationDate, next_due = nextDue, state = state)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addTask(task: Task) {
        viewModelScope.launch {
            mLocal.localDao.insert(task)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onChooseDate() {
        val picker = DatePickerDialog(activity)
        picker.datePicker.minDate = Calendar.getInstance().timeInMillis
        picker.show()
        picker.setOnDateSetListener { view, year, month, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            textDate.set(SimpleDateFormat("EEEE, MMM dd").format(date.time))
            this.mDateIndex = listOf(
                SimpleDateFormat("dd").format(date.time).toInt(),
                SimpleDateFormat("MM").format(date.time).toInt(),
                SimpleDateFormat("yyyy").format(date.time).toInt())
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun onAddTask() {
        if (textTitle != "") {
            textTitle?.let { addTask(buildTask(it)) }
            onTaskAddedListener?.onTaskAdded()
        }
    }

    fun setOnTaskAddedListener(listener: OnTaskAddedListener) {
        this.onTaskAddedListener = listener
    }
    interface OnTaskAddedListener{
        fun onTaskAdded()
    }
}