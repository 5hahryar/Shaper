package com.sloupycom.shaper.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.sloupycom.shaper.core.util.Event
import com.sloupycom.shaper.data.repository.TaskRepository
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.core.util.Util
import kotlinx.coroutines.launch
import java.util.*

class MainActivityViewModel(private val tasksRepository: TaskRepository) : ViewModel() {

    /** Values **/
    private val weekIndex = Util.getWeekIndex(Calendar.getInstance())

    private val tasks0 = tasksRepository.getTasks(weekIndex[0])
    private val tasks1 = tasksRepository.getTasks(weekIndex[1])
    private val tasks2 = tasksRepository.getTasks(weekIndex[2])
    private val tasks3 = tasksRepository.getTasks(weekIndex[3])
    private val tasks4 = tasksRepository.getTasks(weekIndex[4])
    private val tasks5 = tasksRepository.getTasks(weekIndex[5])
    private val tasks6 = tasksRepository.getTasks(weekIndex[6])
    private val liveDataList = listOf(tasks0, tasks1, tasks2, tasks3, tasks4, tasks5, tasks6)
    private val _liveDataMerger: MediatorLiveData<MutableList<Task>> = MediatorLiveData<MutableList<Task>>()
    val liveDataMerger: MediatorLiveData<MutableList<Task>>
        get() = _liveDataMerger

    var textDate: ObservableField<String> = ObservableField(Util.getDate("EEEE, MMM dd"))
    var busyDays: LiveData<List<String>>? = tasksRepository.getBusyDays(weekIndex[6])
    private lateinit var lastLiveData: LiveData<*>

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    private val _openSettingsEvent = MutableLiveData<Event<Unit>>()
    val openSettingsEvent: LiveData<Event<Unit>> = _openSettingsEvent

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
                    next_due = Util.addDayToDate(task.next_due, task.repetition!!),
                    creation_date = Util.getTodayDateIndex(),
                    state = "ONGOING",
                    repetition = task.repetition
                )
                addTask(repetitionTask)
            }
        }
        else task.state = "ONGOING"
        viewModelScope.launch {
            tasksRepository.updateTask(task)
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
            tasksRepository.deleteTask(task)
        }
    }

    fun undoDeleteTaskItem(task: Task) {
        viewModelScope.launch {
            tasksRepository.addTask(task)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            tasksRepository.addTask(task)
        }
    }

    fun addNewTask() {
        _newTaskEvent.value = Event(Unit)
    }

    fun openSettings() {
        _openSettingsEvent.value = Event(Unit)
    }
}