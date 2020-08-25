package com.sloupycom.shaper.viewModel

import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.Application
import com.sloupycom.shaper.R
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.utils.General
import de.hdodenhof.circleimageview.CircleImageView


class SettingsViewModel(application: android.app.Application): AndroidViewModel(application), PopupMenu.OnMenuItemClickListener {

    private val context = getApplication<android.app.Application>().applicationContext
    private val mRepo: Repo = Repo()
    private val mUser: FirebaseUser? = mRepo.getUserCredentials()
    var imageUri: String? = mUser?.photoUrl.toString()
    private val mGeneral = General()
    var name: String? = mUser?.displayName
    var email: String? = mUser?.email
    var nightMode: String? = "k"

    init {
        name = mRepo.getUserCredentials()?.displayName
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.textView_nightMode -> {
                val popup = PopupMenu(context, view)
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

@BindingAdapter("loadImage")
fun loadImage(imageView: CircleImageView, imageUrl: String?) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_account_circle_24px)
        .into(imageView)
}