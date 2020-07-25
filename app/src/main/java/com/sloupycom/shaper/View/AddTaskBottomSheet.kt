package com.sloupycom.shaper.View

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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.Controller.General
import com.sloupycom.shaper.Model.Repo
import com.sloupycom.shaper.R
import kotlinx.android.synthetic.main.bottom_sheet_add_task.*
import kotlinx.android.synthetic.main.bottom_sheet_add_task.view.*
import java.text.SimpleDateFormat
import java.util.*

class AddTaskBottomSheet: BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {

    val mGeneral = General()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
            = BottomSheetDialog(requireContext(), theme)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_task, container, false)

        setupUi(view)
        setListeners(view)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setListeners(view: View) {
        view.button_add.setOnClickListener {
            val task = hashMapOf(
                "creation_date" to "General().",
                "description" to editText_desc.text.toString(),
                "name" to editText_name.text.toString(),
                "next_due" to textView_date.text.toString(),
                "owner_id" to "null",
                "reminder" to "null",
                "repetition" to "null",
                "state" to "DUE"
            )
            Repo().addTask(task)
            dismiss()
        }

        view.textView_date.setOnClickListener{
            var picker = DatePickerDialog(context!!)
            picker.datePicker.minDate = Calendar.getInstance().timeInMillis
            picker.show()
            picker.setOnDateSetListener(this)
        }
    }

    private fun setupUi(view: View) {
        view.textView_date.text = General().getDate("EEEE, MMM dd")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        this.view?.textView_date?.text = SimpleDateFormat("EEEE, MMM dd")
            .format(date.time)
    }
}