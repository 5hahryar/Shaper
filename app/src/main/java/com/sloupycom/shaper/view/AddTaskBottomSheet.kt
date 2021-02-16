package com.sloupycom.shaper.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sloupycom.shaper.R
import com.sloupycom.shaper.database.Local
import com.sloupycom.shaper.databinding.BottomsheetAddTaskBinding
import com.sloupycom.shaper.utils.TextInputFilter
import com.sloupycom.shaper.viewmodel.AddTaskViewModel
import kotlinx.android.synthetic.main.bottomsheet_add_task.*
import java.text.SimpleDateFormat


class AddTaskBottomSheet : BottomSheetDialogFragment() {

    /**Values**/
    private lateinit var mBinding: BottomsheetAddTaskBinding
    private lateinit var viewModel: AddTaskViewModel
    private var vibrator: Vibrator? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AddTaskBottomSheet)
        return super.onCreateDialog(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_task, container, false)
        viewModel = ViewModelProvider(this).get(AddTaskViewModel::class.java)
        viewModel.mLocal = Local.getInstance(requireContext())
        mBinding.viewModel = viewModel

        vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setOnTaskAddedListener(object : AddTaskViewModel.OnTaskAddedListener {
            override fun onTaskAdded() {
                dismiss()
                showSnackBar()
            }
        })

        viewModel.textError.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                editTextLayout_title.error = (sender as ObservableField<String>).get()
            }
        })

        //Set filter for repetition editText
        editText_repetition.filters = arrayOf(TextInputFilter(1, 365))

        toggleGroup.setOnSelectListener {
            if (it.isSelected) {
                expandable_layout.expand()
                vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
                showSoftKeyboard(view.findViewById(R.id.editText_repetition))
            }
            else {
                expandable_layout.collapse()
                hideSoftKeyboard(view.findViewById(R.id.editText_repetition))
            }
        }

        mBinding.textViewDate.setOnClickListener {
            val picker = DatePickerDialog(requireContext())
            picker.datePicker.minDate = Calendar.getInstance().timeInMillis
            picker.show()
            picker.setOnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.onDateSelected(year, month, dayOfMonth)
            }
        }
    }

    private fun showSnackBar() {
        val fab: ExtendedFloatingActionButton = activity!!.findViewById(R.id.floatingActionButton)
        Snackbar.make(fab, getText(R.string.on_task_added), Snackbar.LENGTH_SHORT)
            .setAnchorView(fab)
            .setBackgroundTint(activity!!.getColor(R.color.colorSecondary))
            .setTextColor(activity!!.getColor(R.color.colorOnSecondary))
            .show()
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideSoftKeyboard(view: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

