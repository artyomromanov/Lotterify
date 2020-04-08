package com.example.lotterify.main.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lotterify.R
import com.example.lotterify.deposit.DepositActivity
import com.example.lotterify.main.model.LoadingDrawsState
import com.example.lotterify.main.model.UserDataState
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.MainViewModelFactory
import com.example.lotterify.EMAIL_KEY
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var signedInEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val model = ViewModelProvider(this, MainViewModelFactory(applicationContext)).get(MainViewModel::class.java)
        val layoutManager = LinearLayoutManager(this)

        signedInEmail = intent?.getStringExtra(EMAIL_KEY) ?: "unknown user"
        tv_email.text = signedInEmail

        model.findUser(signedInEmail)

        rv_draws.layoutManager = layoutManager

        btnParseHTML.setOnClickListener {
            model.fetchNumbers()
        }

        model.getResultsData().observe(this, Observer {
            when (it) {
                is LoadingDrawsState.IN_PROGRESS -> showProgress(true)
                is LoadingDrawsState.SUCCESS -> {
                    rv_draws.adapter = DrawsAdapter(it.results); showProgress(false)
                }
                is LoadingDrawsState.ERROR -> {
                    tv_status.text = it.error.message; showProgress(false)
                }
            }
        })

        model.getUserData().observe(this, Observer {
            when (it) {
                is UserDataState.LOADING -> {
                }
                is UserDataState.NEW -> {
                    tv_email.text = "New user ${it.user.username}"
                    tv_email.setTextColor(Color.GREEN)
                }
                is UserDataState.ERROR -> {
                    tv_email.text = "Error ${it.error.message}"
                    tv_email.setTextColor(Color.GREEN)
                }
                is UserDataState.EXISTING -> {
                    tv_email.text = "Existing user ${it.user.username}"
                    tv_email.setTextColor(Color.BLUE)
                }
            }
        })

        btn_deposit.setOnClickListener {
            startActivity(Intent(this@MainActivity, DepositActivity::class.java))
        }
    }

    private fun showProgress(show: Boolean) {
        pb_progress.visibility = if (show) View.VISIBLE else View.GONE
        rv_draws.visibility = if (show) View.GONE else View.VISIBLE
    }
}