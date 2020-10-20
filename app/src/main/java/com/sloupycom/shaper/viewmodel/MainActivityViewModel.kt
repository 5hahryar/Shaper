package com.sloupycom.shaper.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.dagger.DaggerDependencyComponent
import com.sloupycom.shaper.database.Local
import com.sloupycom.shaper.model.Task
import kotlinx.coroutines.launch


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    /** Values **/
    private val mComponent = DaggerDependencyComponent.create()
    private val mUtil = mComponent.getUtil()
    private val mLocal = Local.getInstance(application)
    val tasks = mLocal.localDao.getAllTasks()

    var textDate: ObservableField<String> = ObservableField(mUtil.getDate("EEEE, MMM dd"))
    var isEmpty: ObservableBoolean = ObservableBoolean(true)
    var isLoading: ObservableBoolean = ObservableBoolean(true)
    var busyDays: ObservableField<List<Int>> = ObservableField(mutableListOf())


    /**
     * Called when state of a task changes
     */
    fun onTaskStateChanged(task: Task) {
        if (task.state != "DONE") task.state = "DONE"
        else task.state = "ONGOING"
        viewModelScope.launch {
            mLocal.localDao.update(task)
        }
    }

    /**
     * DayBar onDayChanged Callback
     */
    fun dayChanged(date: java.util.HashMap<String, String>, chip: DayBarChip) {
//        isLoading.set(true)
//        if (date[DayBarChip.DAY] == mUtil.getDate("dd")) mRepo.getDueTasks(this)
//        else mRepo.getDueTasksWithDate(
//            date[DayBarChip.DAY]!!.toInt(),
//            date[DayBarChip.MONTH]!!.toInt(),
//            date[DayBarChip.YEAR]!!.toInt(),
//            this
//        )
    }
}