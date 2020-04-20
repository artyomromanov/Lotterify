package com.example.lotterify.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lotterify.R
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.MainViewModelFactory
import com.example.lotterify.util.SoftKeyboardState
import kotlinx.android.synthetic.main.deposit_fragment.*

class DepositFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.deposit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(this, MainViewModelFactory(context!!)).get(MainViewModel::class.java)

        //model.findUser()

        et_deposit_amount.addTextChangedListener {
            val amount = it.toString()
            var count = 0
            for (i in amount) {
                if (i == '.') {
                    count++
                }
            }
            if (amount.contains('.')) {
                if (amount.indexOf('.') == 0 || count > 1 || amount.length - amount.indexOf('.') > 3) {
                    stopEntry()
                }
                if (amount.length - amount.indexOf('.') > 2) {
                    SoftKeyboardState.HIDE(activity!!.parent)
                } else {
                    SoftKeyboardState.SHOW(activity!!.parent)
                }
            }
        }
    }

    private fun stopEntry() {
        with(et_deposit_amount) {
            setText(et_deposit_amount.text.dropLast(1))
            setSelection(et_deposit_amount.text.length);
        }
    }
}
