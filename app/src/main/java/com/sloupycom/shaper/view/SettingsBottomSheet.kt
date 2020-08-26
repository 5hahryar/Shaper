package com.sloupycom.shaper.view

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.BottomsheetSettingsBinding
import com.sloupycom.shaper.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.bottomsheet_settings.*

class SettingsBottomSheet: BottomSheetDialogFragment(), PopupMenu.OnMenuItemClickListener {

    private var mBinding: BottomsheetSettingsBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
            = BottomSheetDialog(requireContext(), theme)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_settings, container, false)
        mBinding?.viewModel = SettingsViewModel(activity!!.application)

        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        textView_nightMode.setOnClickListener{
            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_night_mode, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener(this)
        }
        logoutButton.setOnClickListener {
            //LOGOUT
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.night_auto -> {
                mBinding?.viewModel?.setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            R.id.night_on -> {
                mBinding?.viewModel?.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            R.id.night_off -> {
                mBinding?.viewModel?.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> return false
        }
        return true
    }

}