package com.sloupycom.shaper.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.shahryar.daybar.DayBar
import com.shahryar.daybar.DayBarChip
import com.sloupycom.shaper.R
import com.sloupycom.shaper.viewmodel.MainActivityViewModel
import com.sloupycom.shaper.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DayBar.OnDayChangedListener {

    /**Values**/
    private var mBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding?.viewModel = MainActivityViewModel(application, baseContext)
        mBinding?.lifecycleOwner = this
        dayBar?.setOnDayChangedListener(this)

        mBinding?.viewModel?.busyDays?.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                dayBar?.setIndicationByDay((sender as ObservableField<List<Int>>).get()!!)
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

    /**
     * Called when selected day from DayBar changes
     */
    override fun onSelectedDayChanged(date: HashMap<String, String>, chip: DayBarChip) {
        mBinding?.viewModel?.dayChanged(date, chip)
    }
}