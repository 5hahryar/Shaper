package com.sloupycom.shaper.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.utils.General
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.BottomSheetAddTaskBinding
import com.sloupycom.shaper.viewModel.AddTaskViewModel
import kotlinx.android.synthetic.main.bottom_sheet_add_task.*
import kotlinx.android.synthetic.main.bottom_sheet_add_task.view.*
import java.text.SimpleDateFormat
import kotlin.collections.HashMap

class AddTaskBottomSheet: BottomSheetDialogFragment() {

    private var binding: BottomSheetAddTaskBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
            = BottomSheetDialog(requireContext(), theme)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_task, container, false)
        binding?.viewModel = AddTaskViewModel(this)

        return binding?.root
    }
}