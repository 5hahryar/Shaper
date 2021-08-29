package com.sloupycom.shaper.data.source.repository

import androidx.lifecycle.LiveData
import com.sloupycom.shaper.model.Task

interface TaskRepository {

    fun getTasks(): LiveData<List<Task>>?

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}