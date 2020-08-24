package com.sloupycom.shaper.viewModel

import android.net.Uri
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.view.SettingsBottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_settings.*
import kotlinx.android.synthetic.main.bottom_sheet_settings.view.*

class SettingsViewModel(bottomSheet: SettingsBottomSheet): ViewModel() {

    private val mRepo: Repo = Repo()
    private val mUser: FirebaseUser? = mRepo.getUserCredentials()
    var name: String? = mUser?.displayName
    var email: String? = mUser?.email
    var profileImage: Uri? = mUser?.photoUrl

    init {
        name = mRepo.getUserCredentials()?.displayName
        Glide.with(bottomSheet)
            .load(profileImage)
            .placeholder(R.drawable.ic_account_circle_24px)
            .into(bottomSheet.binding?.root?.imageView_profile!!)
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.switchNightMode -> {
                if ((view as SwitchMaterial).isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

}