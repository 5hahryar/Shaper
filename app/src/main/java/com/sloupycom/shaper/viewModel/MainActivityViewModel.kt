package com.sloupycom.shaper.viewModel

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.shahryar.daybar.DayBar
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.utils.General
import com.sloupycom.shaper.view.AddTaskBottomSheet
import com.sloupycom.shaper.view.MainActivity
import com.sloupycom.shaper.view.SettingsBottomSheet
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
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
    private var dayBar: DayBar? = null

    /**
     * Initialize class
     */
    init {
        dayBar = activity.dayBar
        dayBar?.dayChangedListener = this
        adapter = TaskAdapter(this)
        mRepo.getDueTasks(this)
    }

    /**
     * Method is called when data set changes
     */
    override fun onDataChanged(data: ArrayList<Task>) {
        activity.shimmer.hideShimmer()
        if (data.isEmpty()) activity.recyclerView_empty.visibility = View.VISIBLE
        else activity.recyclerView_empty.visibility = View.GONE
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
            R.id.imageButton_settings -> {
                SettingsBottomSheet().show(activity.supportFragmentManager, "SettingsBottomSheet")
            }
        }
    }

    /**
     * Called when selected day from DayBar changes
     */
    override fun onSelectedDayChanged(date: HashMap<String, String>, chip: DayBarChip) {
        if (date[DayBarChip.DAY] == mGeneral.getDate("dd")) mRepo.getDueTasks(this)
        else mRepo.getDueTasksWithDate(date[DayBarChip.DAY]!!, date[DayBarChip.MONTH]!!, date[DayBarChip.YEAR]!!, this)
    }

    /**
     * Called when state of a task changes
     */
    override fun onTaskStateChanged(task: Task) {
        val taskDue: Date = SimpleDateFormat("EEE MMM dd yyyy").parse(task.next_due)
        val today = SimpleDateFormat("EEE MMM dd yyyy").parse(mGeneral.getDate("EEE MMM dd yyyy"))
        when {
            task.state != "DONE" -> task.state = "DONE"
            taskDue.after(today) -> task.state = "ONGOING"
            taskDue.before(today) -> task.state = "OVERDUE"
            else -> task.state = "DUE"
        }
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