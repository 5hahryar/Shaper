package com.sloupycom.shaper.viewModel

import android.content.Context
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.view.AddTaskBottomSheet
import com.sloupycom.shaper.view.MainActivity


class MainActivityViewModel(private val activity: MainActivity) : ViewModel(), Repo.OnDataChanged {

    /** Values **/
    private val mGeneral = General()
    private val mRepo = Repo()
    var todayDate: String = mGeneral.getDate("EEEE, MMM dd")
    var taskList: ArrayList<Task> = arrayListOf()
    var adapter: TaskAdapter

    /**
     * Initialize class
     */
    init {
        adapter = TaskAdapter(mRepo, activity)
        mRepo.getDueTasks(this)
    }

    /**
     * Method is called when data set changes
     */
    override fun onDataChanged(data: ArrayList<Task>) {
        taskList.clear()
        taskList = data
    }

    /**
     * Set onClick method for buttons
     */
    fun onClick(view: View) {
        when (view.id) {
            R.id.floatingActionButton -> {
                AddTaskBottomSheet().show(activity.supportFragmentManager, "AddTaskBottomSheet")
            }
        }
    }

}

/**
 * Set adapter which is passed from xml layout to recyclerView
 */
@BindingAdapter(value = ["setAdapter"])
fun setAdapter(recyclerView: RecyclerView, adapter: TaskAdapter) {
    recyclerView.adapter = adapter
}