package com.sloupycom.shaper.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUi()
    }

    private fun setupUi() {
        populateDayBarList()

        textView_date.text = mGeneral.getDate("EEEE, MMM dd")
        textView_title.text = getString(R.string.activity_main_title)

        setupDayBar()
    }

    private fun populateDayBarList() {

    }

    private fun setupDayBar() {
        var dayOfWeek: Int = mGeneral.getDate("F").toInt()
        var mondayGap: Int = mGeneral.getDate("F").toInt() - 1
        var mondayDate:Int = mGeneral.getDate("d").toInt() - mondayGap

        chip1.textOn = "$mondayDate\n" + getString(R.string.day_mon)
        chip2.textOn = "${mondayDate+1}\n" + getString(R.string.day_tue)
        chip3.textOn = "${mondayDate+2}\n" + getString(R.string.day_wed)
        chip4.textOn = "${mondayDate+3}\n" + getString(R.string.day_thu)
        chip5.textOn = "${mondayDate+4}\n" + getString(R.string.day_fri)
        chip6.textOn = "${mondayDate+5}\n" + getString(R.string.day_sat)
        chip7.textOn = "${mondayDate+6}\n" + getString(R.string.day_sun)
        chip1.textOff = "$mondayDate\n" + getString(R.string.day_mon)
        chip2.textOff = "${mondayDate+1}\n" + getString(R.string.day_tue)
        chip3.textOff = "${mondayDate+2}\n" + getString(R.string.day_wed)
        chip4.textOff = "${mondayDate+3}\n" + getString(R.string.day_thu)
        chip5.textOff = "${mondayDate+4}\n" + getString(R.string.day_fri)
        chip6.textOff = "${mondayDate+5}\n" + getString(R.string.day_sat)
        chip7.textOff = "${mondayDate+6}\n" + getString(R.string.day_sun)

        chip1.isChecked = false
        chip2.isChecked = false
        chip3.isChecked = false
        chip4.isChecked = false
        chip5.isChecked = false
        chip6.isChecked = false
        chip7.isChecked = false

        for (i in 1..7) {
        }
    }
}