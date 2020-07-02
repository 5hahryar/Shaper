package com.sloupycom.shaper.View

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.ToggleButton
import com.google.android.material.chip.ChipGroup
import com.sloupycom.shaper.Controller.General
import com.sloupycom.shaper.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.day_chip.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var mGeneral = General()
    var dayBarList = mutableListOf<ToggleButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUi()
    }

    private fun setupUi() {
        textView_date.text = mGeneral.getDate("EEEE, MMM dd")
        textView_title.text = getString(R.string.activity_main_title)

        setupDayBar()
    }

    private fun setupDayBar() {
        dayBarList.addAll(listOf(chip1, chip2, chip3, chip4, chip5, chip6, chip7))
        val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        var dayOfWeek: Int = mGeneral.getDate("F").toInt()
        var mondayGap: Int = mGeneral.getDate("F").toInt() - 1
        var mondayDate:Int = mGeneral.getDate("d").toInt() - mondayGap

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



    fun onClick(view: View) {

    }
}