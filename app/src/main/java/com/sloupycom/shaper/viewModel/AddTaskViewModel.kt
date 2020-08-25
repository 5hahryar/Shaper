package com.sloupycom.shaper.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.utils.General
import com.sloupycom.shaper.view.AddTaskBottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_add_task.*
import java.text.SimpleDateFormat
import kotlin.collections.HashMap

class AddTaskViewModel(application: Application): AndroidViewModel(application), DatePickerDialog.OnDateSetListener {

    /** Values **/
    private val context = getApplication<android.app.Application>().applicationContext
    @RequiresApi(Build.VERSION_CODES.N)
    val mCalendar: Calendar = Calendar.getInstance()
    var mDate = context.resources.getString(R.string.today)
    private val mRepo: Repo = Repo()
    private val mGeneral = General()
    private var index: List<Int>? = null

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
                val picker = DatePickerDialog(context)
                picker.datePicker.minDate = Calendar.getInstance().timeInMillis
                picker.show()
                picker.setOnDateSetListener(this)
            }
        }
    }

    /**
     * Build a Task object
     */
    @SuppressLint("SimpleDateFormat")
    private fun buildTask(): HashMap<String, Any> {
        val creationDate = mGeneral.getDate("EEE MMM dd yyyy", mCalendar.time)
        val desc = bottomSheet.editText_desc.text.toString()
        val name = bottomSheet.editText_name.text.toString()
        if (index == null) {
            index = listOf(
                SimpleDateFormat("dd").format(mCalendar.time).toInt(),
                SimpleDateFormat("MM").format(mCalendar.time).toInt(),
                SimpleDateFormat("yyyy").format(mCalendar.time).toInt()
            )
        }
        val owId = mRepo.getUserCredentials()?.uid.toString()
        val id = mCalendar.timeInMillis.toString()
        val state: String = if (bottomSheet.textView_date.text == SimpleDateFormat("EEEE, MMM dd yyyy")
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
            "next_due" to index!!,
            "state" to state
        )
    }

    /**
     * Called when user selects a day from DatePicker
     */
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        bottomSheet.textView_date.text = SimpleDateFormat("EEEE, MMM dd").format(date.time)
        this.index = listOf(
            SimpleDateFormat("dd").format(date.time).toInt(),
            SimpleDateFormat("MM").format(date.time).toInt(),
            SimpleDateFormat("yyyy").format(date.time).toInt())
    }
}