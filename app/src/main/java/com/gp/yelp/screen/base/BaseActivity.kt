package com.gp.yelp.screen.base

import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


abstract class BaseActivity : AppCompatActivity() {

    fun hideKeyboard() {
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
    }
}