package com.sloupycom.shaper.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sloupycom.shaper.model.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [Task::class], version = 5, exportSchema = false)
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

                    //Clear Done tasks
                    GlobalScope.launch {
                        INSTANCE!!.localDao.removeOldDoneTasks(
                            com.sloupycom.shaper.utils.Util().getDateIndex(Calendar.getInstance())
                        )
                    }
                }
                return instance
            }
        }
    }

}