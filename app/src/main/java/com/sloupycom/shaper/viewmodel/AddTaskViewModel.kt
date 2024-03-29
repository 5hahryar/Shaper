package com.sloupycom.shaper.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.text.Editable
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.sloupycom.shaper.R
import com.sloupycom.shaper.core.util.Event
import com.sloupycom.shaper.data.repository.TaskRepository
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.core.util.Util
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class AddTaskViewModel(context: Context, private val taskRepository: TaskRepository): ViewModel() {

    /** Values **/
    @RequiresApi(Build.VERSION_CODES.N)
    // TODO: Cannot write unit tests when getting an instance of calendar here
    private val mCalendar: Calendar = Calendar.getInstance()
    val textDate: ObservableField<String> = ObservableField(context.getString(R.string.today))
    val textError: ObservableField<String> = ObservableField()

    private var mDateIndex: List<String>? = null
    private var isRepetitionSelected: Boolean = false
    private var textRepetition: String = ""
    var textTitle: String? = null

    private val _newTaskAddedEvent = MutableLiveData<Event<Unit>>()
    val newTaskAddedEvent: LiveData<Event<Unit>> = _newTaskAddedEvent

    private val _openDatePickerEvent = MutableLiveData<Event<Unit>>()
    val openDatePickerEvent: LiveData<Event<Unit>> = _openDatePickerEvent

    /**
     * Build a Task object
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    private fun buildTask(title: String): Task {
        val creationDate = Util.getDate("yyyyMMdd", mCalendar.time)
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
    private fun addTask(task: Task) {
        viewModelScope.launch {
            taskRepository.addTask(task)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        textDate.set(SimpleDateFormat("EEEE, MMM dd").format(date.time))
        this.mDateIndex = listOf(
            SimpleDateFormat("dd").format(date.time),
            SimpleDateFormat("MM").format(date.time),
            SimpleDateFormat("yyyy").format(date.time)
        )
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun onAddTask() {
        if (textTitle != null && textTitle != "") {
            textTitle?.let { addTask(buildTask(it)) }
            _newTaskAddedEvent.value = Event(Unit)
        } else {
            textError.set(R.string.empty_title_error.toString())
            textError.notifyChange()
        }
    }

    fun onRepetitionClick(view: View) {
        isRepetitionSelected = view.isSelected
    }

    fun afterTextRepetitionChanged(editable: Editable) {
        textRepetition = editable.toString()
    }

    //TODO: Having separate functions for each onClick is probably better for testing
    fun onClick(view: View) {
        when (view.id) {
            R.id.textView_date -> {
                _openDatePickerEvent.value = Event(Unit)
            }
        }
    }
}