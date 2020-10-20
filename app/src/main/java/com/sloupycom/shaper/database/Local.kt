package com.sloupycom.shaper.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sloupycom.shaper.model.Task

@Database(entities = [Task::class], version = 3, exportSchema = false)
abstract class Local: RoomDatabase() {

    abstract val localDao: LocalDao

    companion object {

        @Volatile
        private var INSTANCE: Local? = null

        fun getInstance(context: Context): Local {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        Local::class.java,
                        "shaper_local_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}