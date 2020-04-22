package com.example.lotterify.frragmentLogin

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lotterify.R
import com.example.lotterify.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.please_sign_in_fragment.*

class PleaseSignInFragment(private val model : MainViewModel) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.please_sign_in_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_please_sign_in.setTextColor(Color.RED)

    }

}