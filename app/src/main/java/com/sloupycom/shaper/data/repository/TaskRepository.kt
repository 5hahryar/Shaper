package com.sloupycom.shaper.data.repository

import androidx.lifecycle.LiveData
import com.sloupycom.shaper.model.Task

interface TaskRepository {

    fun getTasks(): LiveData<List<Task>>?

    fun getTasks(dateIndex: String): LiveData<MutableList<Task>>?

    fun getTasksUntil(dateIndex: String): LiveData<MutableList<Task>>?

    fun getBusyDays(until: String): LiveData<List<String>>?

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}