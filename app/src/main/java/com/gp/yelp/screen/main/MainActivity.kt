package com.gp.yelp.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gp.yelp.R
import com.gp.yelp.screen.base.BaseActivity
import com.gp.yelp.screen.base.BaseFragment
import kotlinx.android.synthetic.main.toolbar.*
import timber.log.Timber

class MainActivity : BaseActivity(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.addToBackStack(BusinessListFragment.TAG)
            val fragment = BusinessListFragment()
            ft.add(R.id.fragmentContainer, fragment, BusinessListFragment.TAG).commit()
        } else {
            val backStackEntryCount = supportFragmentManager.backStackEntryCount
            if (backStackEntryCount == 0)
                return

            val backStackEntry = supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 1)
            val fragment = supportFragmentManager.findFragmentByTag(backStackEntry.name)
            if (fragment != null) {
                fragment as BaseFragment
                fragment.setUpToolBar()
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            hideKeyboard()
            showToolbar()
            getFragmentForeground()?.setUpToolBar()
        }

        toolBackClickListener()
    }

    private fun toolBackClickListener() {
        toolBarSearch.setOnClickListener(toolBarClickListener)
        toolbarBack.setOnClickListener(toolBarClickListener)
        toolbarClose.setOnClickListener(toolBarClickListener)
        toolbarDone.setOnClickListener(toolBarClickListener)
        toolbarFilter.setOnClickListener(toolBarClickListener)
    }

    enum class OptionButton(var id: Int) {
        SEARCH(R.id.toolBarSearch), BACK(R.id.toolbarBack), CLOSE(R.id.toolbarClose), DONE(R.id.toolbarDone), FILTER(R.id.toolbarFilter)
    }

    override fun hideOptionMenus() {
        toolbarContainer.findViewById<View>(OptionButton.BACK.id)
        enumValues<OptionButton>().forEach {
            val view = toolbarContainer.findViewById<View>(it.id)
            if (view != null) {
                view.visibility = View.GONE
            }
        }
    }

    override fun showMenus(vararg optionButtons: OptionButton) {
        for (button: OptionButton in optionButtons.iterator()) {
            toolbarContainer.findViewById<View>(button.id).visibility = View.VISIBLE
        }
    }

    override fun showHeaderLogo() {
        tvHeader.visibility = View.GONE
        imgHeader.visibility = View.VISIBLE
    }

    override fun setHeaderText(title: String) {
        tvHeader.visibility = View.VISIBLE
        imgHeader.visibility = View.GONE
        tvHeader.text = title
    }

    private val toolBarClickListener = View.OnClickListener { toolbarButton ->
        getFragmentForeground()?.onToolbarClicked(toolbarButton.id)
    }

    private fun getFragmentForeground(): BaseFragment? {
        val entryCount = supportFragmentManager.backStackEntryCount
        return if (entryCount > 0) {
            val entry = supportFragmentManager.getBackStackEntryAt(entryCount - 1)
            val fragment = supportFragmentManager.findFragmentByTag(entry.name)
            if (fragment is BaseFragment) {
                fragment
            } else {
                null
            }
        } else {
            supportFragmentManager.findFragmentByTag(BusinessListFragment.TAG) as BaseFragment?
        }
    }

    override fun onBackPressed() {
        val entryCount = supportFragmentManager.backStackEntryCount
        if (entryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun hideToolbar() {
        toolbarContainer.visibility = View.GONE
    }

    override fun showToolbar() {
        toolbarContainer.visibility = View.VISIBLE
    }
}