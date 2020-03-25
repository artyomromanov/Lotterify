package com.example.lotterify.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lotterify.R
import com.example.lotterify.util.RC_SIGN_IN
import com.example.lotterify.main.view.MainActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var tvGoogleButtonText : TextView
    private lateinit var callbackManager : CallbackManager

    private var googleAccount : GoogleSignInAccount? = null
    private var signedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        callbackManager = CallbackManager.Factory.create()

        btn_fb_sign_in.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                val token = loginResult?.accessToken
                tv_status.text = token?.userId
            }
            override fun onCancel() {
                tv_status.text = getString(R.string.txt_cancelled_login)
            }
            override fun onError(exception: FacebookException) {
                tv_status.text = exception.message
            }
        })

        btn_proceed.setOnClickListener {
            if(signedIn){
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this@LoginActivity, getString(R.string.txt_please_sign_in), Toast.LENGTH_SHORT).show()
            }
        }



        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        googleAccount = GoogleSignIn.getLastSignedInAccount(this)

        with(btn_google_sign_in) {

            tvGoogleButtonText = getChildAt(0) as TextView

            setOnClickListener {
                if(!signedIn){
                    googleSignIn()
                } else {
                    googleSignOut()
                    signedIn = false
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        signedIn = googleAccount != null
        updateUI(googleAccount)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //Facebook
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        //Google
        if (requestCode == RC_SIGN_IN) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
            if  (account != null) {
                val text = getString(R.string.txt_signed_in_user) + account.displayName
                tv_status.text = text
                iv_avatar.visibility = View.VISIBLE
                Picasso.get().load(googleAccount?.photoUrl).into(iv_avatar)
                tvGoogleButtonText.text = getString(R.string.txt_sign_out)
            } else {
                tv_status.text = getString(R.string.txt_not_signed_in)
                iv_avatar.visibility = View.GONE
                tvGoogleButtonText.text = getString(R.string.txt_sign_in)
            }
    }

    private fun googleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    private fun googleSignOut() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this) {
                Toast.makeText(this@LoginActivity, getString(R.string.txt_signed_out_user) + googleAccount?.displayName, Toast.LENGTH_SHORT).show()
                googleAccount = null
                updateUI(googleAccount)
            }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            googleAccount = completedTask.getResult(ApiException::class.java)
            updateUI(googleAccount)
            signedIn = true

        } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            tv_status.text = e.statusCode.toString()
            updateUI(null)
            signedIn = false
        }
    }
}
