package com.sloupycom.shaper.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.shahryar.daybar.DayBar
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.R
import com.sloupycom.shaper.viewmodel.MainActivityViewModel
import com.sloupycom.shaper.databinding.ActivityMainBinding
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.model.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottomsheet_add_task.*

class MainActivity : AppCompatActivity(), DayBar.OnDayChangedListener {

    /**Values**/
    private var mBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Data binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding?.viewModel = MainActivityViewModel(application)
        mBinding?.lifecycleOwner = this

        //Setup DayBar listener
        dayBar?.setOnDayChangedListener(this)

        setupRecyclerView()

//        mBinding?.viewModel?.busyDays?.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
//            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
//                dayBar?.setIndicationByDay((sender as ObservableField<List<Int>>).get()!!)
//            }
//        })

    }

    private fun setupRecyclerView() {

        val adapter = TaskAdapter(this, )
        recyclerView_todayDue.adapter = adapter

        adapter.setOnTaskStateListener(object: TaskAdapter.TaskStateListener{
            override fun onTaskStateChanged(task: Task) {
                mBinding?.viewModel?.onTaskStateChanged(task)
            }
        })

        //Listen for data changes from viewModel
        mBinding?.viewModel?.tasks?.observe(this, {
            it.let {
                adapter.data = it
            }
        })

        //Change FAB visibility on recyclerView scroll
        recyclerView_todayDue.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy >0) {
                    // Scroll Down
                    if (floatingActionButton.isShown) {
                        floatingActionButton.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!floatingActionButton.isShown) {
                        floatingActionButton.show();
                    }
                }
            }})
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

    /**
     * Called when selected day from DayBar changes
     */
    override fun onSelectedDayChanged(date: HashMap<String, String>, chip: DayBarChip) {
        mBinding?.viewModel?.dayChanged(date, chip)
    }
}