package com.sloupycom.shaper.viewModel

import android.content.Context
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.view.AddTaskBottomSheet
import com.sloupycom.shaper.view.MainActivity


class MainActivityViewModel(val activity: MainActivity) : ViewModel(), Repo.OnDataChanged {

    private val mGeneral = General()

    var todayDate: String = mGeneral.getDate("EEEE, MMM dd")
    var taskList: ArrayList<Task> = arrayListOf(Task("","","","","","","","","","",""))
    var adapter: TaskAdapter
    val mRepo = Repo()

    init {
        adapter = TaskAdapter(mRepo, activity)
        mRepo.getDueTasks(this)
    }

    override fun onDataChanged(data: ArrayList<Task>) {
        taskList.clear()
        taskList = data
    }

    fun onClick() {
        AddTaskBottomSheet().show(activity.supportFragmentManager, "AddTaskBottomSheet")
    }

}
@BindingAdapter("listData")
fun entries(recyclerView: RecyclerView?, list: ArrayList<Task>?) {
    //write your code to set RecyclerView adapter.

}