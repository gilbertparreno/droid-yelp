package com.gp.yelp.screen.base

import androidx.fragment.app.Fragment
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater



abstract class BaseFragment : Fragment() {
    abstract fun onToolbarClicked(buttonId: Int)
    abstract fun setUpToolBar()
}