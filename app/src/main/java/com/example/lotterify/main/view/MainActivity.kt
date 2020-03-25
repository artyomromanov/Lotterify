package com.example.lotterify.main.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lotterify.deposit.DepositActivity
import com.example.lotterify.R
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val model = ViewModelProvider(this, MainViewModelFactory(applicationContext)).get(MainViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)

        rv_draws.layoutManager = layoutManager

        btnParseHTML.setOnClickListener {
                model.fetchNumbers()
        }

        model.getResultsData().observe(this, Observer {

            rv_draws.adapter = DrawsAdapter(it)

            //textView!!.text = it.toString()
        })

        btn_deposit.setOnClickListener {
            val intent = Intent(this@MainActivity, DepositActivity::class.java)
            startActivity(intent)

        }
    }
}