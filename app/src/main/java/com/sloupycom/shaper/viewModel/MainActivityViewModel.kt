package com.sloupycom.shaper.viewModel

import android.app.Application
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivityViewModel(application: Application) : AndroidViewModel(application),
    Repo.OnDataChanged,
    DayBar.OnDayChangedListener,
    TaskAdapter.TaskStateListener{

    /** Values **/
    private val context = getApplication<android.app.Application>().applicationContext
    private val mGeneral = General()
    private val mRepo = Repo()
    var todayDate: String = mGeneral.getDate("EEEE, MMM dd")
    var adapter: TaskAdapter
    private var mDayBar: DayBar? = null

    /**
     * Initialize class
     */
    init {
        mDayBar = context.dayBar
        mDayBar?.dayChangedListener = this
        adapter = TaskAdapter(context, this)
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
        else mRepo.getDueTasksWithDate(
            date[DayBarChip.DAY]!!.toInt(),
            date[DayBarChip.MONTH]!!.toInt(),
            date[DayBarChip.YEAR]!!.toInt(),
            this
        )
    }

    /**
     * Called when state of a task changes
     */
    override fun onTaskStateChanged(task: Task) {
        if (task.state != "DONE") task.state = "DONE"
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