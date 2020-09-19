package com.sloupycom.shaper.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.R
import com.sloupycom.shaper.dagger.DaggerDependencyComponent
import com.sloupycom.shaper.model.Repo
import de.hdodenhof.circleimageview.CircleImageView


class SettingsViewModel(application: Application): AndroidViewModel(application) {

    /** Values **/
    private val mComponent = DaggerDependencyComponent.create()
    private val mRepo: Repo = mComponent.getRepo()
    private val mUtil = mComponent.getUtil()
    private val mUser: FirebaseUser? = mRepo.getUserCredentials()

    var imageUri: String? = mUser?.photoUrl.toString()
    var name: String? = mUser?.displayName
    var email: String? = mUser?.email
    var nightMode: ObservableField<String> = ObservableField(mUtil.getNightMode(application.applicationContext))

    init {
        name = mRepo.getUserCredentials()?.displayName
    }

    fun setNightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        mUtil.writePreference(getApplication(), "night_mode", mode)
    }
}

@BindingAdapter("loadImage")
fun loadImage(imageView: CircleImageView, imageUrl: String?) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_account_circle_24px)
        .into(imageView)
}