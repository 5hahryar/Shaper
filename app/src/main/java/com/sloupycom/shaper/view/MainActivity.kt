package com.sloupycom.shaper.view

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shahryar.daybar.DayBar
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.ActivityMainBinding
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.model.adapter.SwipeToDeleteCallBack
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), DayBar.OnDayChangedListener {

    /**Values**/
    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel: MainActivityViewModel by viewModel()
    private val mTaskAdapter = TaskAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Data binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        //Setup DayBar listener
        dayBar?.setOnDayChangedListener(this)

        setupRecyclerView()

        listenToEvents()

//        mBinding.viewModel?.busyDays?.observe(this, {
//            dayBar.setIndicationByDay(Util().getBusyWeekDaysFromDateIndex(it))
//        })

    }

    private fun listenToEvents() {
        mViewModel.newTaskEvent.observe(this, {
            AddTaskBottomSheet().show(supportFragmentManager, "AddTaskBottomSheet")
        })

        mViewModel.openSettingsEvent.observe(this, {
            SettingsBottomSheet().show(supportFragmentManager, "SettingsBottomSheet")
        })
    }

    private fun setupRecyclerView() {
        recyclerView_todayDue.adapter = mTaskAdapter

        //Setup on item swipe listener
        ItemTouchHelper(SwipeToDeleteCallBack(mTaskAdapter)).attachToRecyclerView(recyclerView_todayDue)

        mTaskAdapter.setOnTaskStateListener(object : TaskAdapter.TaskStateListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onTaskStateChanged(task: Task) {
                mBinding.viewModel?.onTaskStateChanged(task)
            }

            override fun onTaskItemDeleted(task: Task) {
                mBinding.viewModel?.deleteTaskItem(task)
                showItemDeletedSnackBar()
            }

            override fun onTaskItemDeleteUndo(task: Task) {
                mBinding.viewModel?.undoDeleteTaskItem(task)
            }
        })

        //Observe liveData in order to update recyclerView
        mBinding.viewModel?.liveDataMerger?.observe(this, {
            it.let {
                mTaskAdapter.data = it
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

    private fun showItemDeletedSnackBar() {
        Snackbar.make(floatingActionButton, getText(R.string.delete_task), Snackbar.LENGTH_SHORT)
            .setAnchorView(floatingActionButton)
            .setBackgroundTint(getColor(R.color.colorSecondary))
            .setTextColor(getColor(R.color.colorOnSecondary))
            .setAction(getString(R.string.undo)) { mTaskAdapter.undoRemove() }
            .setActionTextColor(getColor(R.color.colorOnSecondary))
            .show()
    }

    /**
     * Called when selected day from DayBar changes
     */
    override fun onSelectedDayChanged(index: Int, date: HashMap<String, String>, chip: DayBarChip) {
        mBinding.viewModel?.dayChanged(index)
    }

}