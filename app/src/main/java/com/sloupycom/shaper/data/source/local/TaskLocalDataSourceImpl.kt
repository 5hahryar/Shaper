package com.sloupycom.shaper.data.source.local

import androidx.lifecycle.LiveData
import com.sloupycom.shaper.data.TaskDataSource
import com.sloupycom.shaper.model.Task

class TaskLocalDataSourceImpl(private val taskDao: LocalDao): TaskDataSource {

    override suspend fun addTask(task: Task) {
        taskDao.insert(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    override fun getTasks(): LiveData<List<Task>>? =
        taskDao.getTasks()

    override fun getTasks(dateIndex: String): LiveData<MutableList<Task>>? {
        return taskDao.getTasks(dateIndex)
    }

    override fun getBusyDays(until: String): LiveData<List<String>>? {
        return taskDao.getBusyDaysOfWeek(until)
    }

}