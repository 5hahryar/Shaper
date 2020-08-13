package com.sloupycom.shaper.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.ViewModel.Constants
import com.sloupycom.shaper.ViewModel.MyAuthController
import com.sloupycom.shaper.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), MyAuthController.OnAuthCompleteListener {

    lateinit var mMyAuthController: MyAuthController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loading_layout.visibility = View.VISIBLE
        mMyAuthController = MyAuthController(this, this)
    }

    fun onClick(view: View) {
        if (view.id == R.id.button_login) mMyAuthController.signIn()
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.RC_SIGN_IN){
            mMyAuthController.onSignUpIntentResult(data)
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
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}