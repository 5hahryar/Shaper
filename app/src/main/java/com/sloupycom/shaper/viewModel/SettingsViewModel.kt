package com.sloupycom.shaper.viewModel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.view.MainActivity
import com.sloupycom.shaper.view.SettingsBottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_settings.*
import kotlinx.android.synthetic.main.bottom_sheet_settings.view.*

class SettingsViewModel(private val bottomSheet: SettingsBottomSheet): ViewModel(),
    PopupMenu.OnMenuItemClickListener {

    private val mRepo: Repo = Repo()
    private val mUser: FirebaseUser? = mRepo.getUserCredentials()
    var name: String? = mUser?.displayName
    var email: String? = mUser?.email
    var profileImage: Uri? = mUser?.photoUrl
    var nightMode: String = bottomSheet.activity?.getSharedPreferences("dark_mode", Context.MODE_PRIVATE).toString()

    init {
        name = mRepo.getUserCredentials()?.displayName
        Glide.with(bottomSheet)
            .load(profileImage)
            .placeholder(R.drawable.ic_account_circle_24px)
            .into(bottomSheet.binding?.root?.imageView_profile!!)
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.textView_nightMode -> {
                val popup = PopupMenu(bottomSheet.context, view)
                popup.menuInflater.inflate(R.menu.menu_night_mode, popup.menu)
                popup.show()
                popup.setOnMenuItemClickListener(this)
            }
            R.id.logoutButton -> {
                //LOGOUT USER
            }
        }

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.night_auto -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            R.id.night_on -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.night_off -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> return false
        }
        return true
    }

}