package com.example.lotterify.frragmentLogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.lotterify.R
import com.example.lotterify.RC_SIGN_IN
import com.example.lotterify.main.model.UserDataState
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.UIState
import com.example.lotterify.util.toast
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment(private val model: MainViewModel) : Fragment() {


    //Google sign in stuff
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var tvGoogleButtonText: TextView
    private lateinit var callbackManager: CallbackManager

    private var googleAccount: GoogleSignInAccount? = null

    //Boolean showing if signed in with anything
    private var signedIn = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.login_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///Starts with a clean slate
        model.removeAllUsersTESTONLY()

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
                is UserDataState.Loading -> {
                    showProgress()
                }
                is UserDataState.NotFound -> {
                    showProgress(false)
                    model.setUIState(UIState.ACCOUNT_CREATION_USERNAME)
                }
                is UserDataState.Existing -> {
                    showProgress(false)
                    model.setUIState(UIState.OAUTH_SIGNED_IN)
                    context?.toast("Welcome old user ${it.user.username}!")
                }
                is UserDataState.New -> {
                    showProgress(false)
                    model.setUIState(UIState.OAUTH_SIGNED_IN)
                    model.signedInLotterifyUser = it.user
                }
                is UserDataState.Deleted -> {
                    showProgress(false)
                    context?.toast(it.message)
                }
                is UserDataState.Error -> {
                    showProgress(false)
                    context?.toast("${it.error.message}")
                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        if (googleAccount != null)
            signedIn = true
        updateUI(googleAccount)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Google
        if (requestCode == RC_SIGN_IN) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {

            //Save the signed in user into the VM
            model.signedInOAuthUser = account.email

            //Continue to check the DB for the users credentials
            googleAccount?.email?.let { model.findUser(it) } ?: context?.toast("No email associated with this account")

            //UI Changes
            val text = getString(R.string.txt_signed_in_user) + account.displayName
            tv_status.text = text
            Picasso.get().load(googleAccount?.photoUrl).into(iv_avatar)
            tvGoogleButtonText.text = getString(R.string.txt_sign_out)

        } else {
            //State change
            model.setUIState(UIState.OAUTH_NOT_SIGNED_IN)

            //UI Changes
            tv_status.text = getString(R.string.txt_not_signed_in)
            iv_avatar.setImageResource(R.drawable.not_signed_in)
            tvGoogleButtonText.text = getString(R.string.txt_sign_in)
        }
    }

    private fun googleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun googleSignOut() {
        googleSignInClient.signOut()
            .addOnCompleteListener {

                context?.toast(getString(R.string.txt_signed_out_user) + googleAccount?.displayName)

                googleAccount = null
                updateUI(null)

            }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try {

            googleAccount = completedTask.getResult(ApiException::class.java)
            updateUI(googleAccount)
            signedIn = true

        } catch (e: ApiException) {

            tv_status.text = e.statusCode.toString()
            updateUI(null)
            signedIn = false

        }
    }

    private fun showProgress(show: Boolean = true) {
        with(pb_login_progress) { visibility = if (show) View.VISIBLE else View.INVISIBLE }
    }
}