package com.sloupycom.shaper.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.dagger.DaggerDependencyComponent
import com.sloupycom.shaper.database.Local
import com.sloupycom.shaper.model.Task
import kotlinx.coroutines.launch
import java.util.*


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    /** Values **/
    private val mComponent = DaggerDependencyComponent.create()
    private val mUtil = mComponent.getUtil()
    private val mLocal = Local.getInstance(application)
    private val mCalendar = Calendar.getInstance()
    private val weekIndex = mUtil.getWeekIndex(Calendar.getInstance())

    private val tasks0 = mLocal.localDao.getTodayTasks(weekIndex[0])
    private val tasks1 = mLocal.localDao.getDayTasks(weekIndex[1])
    private val tasks2 = mLocal.localDao.getDayTasks(weekIndex[2])
    private val tasks3 = mLocal.localDao.getDayTasks(weekIndex[3])
    private val tasks4 = mLocal.localDao.getDayTasks(weekIndex[4])
    private val tasks5 = mLocal.localDao.getDayTasks(weekIndex[5])
    private val tasks6 = mLocal.localDao.getDayTasks(weekIndex[6])
    private val liveDataList = listOf(tasks0, tasks1, tasks2, tasks3, tasks4, tasks5, tasks6)
    val liveDataMerger: MediatorLiveData<MutableList<Task>> = MediatorLiveData<MutableList<Task>>()

    var textDate: ObservableField<String> = ObservableField(mUtil.getDate("EEEE, MMM dd"))
    var busyDays: LiveData<List<String>>? = mLocal.localDao.getBusyDaysOfWeek()
    private lateinit var lastLiveData: LiveData<*>

    init {
        liveDataMerger.addSource(tasks0!!) {
                value -> liveDataMerger.value = value
            lastLiveData = tasks0
        }
    }


    /**
     * Called when state of a task changes
     */
    fun onTaskStateChanged(task: Task) {
        if (task.state == "ONGOING") task.state = "DONE"
        else task.state = "ONGOING"
        viewModelScope.launch {
            mLocal.localDao.update(task)
        }
    }

    /**
     * DayBar onDayChanged Callback
     */
    fun dayChanged(index: Int, date: HashMap<String, String>, chip: DayBarChip) {
        liveDataMerger.removeSource(lastLiveData)
        liveDataMerger.addSource(liveDataList[index]!!) {value -> liveDataMerger.value = value
            lastLiveData = liveDataList[index]!!
        }

    }
}