package com.sloupycom.shaper.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sloupycom.shaper.R
import com.sloupycom.shaper.viewModel.MainActivityViewModel
import com.sloupycom.shaper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setup databinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.viewModel = MainActivityViewModel(this)
        binding?.lifecycleOwner = this

    }
}