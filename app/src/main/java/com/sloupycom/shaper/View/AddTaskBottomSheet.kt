package com.sloupycom.shaper.View

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.Controller.General
import com.sloupycom.shaper.Model.Repo
import com.sloupycom.shaper.R
import kotlinx.android.synthetic.main.bottom_sheet_add_task.*
import kotlinx.android.synthetic.main.bottom_sheet_add_task.view.*

class AddTaskBottomSheet: BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
            = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_task, container, false)

        setupUi()
        setListeners()

        return view
    }

    private fun setListeners() {
        view?.button_add?.setOnClickListener {
            val task = hashMapOf(
                "creation_date" to textView_date.text.toString(),
                "description" to editText_desc.text.toString(),
                "name" to editText_name.text.toString(),
                "next_due" to "null",
                "owner_id" to "null",
                "reminder" to "null",
                "repetition" to "null",
                "state" to "ONGOING"
            )
            Repo().addTask(task)
            dismiss()
        }
    }

    private fun setupUi() {
        view?.textView_date?.text = General().getDate("EEEE, MMM dd")
    }
}