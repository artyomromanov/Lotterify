package com.example.lotterify.frragmentLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lotterify.R
import com.example.lotterify.RC_SIGN_IN
import com.example.lotterify.di.components.DaggerViewModelComponent
import com.example.lotterify.main.model.UserDataState
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.MainViewModelFactory
import com.example.lotterify.util.LotterifyApplication
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class LoginFragment (private val model : MainViewModel) : Fragment() {

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

            model.signedInUser = account.email //save the signed in user into the VM
            tv_status.text = text
            Picasso.get().load(googleAccount?.photoUrl).into(iv_avatar)
            tvGoogleButtonText.text = getString(R.string.txt_sign_out)
            enableTabs(true)
        } else {
            tv_status.text = getString(R.string.txt_not_signed_in)
            tvGoogleButtonText.text = getString(R.string.txt_sign_in)
            iv_avatar.setImageResource(R.drawable.not_signed_in)
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
            for (item in it.children) {
                item.isEnabled = enable
            }
        }
    }
}