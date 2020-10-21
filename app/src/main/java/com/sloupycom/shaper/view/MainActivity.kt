package com.sloupycom.shaper.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.shahryar.daybar.DayBar
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.ActivityMainBinding
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), DayBar.OnDayChangedListener {

    /**Values**/
    private var mBinding: ActivityMainBinding? = null
    private val adapter = TaskAdapter(this)
//    var liveDataMerger: MediatorLiveData<MutableList<Task>> = MediatorLiveData<MutableList<Task>>()

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

        recyclerView_todayDue.adapter = adapter

        adapter.setOnTaskStateListener(object : TaskAdapter.TaskStateListener {
            override fun onTaskStateChanged(task: Task) {
                mBinding?.viewModel?.onTaskStateChanged(task)
            }
        })

//        liveDataMerger.addSource(mBinding?.viewModel?.tasks1!!) { value -> liveDataMerger.value = value }
        //Listen for data changes from viewModel
//        liveDataMerger.addSource(mBinding?.viewModel?.tasks!!) { value -> liveDataMerger.value = value }

        mBinding?.viewModel?.liveDataMerger?.observe(this, {
            it.let {
                adapter.data = it
            }
        })

//        mBinding?.viewModel?.tasks?.observe(this, {
//            it.let {
//                adapter.data = it
//            }
//        })

        //Change FAB visibility on recyclerView scroll
        recyclerView_todayDue.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // Scroll Down
                    if (floatingActionButton.isShown) {
                        floatingActionButton.hide();
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!floatingActionButton.isShown) {
                        floatingActionButton.show();
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

    override fun onSelectedDayChanged(index: Int, date: HashMap<String, String>, chip: DayBarChip) {
        mBinding?.viewModel?.dayChanged(index, date, chip)
    }

    /**
     * Called when selected day from DayBar changes
     */


}