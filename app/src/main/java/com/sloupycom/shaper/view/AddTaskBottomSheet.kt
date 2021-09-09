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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.BottomsheetAddTaskBinding
import com.sloupycom.shaper.core.util.TextInputFilter
import com.sloupycom.shaper.viewmodel.AddTaskViewModel
import kotlinx.android.synthetic.main.bottomsheet_add_task.*
import org.koin.android.ext.android.inject


class AddTaskBottomSheet : BottomSheetDialogFragment() {

    /**Values**/
    private lateinit var mBinding: BottomsheetAddTaskBinding
    private val mViewModel: AddTaskViewModel by inject()
    private var mVibrator: Vibrator? = null

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
        mBinding.viewModel = mViewModel

        mVibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupListeners() {

        mViewModel.newTaskAddedEvent.observe(this, {
            dismiss()
            showSnackBar()
        })

        //TODO: Use two way data binding instead
        mViewModel.textError.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                editTextLayout_title.error = (sender as ObservableField<String>).get()
            }
        })

        mViewModel.openDatePickerEvent.observe(this, {
            val picker = DatePickerDialog(requireContext())
            picker.datePicker.minDate = Calendar.getInstance().timeInMillis
            picker.show()
            picker.setOnDateSetListener { _, year, month, dayOfMonth ->
                mViewModel.onDateSelected(year, month, dayOfMonth)
            }
        })

        toggleGroup.setOnSelectListener {
            if (it.isSelected) {
                expandable_layout.expand()
                mVibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
                showSoftKeyboard(mBinding.root.findViewById(R.id.editText_repetition))
            }
            else {
                expandable_layout.collapse()
                hideSoftKeyboard(mBinding.root.findViewById(R.id.editText_repetition))
            }
        }
    }

    private fun setupViews() {
        //Set filter for repetition editText
        editText_repetition.filters = arrayOf(TextInputFilter(1, 365))
    }

    private fun showSnackBar() {
        val fab: ExtendedFloatingActionButton = requireActivity().findViewById(R.id.floatingActionButton)
        Snackbar.make(fab, getText(R.string.on_task_added), Snackbar.LENGTH_SHORT)
            .setAnchorView(fab)
            .setBackgroundTint(requireActivity().getColor(R.color.colorSecondary))
            .setTextColor(requireActivity().getColor(R.color.colorOnSecondary))
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

