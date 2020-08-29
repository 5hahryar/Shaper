package com.sloupycom.shaper.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.utils.Constants
import com.sloupycom.shaper.R
import com.sloupycom.shaper.utils.AuthHelper
import com.sloupycom.shaper.utils.Util
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), AuthHelper.OnAuthCompleteListener {

    /** Values **/
    private lateinit var mAuthHelper: AuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppCompatDelegate.setDefaultNightMode(Util(application).getNightModeCon())
        mAuthHelper = AuthHelper(application)
        mAuthHelper.signInExistingAccount(this)
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.button_login -> startActivityForResult(mAuthHelper.getSignIntent(),
                Constants.RC_SIGN_IN)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.RC_SIGN_IN && resultCode == RESULT_OK){
            mAuthHelper.fetchFirebaseUser(GoogleSignIn.getSignedInAccountFromIntent(data).result, this)
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
}