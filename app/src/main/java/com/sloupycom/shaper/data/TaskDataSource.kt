package com.sloupycom.shaper.data

import androidx.lifecycle.LiveData
import com.sloupycom.shaper.model.Task

interface TaskDataSource {

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    fun getTasks(): LiveData<List<Task>>?
}