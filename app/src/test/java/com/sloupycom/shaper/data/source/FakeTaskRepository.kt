package com.sloupycom.shaper.data.source

import androidx.lifecycle.LiveData
import com.sloupycom.shaper.data.repository.TaskRepository
import com.sloupycom.shaper.model.Task

class FakeTaskRepository: TaskRepository {

    override fun getTasks(): LiveData<List<Task>>? {
        TODO("Not yet implemented")
    }

    override fun getTasks(dateIndex: String): LiveData<MutableList<Task>>? {
        TODO("Not yet implemented")
    }

    override fun getTasksUntil(dateIndex: String): LiveData<MutableList<Task>>? {
        TODO("Not yet implemented")
    }

    override fun getBusyDays(until: String): LiveData<List<String>>? {
        TODO("Not yet implemented")
    }

    override suspend fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }
}