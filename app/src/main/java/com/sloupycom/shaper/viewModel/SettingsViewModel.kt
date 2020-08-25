package com.sloupycom.shaper.viewModel

import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.Application
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.utils.General
import com.sloupycom.shaper.view.SettingsBottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_settings.view.*
import javax.inject.Inject

class SettingsViewModel(private val bottomSheet: SettingsBottomSheet): ViewModel(),
    PopupMenu.OnMenuItemClickListener {

    private val mRepo: Repo = Repo()
    var application = Application()
    private val mUser: FirebaseUser? = mRepo.getUserCredentials()
    private var profileImage: Uri? = mUser?.photoUrl
    private val mGeneral = General()
    var name: String? = mUser?.displayName
    var email: String? = mUser?.email
    var nightMode: String? = application.getNightMode()

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
            R.id.night_auto -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                mGeneral.writePreference("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            R.id.night_on -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mGeneral.writePreference("night_mode", AppCompatDelegate.MODE_NIGHT_YES)
            }
            R.id.night_off -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mGeneral.writePreference("night_mode", AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> return false
        }
        return true
    }

}