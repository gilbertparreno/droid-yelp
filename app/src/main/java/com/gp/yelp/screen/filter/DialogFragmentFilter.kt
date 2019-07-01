package com.gp.yelp.screen.filter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.gp.yelp.R
import com.gp.yelp.app.App
import kotlinx.android.synthetic.main.dialog_fragment_filter.*
import javax.inject.Inject


class DialogFragmentFilter : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FilterViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as Activity

        let {
            DaggerFilterComponent.builder()
                    .appComponent((activity.application as App).appComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[FilterViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)

        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT
                val wlp = window.attributes
                wlp.gravity = Gravity.BOTTOM
                window.attributes = wlp
                window.setBackgroundDrawableResource(android.R.color.transparent)
                window.setLayout(width, height)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_filter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sbDistance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                tvRadiusValue.text = resources.getString(R.string.lbl_seekbar_radius, value)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        cbOpenNow.setOnCheckedChangeListener { _, isChecked ->
            tvOpenNowValue.text = if (isChecked) "Open Only" else "All"
        }

        btnApply.setOnClickListener {

            dismiss()
        }
    }

    companion object {
        val TAG = DialogFragmentFilter.javaClass.simpleName
    }
}