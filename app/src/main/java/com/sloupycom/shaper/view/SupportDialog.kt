package com.sloupycom.shaper.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.DialogFragment
import com.sloupycom.shaper.R
import kotlinx.android.synthetic.main.dialog_support.view.*

class SupportDialog : DialogFragment() {

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_support, null)
            view.textView_repo.movementMethod = LinkMovementMethod.getInstance()
            view.textView_email.movementMethod = LinkMovementMethod.getInstance()
            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}