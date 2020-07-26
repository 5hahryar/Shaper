package com.sloupycom.shaper.Controller

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sloupycom.shaper.R
import kotlinx.coroutines.runBlocking

class MyAuthController(private val context:Activity, val listener:OnAuthCompleteListener) {

    private var mGSO: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestProfile()
        .requestEmail()
        .build()
    var mGSC: GoogleSignInClient
    private var mGSA: GoogleSignInAccount?
    private var mAuth: FirebaseAuth

    init {
        mGSC = GoogleSignIn.getClient(context, mGSO)
        mGSA = GoogleSignIn.getLastSignedInAccount(context)
        mAuth = FirebaseAuth.getInstance()
        signInExistingAccount()
    }

    private fun signInExistingAccount() {
        if (mGSA!=null) {
            fetchFirebaseUser(mGSA)
        }
        else listener.onAuthFailed("NO ACCOUNT FOUND")
    }

    private fun signupNewAccount() {
        val signInIntent: Intent = mGSC.signInIntent
        context.startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
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

    fun signIn() {
        signupNewAccount()
    }

    interface OnAuthCompleteListener {
        fun onAuthSuccessful(account: FirebaseUser)
        fun onAuthFailed(error: String)
    }
}