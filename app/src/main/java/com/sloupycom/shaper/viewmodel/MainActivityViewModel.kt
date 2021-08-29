package com.sloupycom.shaper.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.sloupycom.shaper.data.source.local.Local
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.utils.Util
import kotlinx.coroutines.launch
import java.util.*


class MainActivityViewModel(application: Application) : ViewModel() {

    /** Values **/
    private val mLocal = Local.getInstance(application)
    private val mUtil = Util()
    private val weekIndex = mUtil.getWeekIndex(Calendar.getInstance())
    private val tasks0 = mLocal.localDao.getTodayTasks(weekIndex[0])

    private val tasks1 = mLocal.localDao.getDayTasks(weekIndex[1])
    private val tasks2 = mLocal.localDao.getDayTasks(weekIndex[2])
    private val tasks3 = mLocal.localDao.getDayTasks(weekIndex[3])
    private val tasks4 = mLocal.localDao.getDayTasks(weekIndex[4])
    private val tasks5 = mLocal.localDao.getDayTasks(weekIndex[5])
    private val tasks6 = mLocal.localDao.getDayTasks(weekIndex[6])
    private val liveDataList = listOf(tasks0, tasks1, tasks2, tasks3, tasks4, tasks5, tasks6)
    private val _liveDataMerger: MediatorLiveData<MutableList<Task>> = MediatorLiveData<MutableList<Task>>()
    val liveDataMerger: MediatorLiveData<MutableList<Task>>
        get() = _liveDataMerger

    var textDate: ObservableField<String> = ObservableField(mUtil.getDate("EEEE, MMM dd"))
    var busyDays: LiveData<List<String>>? = mLocal.localDao.getBusyDaysOfWeek(weekIndex[6])
    private lateinit var lastLiveData: LiveData<*>

    init {
        _liveDataMerger.addSource(tasks0!!) {
                value -> _liveDataMerger.value = value
            lastLiveData = tasks0
        }
    }


    /**
     * Called when state of a task changes
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun onTaskStateChanged(task: Task) {
        if (task.state == "ONGOING") {
            task.state = "DONE"
            if (task.repetition != null) {
                val repetitionTask = Task(
                    title = task.title,
                    next_due = mUtil.addDayToDate(task.next_due, task.repetition!!),
                    creation_date = mUtil.getTodayDateIndex(),
                    state = "ONGOING",
                    repetition = task.repetition
                )
                addTask(repetitionTask)
            }
        }
        else task.state = "ONGOING"
        viewModelScope.launch {
            mLocal.localDao.update(task)
        }
    }

    /**
     * DayBar onDayChanged Callback
     */
    fun dayChanged(index: Int) {
        _liveDataMerger.removeSource(lastLiveData)
        _liveDataMerger.addSource(liveDataList[index]!!) {value -> _liveDataMerger.value = value
            lastLiveData = liveDataList[index]!!
        }

    }

    fun deleteTaskItem(task: Task) {
        viewModelScope.launch {
            mLocal.localDao.delete(task)
        }
    }

    fun undoDeleteTaskItem(task: Task) {
        viewModelScope.launch {
            mLocal.localDao.insert(task)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addTask(task: Task) {
        viewModelScope.launch {
            mLocal.localDao.insert(task)
        }
    }
}