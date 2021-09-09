package com.sloupycom.shaper.data.repository

import androidx.lifecycle.LiveData
import com.sloupycom.shaper.data.source.local.TaskLocalDataSourceImpl
import com.sloupycom.shaper.model.Task

class TaskRepositoryImpl(private val localDataSource: TaskLocalDataSourceImpl): TaskRepository {

    override fun getTasks(): LiveData<List<Task>>? =
        localDataSource.getTasks()

    override fun getTasks(dateIndex: String): LiveData<MutableList<Task>>? {
        return localDataSource.getTasks(dateIndex)
    }

    override fun getBusyDays(until: String): LiveData<List<String>>? {
        return localDataSource.getBusyDays(until)
    }

    override suspend fun addTask(task: Task) {
        localDataSource.addTask(task)
    }

    override suspend fun updateTask(task: Task) {
        localDataSource.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        localDataSource.deleteTask(task)
    }
}