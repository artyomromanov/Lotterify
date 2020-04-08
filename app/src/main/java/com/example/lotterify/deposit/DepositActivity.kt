package com.example.lotterify.deposit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.lotterify.R
import com.example.lotterify.main.view.MainActivity
import com.example.lotterify.util.SoftKeyboardState
import kotlinx.android.synthetic.main.activity_deposit.*


class DepositActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)

        title = "Deposit"

        et_deposit_amount.addTextChangedListener {
            val amount = it.toString()
            var count = 0
            for (i in amount) {
                if (i == '.') {
                    count++
                }
            }
            if (amount.contains('.')) {
                if (amount.indexOf('.') == 0 || count > 1 || amount.length - amount.indexOf('.') > 3 ) {
                    stopEntry()
                }
                if(amount.length - amount.indexOf('.') > 2 ){
                    SoftKeyboardState.HIDE(this)
                }else{
                    SoftKeyboardState.SHOW(this)
                }
            }
        }

        btn_pay_p.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    private fun stopEntry() {
        with(et_deposit_amount) {
            setText(et_deposit_amount.text.dropLast(1))
            setSelection(et_deposit_amount.text.length);
        }
    }
}
