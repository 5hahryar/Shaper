package com.sloupycom.shaper.viewModel

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.utils.General
import com.sloupycom.shaper.view.AddTaskBottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_add_task.*
import kotlinx.android.synthetic.main.bottom_sheet_add_task.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddTaskViewModel(private val bottomSheet: AddTaskBottomSheet): ViewModel(), DatePickerDialog.OnDateSetListener {

    /** Values **/
    @RequiresApi(Build.VERSION_CODES.N)
    val mCalendar: Calendar = Calendar.getInstance()
    val date = General().getDate("EEEE, MMM dd yyyy")
    private val mRepo: Repo = Repo()
    private val mGeneral = General()
    private var nextDue: String? = null

    /**
     * Set onClick method for buttons
     */
    fun onClick(view: View) {
        when(view.id) {
            R.id.button_add -> {
                mRepo.addTask(buildTask())
                bottomSheet.dismiss()
            }
            R.id.textView_date -> {
                var picker = DatePickerDialog(bottomSheet.context!!)
                picker.datePicker.minDate = Calendar.getInstance().timeInMillis
                picker.show()
                picker.setOnDateSetListener(this)
            }
        }
    }

    /**
     * Build a Task object
     */
    private fun buildTask(): HashMap<String, String> {
        val creationDate = mGeneral.getDate("EEE MMM dd yyyy", mCalendar.time)
        val desc = bottomSheet.editText_desc.text.toString()
        val name = bottomSheet.editText_name.text.toString()
        if (nextDue == null) nextDue = SimpleDateFormat("EEE MMM dd yyyy").format(Calendar.getInstance().time)
        val owId = mRepo.getUserCredentials()?.uid.toString()
        val rem = ""
        val rep = ""
        var state = ""
        val id = mCalendar.timeInMillis.toString()
        state = if (bottomSheet.textView_date.text == SimpleDateFormat("EEEE, MMM dd yyyy")
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
            "reminder" to rem,
            "repetition" to rep,
            "state" to state,
            "next_due" to nextDue!!
        )
    }

    /**
     * Called when user selects a day from DatePicker
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        if (year == mCalendar.time.year) {
            bottomSheet.textView_date?.text = SimpleDateFormat("EEEE, MMM dd")
                .format(date.time)
        }
        else {
            bottomSheet.textView_date?.text = SimpleDateFormat("EEEE, MMM dd yyyy")
                .format(date.time)
        }
        this.nextDue = SimpleDateFormat("EEE MMM dd yyyy").format(date.time)
    }
}