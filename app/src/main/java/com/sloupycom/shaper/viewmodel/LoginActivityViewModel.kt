package com.sloupycom.shaper.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sloupycom.shaper.R
import kotlinx.coroutines.runBlocking

class LoginActivityViewModel(application: Application, private val listener:OnAuthCompleteListener): AndroidViewModel(application) {

    private val mContext = getApplication<Application>().applicationContext
    var mGSC: GoogleSignInClient
    private var mGSA: GoogleSignInAccount?
    private var mAuth: FirebaseAuth
    private var mGSO: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(mContext.getString(R.string.default_web_client_id))
        .requestProfile()
        .requestEmail()
        .build()

    init {
        mGSC = GoogleSignIn.getClient(mContext, mGSO)
        mGSA = GoogleSignIn.getLastSignedInAccount(mContext)
        mAuth = FirebaseAuth.getInstance()
        signInExistingAccount()
    }

    private fun signInExistingAccount() {
        if (mAuth.currentUser!=null) {
            listener.onAuthSuccessful(mAuth.currentUser!!)
        }
        else listener.onAuthFailed("NO ACCOUNT FOUND")
    }



    private fun fetchFirebaseUser(googleAccount: GoogleSignInAccount?) {
        runBlocking {
            if (googleAccount != null) {
                mAuth.signInWithCredential(
                    GoogleAuthProvider.getCredential(googleAccount.idToken, null))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            listener.onAuthSuccessful(mAuth.currentUser!!)
                        } else listener.onAuthFailed(task.result.toString())
                    }
            }
        }
    }

    fun onSignUpIntentResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        mGSA = task.result
        fetchFirebaseUser(mGSA)
    }

    interface OnAuthCompleteListener {
        fun onAuthSuccessful(account: FirebaseUser)
        fun onAuthFailed(error: String)
    }
}