package com.sloupycom.shaper.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.sloupycom.shaper.Controller.FirebaseAuthController
import com.sloupycom.shaper.R


class LoginActivity : AppCompatActivity() {

    val RC_SIGN_IN = 10

    lateinit var mFirebaseAuthController: FirebaseAuthController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        mFirebaseAuthController = FirebaseAuthController(this)
        Login()
    }

    fun onClick(view: View) {
        if (view.id == R.id.button_login) Login()
    }

    private fun Login() {
        val mAccount = mFirebaseAuthController.Authenticate()
        if (mAccount==null) {
            val signInIntent: Intent = mFirebaseAuthController.mGSC.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        else UpdateUi(mAccount)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            UpdateUi(mFirebaseAuthController.HandleSignInResult(data))
        }
    }

    private fun UpdateUi(account: GoogleSignInAccount?) {
        if (account!=null){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("intent", account)
            startActivity(intent)
            finish()
        }
    }
}