package com.sloupycom.shaper.utils

import android.app.Application
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

class AuthHelper(application: Application) {

    /**Values**/
    private val mContext = application.applicationContext
    private var mGSC: GoogleSignInClient
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
    }

    /**
     * Sign-in with existing account
     */
    fun signInExistingAccount(listener: OnAuthCompleteListener) {
        if (mAuth.currentUser != null) {
            listener.onAuthSuccessful(mAuth.currentUser!!)
        } else listener.onAuthFailed("NO ACCOUNT FOUND")
    }

    /**
     * Get firebase credentials of an account
     */
    fun fetchFirebaseUser(googleAccount: GoogleSignInAccount?, listener: OnAuthCompleteListener) {
        runBlocking {
            if (googleAccount != null) {
                mAuth.signInWithCredential(
                    GoogleAuthProvider.getCredential(googleAccount.idToken, null)
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            listener.onAuthSuccessful(mAuth.currentUser!!)
                        } else listener.onAuthFailed(task.result.toString())
                    }
            }
        }
    }

    fun signOut() {
        runBlocking {
            mAuth.signOut()
            mGSC.signOut()
        }
    }

    fun getSignIntent(): Intent {
        return mGSC.signInIntent
    }

    interface OnAuthCompleteListener {
        fun onAuthSuccessful(account: FirebaseUser)
        fun onAuthFailed(error: String)
    }
}