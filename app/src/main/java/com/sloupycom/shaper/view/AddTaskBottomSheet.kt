package com.sloupycom.shaper.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.BottomSheetAddTaskBinding
import com.sloupycom.shaper.viewModel.AddTaskViewModel
import kotlinx.android.synthetic.main.bottom_sheet_add_task.*

class AddTaskBottomSheet: BottomSheetDialogFragment() {

    private var binding: BottomSheetAddTaskBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
            = BottomSheetDialog(requireContext(), theme)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_task, container, false)
        binding?.viewModel = AddTaskViewModel(activity!!.application)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        textView_date.setOnClickListener {
            val picker = DatePickerDialog(context!!)
            picker.datePicker.minDate = Calendar.getInstance().timeInMillis
            picker.show()
            picker.setOnDateSetListener(binding?.viewModel)
        }

        button_add.setOnClickListener {
            binding?.viewModel?.addTask(editText_name.text.toString(),
                editText_desc.text.toString())
            dismiss()
        }

    }
}