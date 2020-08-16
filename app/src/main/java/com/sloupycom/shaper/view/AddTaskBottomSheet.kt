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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.viewModel.General
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.R
import kotlinx.android.synthetic.main.bottom_sheet_add_task.*
import kotlinx.android.synthetic.main.bottom_sheet_add_task.view.*
import java.text.SimpleDateFormat
import kotlin.collections.HashMap

class AddTaskBottomSheet: BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {

    val mGeneral = General()
    @RequiresApi(Build.VERSION_CODES.N)
    val mCalendar: Calendar = Calendar.getInstance()
    private val mRepo: Repo = Repo()

    var nextDue: Calendar? = null

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
            mRepo.addTask(buildTask())
            dismiss()
        }

        view.textView_date.setOnClickListener{
            var picker = DatePickerDialog(context!!)
            picker.datePicker.minDate = Calendar.getInstance().timeInMillis
            picker.show()
            picker.setOnDateSetListener(this)
        }
    }

    private fun buildTask(): HashMap<String, String> {
        val creationDate = mCalendar.time.toString()
        val desc = editText_desc.text.toString()
        val name = editText_name.text.toString()
        val nDueD: String = if (nextDue != null) {
            mGeneral.getDate("dd", nextDue!!.time)
        } else {
            mGeneral.getDate("dd", mCalendar.time)
        }
        val nDueM: String = if (nextDue != null) {
            mGeneral.getDate("MM", nextDue!!.time)
        } else {
            mGeneral.getDate("MM", mCalendar.time)
        }
        val nDueY: String = if (nextDue != null) {
            mGeneral.getDate("yyyy", nextDue!!.time)
        } else {
            mGeneral.getDate("yyyy", mCalendar.time)
        }
        val owId = mRepo.mUser?.uid.toString()
        val rem = ""
        val rep = ""
        var state = ""
        val id = mCalendar.timeInMillis.toString()
        state = if (textView_date.text == SimpleDateFormat("EEEE, MMM dd yyyy")
                .format(java.util.Calendar.getInstance().time)) {
            "DUE"
        } else {
            "ONGOING"
        }
        return hashMapOf(
            "id" to id,
            "owner_id" to owId,
            "name" to name,
            "description" to desc,
            "creation_date" to creationDate,
            "next_due_day" to nDueD,
            "next_due_month" to nDueM,
            "next_due_year" to nDueY,
            "reminder" to rem,
            "repetition" to rep,
            "state" to state
        )
    }

    private fun setupUi(view: View) {
        view.textView_date.text = General().getDate("EEEE, MMM dd yyyy")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        if (year == mCalendar.time.year) {
            this.view?.textView_date?.text = SimpleDateFormat("EEEE, MMM dd")
                .format(date.time)
        }
        else {
            this.view?.textView_date?.text = SimpleDateFormat("EEEE, MMM dd yyyy")
                .format(date.time)
        }
        this.nextDue = date
    }
}