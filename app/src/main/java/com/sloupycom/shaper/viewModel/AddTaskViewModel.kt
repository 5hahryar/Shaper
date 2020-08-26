package com.sloupycom.shaper.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.utils.Util
import java.text.SimpleDateFormat
import kotlin.collections.HashMap

class AddTaskViewModel(application: Application): AndroidViewModel(application), DatePickerDialog.OnDateSetListener {

    /** Values **/
    private val context = getApplication<android.app.Application>().applicationContext
    @RequiresApi(Build.VERSION_CODES.N)
    val mCalendar: Calendar = Calendar.getInstance()
    var textDate: ObservableField<String> = ObservableField("")
    private val mRepo: Repo = Repo(application)
    private val mGeneral = Util(application)
    private var index: List<Int>? = null

    init {
        textDate.set(context.getString(R.string.today))
    }

    /**
     * Build a Task object
     */
    @SuppressLint("SimpleDateFormat")
    private fun buildTask(name: String, desc: String): HashMap<String, Any> {
        val creationDate = mGeneral.getDate("EEE MMM dd yyyy", mCalendar.time)
        if (index == null) {
            index = listOf(
                SimpleDateFormat("dd").format(mCalendar.time).toInt(),
                SimpleDateFormat("MM").format(mCalendar.time).toInt(),
                SimpleDateFormat("yyyy").format(mCalendar.time).toInt()
            )
        }
        val owId = mRepo.getUserCredentials()?.uid.toString()
        val id = mCalendar.timeInMillis.toString()
        val state: String = if (textDate.get() == SimpleDateFormat("EEEE, MMM dd yyyy")
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
        textDate.set(SimpleDateFormat("EEEE, MMM dd").format(date.time))
        this.index = listOf(
            SimpleDateFormat("dd").format(date.time).toInt(),
            SimpleDateFormat("MM").format(date.time).toInt(),
            SimpleDateFormat("yyyy").format(date.time).toInt())
    }

    fun addTask(name: String, desc: String) {
        mRepo.addTask(buildTask(name, desc))
    }
}