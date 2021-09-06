package com.sloupycom.shaper.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sloupycom.shaper.model.Task

@Dao
interface LocalDao {

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query ("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<MutableList<Task>>?

    @Query ("SELECT * FROM task_table")
    fun getTasks(): LiveData<List<Task>>?

    @Query ("SELECT * FROM task_table WHERE next_due <= :dateIndex")
    fun getTasks(dateIndex: String): LiveData<MutableList<Task>>?

    @Query ("SELECT * FROM task_table WHERE next_due = :dateIndex")
    fun getDayTasks(dateIndex: String): LiveData<MutableList<Task>>?

    @Query ("DELETE FROM task_table WHERE next_due < :dateIndex AND state = 'DONE'")
    suspend fun removeOldDoneTasks(dateIndex: String)

    @Query("SELECT DISTINCT next_due FROM task_table WHERE next_due <= :dayUntil")
    fun getBusyDaysOfWeek(dayUntil: String): LiveData<List<String>>?

    @Query ("SELECT * FROM task_table WHERE next_due <= :todayDateIndex")
    suspend fun getReminderTasks(todayDateIndex: String): List<Task>
}