package com.example.lotterify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.lotterify.di.components.DaggerViewModelComponent
import com.example.lotterify.di.modules.MainViewModelModule
import com.example.lotterify.fragmentDeposit.DepositFragment
import com.example.lotterify.fragmentDraws.DrawsFragment
import com.example.lotterify.frragmentLogin.*
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.UIState
import com.example.lotterify.util.LotterifyApplication
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var model: MainViewModel

    private val TAG = "MainActivity"

    private lateinit var signedInEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initializeViewModel()

        supportActionBar?.hide()

        var currentlySelectedId = bottom_navigation.selectedItemId

       bottom_navigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.id_account -> {
                    if (currentlySelectedId != R.id.id_account) {
                        createFragment(LoginFragment(model))
                        currentlySelectedId = R.id.id_account
                    }
                }
                R.id.id_draws -> {
                    if (currentlySelectedId != R.id.id_draws) {
                        createFragment(DrawsFragment(model))
                        currentlySelectedId = R.id.id_draws
                    }
                }
                R.id.id_deposit -> {
                    if (currentlySelectedId != R.id.id_deposit) {
                        createFragment(DepositFragment(model))
                        currentlySelectedId = R.id.id_deposit
                    }
                }
                else -> throw IllegalArgumentException()
            }
            true
        }

        createFragment(LoginFragment(model))

        //Set UI State
        model.getUIStateData().observe(this, Observer {
            when(it){
                UIState.ACCOUNT_CREATION_USERNAME -> { createLoginFragment(CreateAccountUsernameFragment(model)); enableTabs(false) }
                UIState.ACCOUNT_CREATION_CURRENCY -> { createLoginFragment(CreateAccountCurrencyFragment(model)); enableTabs(false) }
                UIState.OAUTH_NOT_SIGNED_IN -> { createLoginFragment(PleaseSignInFragment(model)); enableTabs(false) }
                UIState.OAUTH_SIGNED_IN -> { createLoginFragment(LotAccountSignedInFragment(model)); enableTabs() }
            }
        })

    }

    //Dagger injection
    private fun initializeViewModel() {
        DaggerViewModelComponent
            .builder()
            .appComponent((application as LotterifyApplication).applicationComponent)
            .mainViewModelModule(MainViewModelModule(this))
            .build()
            .injectActivity(this)
    }

    private fun createFragment(fragment: Fragment) {

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment).commit()

    }
    private fun createLoginFragment(fragment: Fragment) {

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.login_fragment_container, fragment).commit()

    }
    private fun enableTabs(enable: Boolean = true) {

       bottom_navigation?.menu?.let {
            for (item in it.children) {
                item.isEnabled = enable
            }
        }
    }

}