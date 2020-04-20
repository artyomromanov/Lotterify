package com.example.lotterify.main.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lotterify.R
import com.example.lotterify.deposit.DepositFragment
import com.example.lotterify.draws.DrawsFragment
import com.example.lotterify.login.LoginFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_activity2.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var signedInEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity2)

        supportActionBar?.hide()

        //val model = ViewModelProvider(this, MainViewModelFactory(applicationContext)).get(MainViewModel::class.java)

        var currentlySelectedId = bottom_navigation.selectedItemId

       bottom_navigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.id_account -> {
                    if (currentlySelectedId != R.id.id_account) {
                        createFragment(LoginFragment())
                        currentlySelectedId = R.id.id_account
                    }
                }
                R.id.id_draws -> {
                    if (currentlySelectedId != R.id.id_draws) {
                        createFragment(DrawsFragment())
                        currentlySelectedId = R.id.id_draws
                    }
                }
                R.id.id_deposit -> {
                    if (currentlySelectedId != R.id.id_deposit) {
                        createFragment(DepositFragment())
                        currentlySelectedId = R.id.id_deposit
                    }
                }
                else -> throw IllegalArgumentException()
            }
            true
        }

        createFragment(LoginFragment())

    }

    private fun createFragment(fragment: Fragment) {

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment).commit()

    }
}