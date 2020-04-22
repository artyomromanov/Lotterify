package com.example.lotterify.frragmentLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lotterify.R
import com.example.lotterify.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.lotterify_account_signed_in.*

class LotAccountSignedInFragment(private val model: MainViewModel) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.lotterify_account_signed_in, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_lot_account_status.text = getString(R.string.txt_signed_in_account, model.signedInOAuthUser)
        tv_login_balance.text = getString(R.string.txt_balance, model.signedInLotterifyUser?.balance.toString(), model.signedInLotterifyUser?.currency)

    }
}