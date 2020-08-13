package com.sloupycom.shaper.ViewModel

import androidx.lifecycle.ViewModel
import com.sloupycom.shaper.Model.Repo

class MainActivityViewModel : ViewModel(), Repo.OnDataChanged {

    private val mGeneral = General()

    var todayDate: String = mGeneral.getDate("EEEE, MMM dd")
    var taskList: ArrayList<com.sloupycom.shaper.Model.Task> = arrayListOf()

    init {
        Repo().getDueTasks(this)
    }

    override fun onDataChanged(data: ArrayList<com.sloupycom.shaper.Model.Task>) {
        taskList.clear()
        taskList = data
    }
}