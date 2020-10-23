package com.sloupycom.shaper.view

import android.app.*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.BottomsheetSettingsBinding
import com.sloupycom.shaper.viewmodel.SettingsViewModel

class SettingsBottomSheet : BottomSheetDialogFragment() {

    /**Values**/
    private var mBinding: BottomsheetSettingsBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.bottomsheet_settings, container, false)
        mBinding?.viewModel = SettingsViewModel(activity!!)

        return mBinding?.root
    }

}