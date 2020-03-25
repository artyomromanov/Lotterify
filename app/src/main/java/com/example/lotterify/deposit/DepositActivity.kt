package com.example.lotterify.deposit

import android.content.Context
import android.os.Bundle
import android.text.TextUtils.indexOf
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.lotterify.R
import kotlinx.android.synthetic.main.activity_deposit.*
import kotlinx.coroutines.delay


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
                if (amount.indexOf('.') == 0 || count > 1 || amount.length - amount.indexOf('.') > 3 ) stopEntry()
            }
        }

        btn_pay_p.setOnClickListener {


        }
    }

    private fun stopEntry() {
        with(et_deposit_amount) {
            setText(et_deposit_amount.text.dropLast(1))
            setSelection(et_deposit_amount.text.length);
        }
    }

    private fun hideSoftKeyboard(fragment: Fragment) {

        val view = fragment.activity?.currentFocus
        if (view != null) {
            val inputManager: InputMethodManager = fragment.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
