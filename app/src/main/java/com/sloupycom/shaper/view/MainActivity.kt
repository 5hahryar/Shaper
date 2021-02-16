package com.sloupycom.shaper.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shahryar.daybar.DayBar
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.R
import com.sloupycom.shaper.database.Local
import com.sloupycom.shaper.databinding.ActivityMainBinding
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.model.adapter.SwipeToDeleteCallBack
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.utils.Util
import com.sloupycom.shaper.viewmodel.MainActivityViewModel
import com.sloupycom.shaper.viewmodel.MainActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), DayBar.OnDayChangedListener {

    /**Values**/
    private var mBinding: ActivityMainBinding? = null
    private lateinit var viewModel: MainActivityViewModel
    private val adapter = TaskAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Data binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, MainActivityViewModelFactory(application)).get(MainActivityViewModel::class.java)
        mBinding?.viewModel = viewModel
        mBinding?.lifecycleOwner = this

        //Setup DayBar listener
        dayBar?.setOnDayChangedListener(this)

        setupRecyclerView()

        mBinding?.viewModel?.busyDays?.observe(this, {
            dayBar.setIndicationByDay(Util().getBusyWeekDaysFromDateIndex(it))
            //TODO: issue -> Indication is not removed when busyDay is removed
        })

    }

    private fun setupRecyclerView() {
        recyclerView_todayDue.adapter = adapter

        //Setup on item swipe listener
        ItemTouchHelper(SwipeToDeleteCallBack(adapter)).attachToRecyclerView(recyclerView_todayDue)

        adapter.setOnTaskStateListener(object : TaskAdapter.TaskStateListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onTaskStateChanged(task: Task) {
                mBinding?.viewModel?.onTaskStateChanged(task)
            }

            override fun onTaskItemDeleted(task: Task) {
                mBinding?.viewModel?.deleteTaskItem(task)
                showItemDeletedSnackBar()
            }

            override fun onTaskItemDeleteUndo(task: Task) {
                mBinding?.viewModel?.undoDeleteTaskItem(task)
            }
        })

        //Observe liveData in order to update recyclerView
        mBinding?.viewModel?.liveDataMerger?.observe(this, {
            it.let {
                adapter.data = it
            }
        })

        //Change FAB visibility on recyclerView scroll
        recyclerView_todayDue.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // Scroll Down
                    if (floatingActionButton.isShown) {
                        floatingActionButton.hide()
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!floatingActionButton.isShown) {
                        floatingActionButton.show()
                    }
                }
            }
        })
    }

    /**
     * Set onClick method for buttons
     */
    fun onClick(view: View) {
        when (view.id) {
            R.id.floatingActionButton -> {
                AddTaskBottomSheet().show(supportFragmentManager, "AddTaskBottomSheet")
            }
            R.id.imageButton_settings -> {
                SettingsBottomSheet().show(supportFragmentManager, "SettingsBottomSheet")
            }
        }
    }

    private fun showItemDeletedSnackBar() {
        Snackbar.make(floatingActionButton, getText(R.string.delete_task), Snackbar.LENGTH_SHORT)
            .setAnchorView(floatingActionButton)
            .setBackgroundTint(getColor(R.color.colorSecondary))
            .setTextColor(getColor(R.color.colorOnSecondary))
            .setAction(getString(R.string.undo)) { adapter.undoRemove() }
            .setActionTextColor(getColor(R.color.colorOnSecondary))
            .show()
    }

    /**
     * Called when selected day from DayBar changes
     */
    override fun onSelectedDayChanged(index: Int, date: HashMap<String, String>, chip: DayBarChip) {
        mBinding?.viewModel?.dayChanged(index)
    }

}