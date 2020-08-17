package com.sloupycom.shaper.viewModel

import android.content.Context
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.shahryar.daybar.DayBar
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.view.AddTaskBottomSheet
import com.sloupycom.shaper.view.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivityViewModel(private val activity: MainActivity) : ViewModel(),
    Repo.OnDataChanged,
    DayBar.OnDayChangedListener,
    TaskAdapter.TaskStateListener{

    /** Values **/
    private val mGeneral = General()
    private val mRepo = Repo()
    var todayDate: String = mGeneral.getDate("EEEE, MMM dd")
    var adapter: TaskAdapter
    var dayBar: DayBar? = null

    /**
     * Initialize class
     */
    init {
        dayBar = activity.dayBar
        dayBar?.dayChangedListener = this
        adapter = TaskAdapter(activity, this)
        mRepo.getDueTasks(this)
    }

    /**
     * Method is called when data set changes
     */
    override fun onDataChanged(data: ArrayList<Task>) {
        adapter.mList.clear()
        adapter.mList.addAll(data)
        adapter.notifyDataSetChanged()
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

    /**
     * Called when selected day from DayBar changes
     */
    override fun onSelectedDayChanged(date: HashMap<String, String>, chip: DayBarChip) {
        mRepo.getDueTasksWithDate(date[DayBarChip.DAY]!!, date[DayBarChip.MONTH]!!, date[DayBarChip.YEAR]!!, this)
    }

    /**
     * Called when state of a task changes
     */
    override fun onTaskStateChanged(task: Task) {
        if (task.state != "DONE") task.state = "DONE"
        else if (task.next_due_day.equals(mGeneral.getDate("dd"))) task.state = "DUE"
        else task.state = "ONGOING"
        mRepo.updateTask(task)
    }
}

/**
 * Set adapter which is passed from xml layout to recyclerView
 */
@BindingAdapter(value = ["setAdapter"])
fun setAdapter(recyclerView: RecyclerView, adapter: TaskAdapter) {
    recyclerView.adapter = adapter
}