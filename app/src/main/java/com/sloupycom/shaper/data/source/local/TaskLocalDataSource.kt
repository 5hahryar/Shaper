package com.sloupycom.shaper.data.source.local

import androidx.lifecycle.LiveData
import com.sloupycom.shaper.data.TaskDataSource
import com.sloupycom.shaper.model.Task

class TaskLocalDataSource(private val taskDao: LocalDao): TaskDataSource {

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

}