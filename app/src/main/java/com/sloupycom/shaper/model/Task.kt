package com.sloupycom.shaper.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "New Task",

    @ColumnInfo(name = "creation_date")
    var creation_date: String = "",

    @ColumnInfo(name = "next_due")
    var next_due: String = "",

    @ColumnInfo(name = "state")
    var state: String = "",

    @ColumnInfo(name = "repetition")
    var repetition: Int? = null
)