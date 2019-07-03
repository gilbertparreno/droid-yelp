package com.gp.yelp.utils

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.gp.yelp.R

abstract class BaseDialogFragment : DialogFragment() {

    override fun onStart() {
        super.onStart()
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)

        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = height
                val wlp = window.attributes
                wlp.gravity = Gravity.BOTTOM
                window.attributes = wlp
                window.setBackgroundDrawableResource(android.R.color.transparent)
                window.setLayout(width, height)
            }
        }
    }

    protected var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
}