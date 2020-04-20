package com.example.lotterify.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lotterify.R
import com.example.lotterify.RC_SIGN_IN
import com.example.lotterify.main.model.UserDataState
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.MainViewModelFactory
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
import kotlinx.android.synthetic.main.main_activity2.*

class LoginFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var tvGoogleButtonText: TextView
    private lateinit var callbackManager: CallbackManager

    private var googleAccount: GoogleSignInAccount? = null
    private var signedIn = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.login_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callbackManager = CallbackManager.Factory.create()

        val model = ViewModelProvider(this, MainViewModelFactory(activity!!.applicationContext)).get(MainViewModel::class.java)

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
            if (signedIn) {
                googleAccount?.email?.let { model.findUser(it) }


            } else {
                Toast.makeText(context, getString(R.string.txt_please_sign_in), Toast.LENGTH_SHORT).show()
            }
        }

        model.getUserData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserDataState.ERROR -> {
                    Toast.makeText(context, "User not initialized, adding new user..", Toast.LENGTH_SHORT).show()
                    //googleAccount?.email?.let { model.addUser(it) }
                }
                is UserDataState.EXISTING -> {
                    Toast.makeText(context, "Welcome old user ${it.user.username}!", Toast.LENGTH_SHORT).show()
                    //startActivity(Intent(context, MainActivity2::class.java).apply { putExtra(EMAIL_KEY, it.user.username) })
                }
                is UserDataState.NEW -> {
                    Toast.makeText(context, "New user created successfully, welcome!", Toast.LENGTH_SHORT).show()
                    //startActivity(Intent(context, MainActivity2::class.java).apply { putExtra(EMAIL_KEY, it.user.username) })
                }
            }
        })

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context!!, googleSignInOptions)
        googleAccount = GoogleSignIn.getLastSignedInAccount(context)

        with(btn_google_sign_in) {

            tvGoogleButtonText = getChildAt(0) as TextView

            setOnClickListener {
                if (!signedIn) {
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
        if (account != null) {
            val text = getString(R.string.txt_signed_in_user) + account.displayName
            tv_status.text = text
            iv_avatar.visibility = View.VISIBLE
            Picasso.get().load(googleAccount?.photoUrl).into(iv_avatar)
            tvGoogleButtonText.text = getString(R.string.txt_sign_out)
            enableTabs(true)

        } else {

            tv_status.text = getString(R.string.txt_not_signed_in)
            iv_avatar.visibility = View.GONE
            tvGoogleButtonText.text = getString(R.string.txt_sign_in)
            enableTabs(false)

        }
    }

    private fun googleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun googleSignOut() {
        googleSignInClient.signOut()
            .addOnCompleteListener {
                Toast.makeText(activity, getString(R.string.txt_signed_out_user) + googleAccount?.displayName, Toast.LENGTH_SHORT).show()
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

    private fun enableTabs(enable: Boolean) {

        activity?.bottom_navigation?.menu?.let {
            for (item in it.children){
                item.isEnabled = enable
            }
        }
    }
}