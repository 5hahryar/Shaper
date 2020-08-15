package com.sloupycom.shaper.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ToggleButton
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sloupycom.shaper.viewModel.General
import com.sloupycom.shaper.model.adapter.TaskAdapter
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.R
import com.sloupycom.shaper.viewModel.MainActivityViewModel
import com.sloupycom.shaper.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.day_chip.*

class MainActivity : AppCompatActivity() {

    var mGeneral = General()
    var dayBarList = mutableListOf<ToggleButton>()
    private val mRepo = Repo()
    private lateinit var taskAdapter: TaskAdapter

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.viewModel = MainActivityViewModel(this)
        binding?.lifecycleOwner = this
        setupUi()

    }

    private fun setupUi() {
        setupDayBar()
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
}