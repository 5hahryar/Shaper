package com.sloupycom.shaper.view

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.R
import com.sloupycom.shaper.utils.AuthHelper
import com.sloupycom.shaper.utils.Constant
import com.sloupycom.shaper.utils.Util
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), AuthHelper.OnAuthCompleteListener {

    /** Values **/
    private lateinit var mAuthHelper: AuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppCompatDelegate.setDefaultNightMode(Util().getNightModeConfig(applicationContext))
        mAuthHelper = AuthHelper(application)
        mAuthHelper.signInExistingAccount(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClick(view: View) {
        when (view.id) {
            R.id.button_login -> {
                createNotificationChannel()
                startActivityForResult(mAuthHelper.getSignIntent(), Constant.RC_SIGN_IN)
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.RC_SIGN_IN && resultCode == RESULT_OK) {
            mAuthHelper.fetchFirebaseUser(
                GoogleSignIn.getSignedInAccountFromIntent(data).result,
                this
            )
            loading_layout.visibility = View.VISIBLE
        }
    }

    override fun onAuthSuccessful(account: FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("intent", account)
        startActivity(intent)
        finish()
    }

    override fun onAuthFailed(error: String) {
        loading_layout.visibility = View.GONE
        Log.d("TAG", error)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name: CharSequence = "Reminder"
        val description = "Daily reminder of tasks"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Constant.ID_NOTIFICATION_CHANNEL, name, importance)
        channel.description = description
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager?.createNotificationChannel(channel)
    }
}