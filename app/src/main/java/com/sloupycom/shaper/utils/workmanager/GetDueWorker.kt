package com.sloupycom.shaper.utils.workmanager

import android.app.Application
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.model.Task
import kotlinx.coroutines.runBlocking

class GetDueWorker (val application: Application, workerParams: WorkerParameters):
    Worker(application, workerParams), Repo.OnDataChanged {

    private var isWorkDone: Boolean = false
    private var mData: ArrayList<Task>? = null

    override fun doWork(): Result {
        Repo().getDueTasks(this)
        return if (isWorkDone && mData != null) Result.success()
        else Result.retry()
    }

    override fun onDataChanged(data: ArrayList<Task>) {
        mData = data
        isWorkDone = true
    }

}