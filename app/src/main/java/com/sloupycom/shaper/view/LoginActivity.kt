package com.sloupycom.shaper.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseUser
import com.sloupycom.shaper.utils.Constants
import com.sloupycom.shaper.viewmodel.LoginActivityViewModel
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginActivityViewModel.OnAuthCompleteListener {

    /** Values **/
    private var mBinding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding?.loadingLayout?.visibility = View.VISIBLE
        mBinding?.viewModel = LoginActivityViewModel(application, this)
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.button_login -> signupNewAccount()
        }
    }

    private fun signupNewAccount() {
        val signInIntent: Intent = mBinding?.viewModel?.mGSC?.signInIntent!!
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.RC_SIGN_IN){
            mBinding?.viewModel?.onSignUpIntentResult(data)
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