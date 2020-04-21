package com.example.lotterify.frragmentLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lotterify.R
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.UIState
import kotlinx.android.synthetic.main.create_account_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*

class CreateAccountFragment(private val model : MainViewModel) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.create_account_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_create_account_status.text = "You are now signed in as ${model.signedInUser}"

        with(btn_create_account_sign_out) {

            val tvGoogleButtonText = getChildAt(0) as TextView
            tvGoogleButtonText.text = getString(R.string.txt_sign_out)

            setOnClickListener {
                model.setUIState(UIState.SIGN_IN)
                model.signedInUser = "cmd:SignedOut"
            }
        }

    }
}