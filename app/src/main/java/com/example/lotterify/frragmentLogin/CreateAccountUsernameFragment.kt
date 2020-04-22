package com.example.lotterify.frragmentLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lotterify.R
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.UIState
import kotlinx.android.synthetic.main.create_account_username_fragment.*

class CreateAccountUsernameFragment(private val model : MainViewModel) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.create_account_username_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_create_account_status.text = getString(R.string.txt_status, model.signedInOAuthUser)

        btn_create_account_username_continue.setOnClickListener {
            model.setUIState(UIState.ACCOUNT_CREATION_CURRENCY)

        }

    }
}