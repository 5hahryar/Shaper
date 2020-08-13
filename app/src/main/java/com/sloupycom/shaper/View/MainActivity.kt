package com.sloupycom.shaper.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ToggleButton
import androidx.databinding.DataBindingUtil
import com.sloupycom.shaper.ViewModel.General
import com.sloupycom.shaper.Model.Adapter.TaskAdapter
import com.sloupycom.shaper.Model.Repo
import com.sloupycom.shaper.R
import com.sloupycom.shaper.ViewModel.MainActivityViewModel
import com.sloupycom.shaper.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.day_chip.*

class MainActivity : AppCompatActivity() {

    var mGeneral = General()
    var dayBarList = mutableListOf<ToggleButton>()
    private val mRepo = Repo()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        MainActivityViewModel()

        setupUi()

    }

    private fun setupUi() {
//        textView_date.text = mGeneral.getDate("EEEE, MMM dd")
//        textView_title.text = getString(R.string.activity_main_title)
        setupDayBar()
//        recyclerView_todayDue.layoutManager = LinearLayoutManager(this)
//        taskAdapter = TaskAdapter(mRepo, this)
//        recyclerView_todayDue.adapter = taskAdapter
    }

    private fun setupDayBar() {
        dayBarList.addAll(listOf(chip1, chip2, chip3, chip4, chip5, chip6, chip7))
        val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        val dayOfWeek: Int = mGeneral.getDate("F").toInt()
        val mondayGap: Int = mGeneral.getDate("F").toInt() - 1
        val mondayDate:Int = mGeneral.getDate("d").toInt() - mondayGap

        val onChipStateChanged = View.OnClickListener { v: View? ->
            val chip = v as ToggleButton
            if (chip.isChecked) {
                for (i in 0..6) {
                    dayBarList[i].isChecked = false
                }
                for (i in 0..6) {
                    if (dayBarList[i].id == chip.id) dayBarList[i].isChecked = true
                }
            }
            else chip.isChecked = true

            taskAdapter.updateWithDate(chip.text.toString(),
                mGeneral.getDate("MMM"),
                mGeneral.getDate("yyyy"))
        }

        for (i in 0..6) {
            dayBarList[i].textOn = "${mondayDate + i}\n${dayNames[mondayDate + i + 1]}"
            dayBarList[i].textOff = "${mondayDate + i}\n${dayNames[mondayDate + i + 1]}"
            dayBarList[i].setOnClickListener(onChipStateChanged)
        }

        for (i in 0..6) {
            if (i == dayOfWeek-1) dayBarList[i].isChecked = true
            else dayBarList[i].isChecked = false
        }

    }

    fun mainActivityOnClick(view: View) {
        when (view.id) {
            R.id.floatingActionButton -> {
                AddTaskBottomSheet().show(supportFragmentManager, "AddTaskBottomSheet")
            }
        }
    }
}