package com.sloupycom.shaper.Controller

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class FirebaseAuthController(val context:Activity) {
    private lateinit var mGSO: GoogleSignInOptions
    var mGSC: GoogleSignInClient
    private var mGSA: GoogleSignInAccount?


    init {
        mGSO =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGSC = GoogleSignIn.getClient(context, mGSO)
        mGSA = GoogleSignIn.getLastSignedInAccount(context)
        Authenticate()
    }

    fun Authenticate(): GoogleSignInAccount?{
        return if (mGSA!=null) mGSA as GoogleSignInAccount
        else null
    }

    fun HandleSignInResult(data: Intent?): GoogleSignInAccount? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        mGSA = task.result
        return mGSA
    }
}