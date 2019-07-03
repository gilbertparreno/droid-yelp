package com.gp.yelp.screen.filter.categoryList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gp.yelp.R
import com.gp.yelp.network.model.Category
import com.gp.yelp.screen.filter.DialogFragmentFilter
import com.gp.yelp.screen.filter.DialogFragmentFilter.Companion.REQUEST_CATEGORY
import com.gp.yelp.utils.BaseDialogFragment
import com.gp.yelp.utils.Utils
import kotlinx.android.synthetic.main.dialog_fragment_category.*
import kotlinx.android.synthetic.main.toolbar.*


class CategoryListFragment : BaseDialogFragment() {

    private var list = mutableListOf<Category>()
    private val adapter = CategoryListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.height = ViewGroup.LayoutParams.MATCH_PARENT

        val listType = object : TypeToken<ArrayList<Category>>() {}.type
        list.addAll(Gson().fromJson<List<Category>>(Utils.getAssetsJSON(context!!, "json/categories.json"), listType))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarClose.visibility = View.VISIBLE
        toolbarClose.setOnClickListener {
            dismiss()
        }
        tvHeader.text = getString(R.string.lbl_add_category)
        rvCategories.layoutManager = LinearLayoutManager(context)
        rvCategories.adapter = adapter

        adapter.addItems(list)

        etSearchCategory.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.searchCategory(s.toString())
            }
        })

        btnDone.setOnClickListener {
            val intent = Intent()
            intent.putParcelableArrayListExtra(DialogFragmentFilter.EXTRA_CATEGORIES, ArrayList(adapter.selectedCategories))

            val fragment = targetFragment
            fragment!!.onActivityResult(REQUEST_CATEGORY, Activity.RESULT_OK, intent)
            dismiss()
        }
    }

    companion object {
        val TAG = "CategoryListFragment"
    }
}