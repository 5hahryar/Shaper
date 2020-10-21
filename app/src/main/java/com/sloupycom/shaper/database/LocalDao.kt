package com.sloupycom.shaper.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sloupycom.shaper.model.Task
import java.util.*

@Dao
interface LocalDao {

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query ("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<MutableList<Task>>?

    @Query ("SELECT * FROM task_table WHERE next_due <= :todayDateIndex AND state = 'ONGOING'")
    fun getTodayTasks(todayDateIndex: String): LiveData<MutableList<Task>>?

    @Query ("SELECT * FROM task_table WHERE next_due = :dateIndex")
    fun getDayTasks(dateIndex: String): LiveData<MutableList<Task>>?
}