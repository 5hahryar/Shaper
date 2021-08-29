package com.sloupycom.shaper.data.source.repository

import androidx.lifecycle.LiveData
import com.sloupycom.shaper.data.source.local.TaskLocalDataSource
import com.sloupycom.shaper.model.Task

class TaskRepositoryImpl(private val localDataSource: TaskLocalDataSource): TaskRepository {

    override fun getTasks(): LiveData<List<Task>>? =
        localDataSource.getTasks()


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