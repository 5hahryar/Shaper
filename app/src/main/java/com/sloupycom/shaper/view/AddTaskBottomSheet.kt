package com.sloupycom.shaper.view

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.sloupycom.shaper.viewmodel.AddTaskViewModel
import kotlinx.android.synthetic.main.bottomsheet_add_task.*

class AddTaskBottomSheet : BottomSheetDialogFragment() {

    /**Values**/
    private var mBinding: BottomsheetAddTaskBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AddTaskBottomSheet)
        return super.onCreateDialog(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_task, container, false)
        mBinding?.viewModel = AddTaskViewModel(activity!!)

        return mBinding?.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding?.viewModel?.setOnTaskAddedListener(object: AddTaskViewModel.OnTaskAddedListener{
            override fun onTaskAdded() {
                dismiss()
                showSnackBar()
            }
        })

        mBinding?.viewModel?.textError?.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                editTextLayout_title.error = (sender as ObservableField<String>).get()
            }
        })
    }

    private fun showSnackBar() {
        val fab: ExtendedFloatingActionButton = activity!!.findViewById(R.id.floatingActionButton)
        Snackbar.make(fab, getText(R.string.on_task_added), Snackbar.LENGTH_SHORT)
            .setAnchorView(fab)
            .setBackgroundTint(activity!!.getColor(R.color.colorSecondary))
            .setTextColor(activity!!.getColor(R.color.colorOnSecondary))
            .show()
    }

}