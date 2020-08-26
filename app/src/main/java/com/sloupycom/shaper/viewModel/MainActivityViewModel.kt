package com.sloupycom.shaper.viewModel

import android.app.Application
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.utils.Util
import kotlin.collections.ArrayList


class MainActivityViewModel(application: Application) : AndroidViewModel(application),
    Repo.OnDataChanged,
    TaskAdapter.TaskStateListener{

    /** Values **/
    private val context = getApplication<Application>().applicationContext
    private val mGeneral = Util(application)
    private val mRepo = Repo(application)
    var textDate: ObservableField<String> = ObservableField(mGeneral.getDate("EEEE, MMM dd"))
    var adapter: ObservableField<TaskAdapter> = ObservableField()

    /**
     * Initialize class
     */
    init {

        adapter.set(TaskAdapter(application, this))
        mRepo.getDueTasks(this)
    }

    /**
     * Method is called when data set changes
     */
    override fun onDataChanged(data: ArrayList<Task>) {
//        activity.shimmer.hideShimmer()
//        if (data.isEmpty()) activity.recyclerView_empty.visibility = View.VISIBLE
//        else activity.recyclerView_empty.visibility = View.GONE
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
        if (date[DayBarChip.DAY] == mGeneral.getDate("dd")) mRepo.getDueTasks(this)
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