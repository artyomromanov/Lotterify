package com.example.lotterify.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object SoftKeyboardState {
    fun HIDE(activity: Activity){
        val view = activity.currentFocus
        val inputManager: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
    fun SHOW(activity: Activity){
        val view = activity.currentFocus
        val inputManager: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(view, 0)

    }
}