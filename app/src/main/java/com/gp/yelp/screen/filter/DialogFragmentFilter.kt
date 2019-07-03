package com.gp.yelp.screen.filter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gp.yelp.R
import com.gp.yelp.app.App
import com.gp.yelp.network.model.Category
import com.gp.yelp.screen.filter.categoryList.CategoryListFragment
import com.gp.yelp.utils.BaseDialogFragment
import com.gp.yelp.utils.ItemOffsetDecoration
import com.gp.yelp.utils.SharedPreferenceUtil
import kotlinx.android.synthetic.main.dialog_fragment_filter.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class DialogFragmentFilter(private val filterListener: FilterListener) : BaseDialogFragment(),
    CategoryAdapter.CategoryListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FilterViewModel

    private var sortAlias = arrayOf<String>()
    private val rbSortIds = arrayOf(R.id.rbBestMatch, R.id.rbRating, R.id.rbReviewCount, R.id.rbDistance)
    private val adapter = CategoryAdapter(this)

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

    override fun addCategory() {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val prev = activity!!.supportFragmentManager.findFragmentByTag(CategoryListFragment.TAG)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        val dialogFragment = CategoryListFragment()

        dialogFragment.setTargetFragment(this, REQUEST_CATEGORY)
        dialogFragment.show(ft, CategoryListFragment.TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_filter, container, false)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[FilterViewModel::class.java]
        lifecycle.addObserver(viewModel)

        viewModel.liveDataRadius.observe(viewLifecycleOwner, Observer {
            sbRadius.progress = it / 1000
            tvRadiusValue.text = resources.getString(R.string.lbl_seekbar_radius, it / 1000)
        })

        viewModel.liveDataOpenNow.observe(viewLifecycleOwner, Observer {
            cbOpenNow.isChecked = it
            tvOpenNowValue.text = if (it) "Open Only" else "All"
        })

        viewModel.liveDataSortBy.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                rbBestMatch.isChecked = true
            } else {
                val index = sortAlias.indexOf(it)
                view?.findViewById<RadioButton>(rbSortIds[index])!!.let { rb ->
                    rb.isChecked = true
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortAlias = resources.getStringArray(R.array.sort_alias)
        initViewModel()

        toolbarClose.visibility = View.VISIBLE
        tvHeader.text = "Filters"
        btnApply.isEnabled = false
        sbRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                tvRadiusValue.text = resources.getString(R.string.lbl_seekbar_radius, value)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
                validateEnableButtonApply()
            }

        })

        cbOpenNow.setOnCheckedChangeListener { checkBox, isChecked ->
            tvOpenNowValue.text = if (isChecked) "Open Only" else "All"

            if (cbOpenNow.isPressed) {
                validateEnableButtonApply()
            }
        }

        btnApply.setOnClickListener {
            val radioButtonID = rgSortBy.checkedRadioButtonId
            val radioButton = view!!.findViewById<RadioButton>(radioButtonID)
            val rbIndex = rgSortBy.indexOfChild(radioButton)
            viewModel.saveSettings(SharedPreferenceUtil.Key.SORT_BY, sortAlias[rbIndex])
            viewModel.saveSettings(SharedPreferenceUtil.Key.OPEN_NOW, cbOpenNow.isChecked)
            viewModel.saveSettings(SharedPreferenceUtil.Key.RADIUS, sbRadius.progress * 1000)
            filterListener.applyFilter()
            dismiss()
        }

        rgSortBy.setOnCheckedChangeListener { _, i ->
            val radioButton = view!!.findViewById<RadioButton>(i)
            if (radioButton.isPressed) {
                validateEnableButtonApply()
            }
        }

        toolbarClose.setOnClickListener {
            dismiss()
        }

        val itemDecoration = ItemOffsetDecoration(context!!, R.dimen.mp_quarter)
        rvCategories.addItemDecoration(itemDecoration)
        rvCategories.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        rvCategories.adapter = adapter
        adapter.addItem(Category(alias = ""))
    }

    private fun validateEnableButtonApply() {
        btnApply.isEnabled = false
        val radiusIsSame = (sbRadius.progress * 1000) == viewModel.liveDataRadius.value
        val openNowIsSame = cbOpenNow.isChecked == viewModel.liveDataOpenNow.value

        val radioButtonID = rgSortBy.checkedRadioButtonId
        val radioButton = view!!.findViewById<RadioButton>(radioButtonID)
        val rbIndex = rgSortBy.indexOfChild(radioButton)

        val sortByIsSame = sortAlias[rbIndex] == viewModel.liveDataSortBy.value

        if (radiusIsSame && openNowIsSame && sortByIsSame) {
            return
        }

        btnApply.isEnabled = true
    }

    companion object {
        val TAG = "DialogFragmentFilter"
        val EXTRA_CATEGORIES = "categories"
        val REQUEST_CATEGORY = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val data = data!!.getParcelableArrayListExtra<Category>(EXTRA_CATEGORIES)
        adapter.addItems(data)
    }
}