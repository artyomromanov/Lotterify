package com.example.lotterify.frragmentLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.lotterify.R
import com.example.lotterify.database.User
import com.example.lotterify.main.model.UserDataState
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.UIState
import kotlinx.android.synthetic.main.create_account_currency_fragment.*
import kotlinx.android.synthetic.main.lotterify_account_signed_in.*

class CreateAccountCurrencyFragment(private val model: MainViewModel) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.create_account_currency_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_create_account_currency_finish.setOnClickListener {
            model.setUIState(UIState.ACCOUNT_CREATION_CURRENCY)
        }

        var selectedCurrency = ""

        rg_currency.setOnCheckedChangeListener { _, checkedId ->

            when(checkedId){

                R.id.currency_eur ->    selectedCurrency = getString(R.string.eur)
                R.id.currency_gbp ->    selectedCurrency = getString(R.string.gbp)
                R.id.currency_usd ->    selectedCurrency = getString(R.string.usd)

            }
        }

        btn_create_account_currency_finish.setOnClickListener {

            if(model.signedInOAuthUser != null){
                val newUser = User(model.signedInOAuthUser!!, 0.00, selectedCurrency)
                model.addUser(newUser)
            }
        }
    }
   }
