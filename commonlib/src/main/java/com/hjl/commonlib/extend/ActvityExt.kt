package com.hjl.commonlib.extend

import android.app.Activity
import android.content.Intent

import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/**
 * author: long
 * description please add a description here
 * Date: 2021/4/21
 */



fun Activity.hideKeyboard() {
    inputMethodManager?.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    currentFocus?.clearFocus()
}

fun Activity.showKeyboard(view: View) {
    view.requestFocus()
    inputMethodManager?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideKeyboard(view: View) {
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.quickStartActivity( cls: Class<*>?){
    startActivity(Intent(this,cls))
}

fun Fragment.quickStartActivity(cls: Class<*>?){
    startActivity(Intent(context,cls))
}

