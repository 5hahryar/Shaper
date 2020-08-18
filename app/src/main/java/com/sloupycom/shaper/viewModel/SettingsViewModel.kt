package com.sloupycom.shaper.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.view.SettingsBottomSheet

class SettingsViewModel(bottomSheet: SettingsBottomSheet): ViewModel() {

    private val mRepo: Repo = Repo()
    private val mUser: FirebaseUser? = mRepo.getUserCredentials()
    var name: String? = mUser?.displayName
    var email: String? = mUser?.email
    var profileImage: Uri? = mUser?.photoUrl

    init {
        name = mRepo.getUserCredentials()?.displayName
    }

}