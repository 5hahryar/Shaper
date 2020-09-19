package com.sloupycom.shaper.viewmodel

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
import com.sloupycom.shaper.dagger.DaggerDependencyComponent
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.utils.Util
import java.text.SimpleDateFormat
import kotlin.collections.HashMap

class AddTaskViewModel(application: Application): AndroidViewModel(application), DatePickerDialog.OnDateSetListener {

    /** Values **/
    private val mComponent = DaggerDependencyComponent.create()
    private val mRepo: Repo = mComponent.getRepo()
    private val mUtil = mComponent.getUtil()
    private val mContext = getApplication<Application>().applicationContext
    @RequiresApi(Build.VERSION_CODES.N) private val mCalendar: Calendar = Calendar.getInstance()
    private var mDateIndex: List<Int>? = null
    var textDate: ObservableField<String> = ObservableField("")

    init {
        textDate.set(mContext.getString(R.string.today))
    }

    /**
     * Build a Task object
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    private fun buildTask(title: String): HashMap<String, Any> {
        val creationDate = mUtil.getDate("EEE MMM dd yyyy", mCalendar.time)
        if (mDateIndex == null) {
            mDateIndex = listOf(
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
            "title" to title,
            "creation_date" to creationDate,
            "next_due" to mDateIndex!!,
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
        this.mDateIndex = listOf(
            SimpleDateFormat("dd").format(date.time).toInt(),
            SimpleDateFormat("MM").format(date.time).toInt(),
            SimpleDateFormat("yyyy").format(date.time).toInt())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addTask(name: String) {
        mRepo.addTask(buildTask(name))
    }
}