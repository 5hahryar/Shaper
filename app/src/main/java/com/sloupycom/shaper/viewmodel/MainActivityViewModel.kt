package com.sloupycom.shaper.viewmodel

import android.app.Application
import android.content.Context
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.dagger.DaggerDependencyComponent
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.model.Task
import kotlin.collections.ArrayList


class MainActivityViewModel(application: Application, activityContext: Context) : AndroidViewModel(application),
    Repo.OnDataChanged,
    TaskAdapter.TaskStateListener{

    /** Values **/
    private val mComponent = DaggerDependencyComponent.create()
    private val mUtil = mComponent.getUtil()
    private val mRepo = mComponent.getRepo()

    var textDate: ObservableField<String> = ObservableField(mUtil.getDate("EEEE, MMM dd"))
    var adapter: ObservableField<TaskAdapter> = ObservableField()
    var isEmpty: ObservableBoolean = ObservableBoolean(true)
    var isLoading: ObservableBoolean = ObservableBoolean(true)

    init {
        adapter.set(TaskAdapter(this, activityContext))
        mRepo.getDueTasks(this)
        Int
    }

    /**
     * Method is called when data set changes
     */
    override fun onDataChanged(data: ArrayList<Task>) {
        isLoading.set(false)
        isEmpty.set(data.isEmpty())
        adapter.get()?.mList?.clear()
        adapter.get()?.mList?.addAll(data)
        adapter.get()?.notifyDataSetChanged()
    }

    /**
     * Called when state of a task changes
     */
    override fun onTaskStateChanged(task: Task) {
        if (task.state != "DONE") task.state = "DONE"
        else task.state = "ONGOING"
        mRepo.updateTask(task)
    }

    fun dayChanged(date: java.util.HashMap<String, String>, chip: DayBarChip) {
        isLoading.set(true)
        if (date[DayBarChip.DAY] == mUtil.getDate("dd")) mRepo.getDueTasks(this)
        else mRepo.getDueTasksWithDate(
            date[DayBarChip.DAY]!!.toInt(),
            date[DayBarChip.MONTH]!!.toInt(),
            date[DayBarChip.YEAR]!!.toInt(),
            this
        )
    }
}

/**
 * Set adapter which is passed from xml layout to recyclerView
 */
@BindingAdapter(value = ["setAdapter"])
fun setAdapter(recyclerView: RecyclerView, adapter: TaskAdapter) {
    recyclerView.adapter = adapter
}