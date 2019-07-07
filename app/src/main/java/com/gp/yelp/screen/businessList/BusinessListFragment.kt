package com.gp.yelp.screen.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gp.yelp.R
import com.gp.yelp.app.App
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.Business
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.model.Category
import com.gp.yelp.screen.base.BaseFragment
import com.gp.yelp.screen.businessList.BusinessListView
import com.gp.yelp.screen.businessList.Settings
import com.gp.yelp.screen.filter.DialogFragmentFilter
import com.gp.yelp.utils.DEFAULT_RADIUS
import com.gp.yelp.utils.DEFAULT_SORT_BY_ALIAS
import com.gp.yelp.utils.SharedPreferenceUtil
import kotlinx.android.synthetic.main.fragment_business_list.*
import javax.inject.Inject


class BusinessListFragment : BaseFragment(), BusinessListView, BusinessAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    @Inject
    lateinit var mainView: MainView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var viewModel: BusinessListViewModel

    private val adapter = BusinessAdapter(this)

    private val REQUEST_ACCESS_COARSE_LOCATION = 1

    private lateinit var settings: Settings

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as Activity
        let {
            DaggerBusinessListComponent.builder()
                    .appComponent((activity.application as App).appComponent)
                    .mainView(activity as MainView)
                    .businessListModule(BusinessListModule(this))
                    .build().inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_business_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = getSettings()
        viewModel = ViewModelProviders.of(this, viewModelFactory)[BusinessListViewModel::class.java]
    }

    override fun setUpToolBar() {
        mainView.hideOptionMenus()
        mainView.showMenus(MainActivity.OptionButton.FILTER, MainActivity.OptionButton.SEARCH)
        mainView.showHeaderLogo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        initViews()

        requestPermission()
    }

    private fun searchBusinessLiveData() {
        viewModel.searchBusiness(settings).observe(this,
                Observer<ApiResponse<BusinessList>> { response ->
                    if (response.throwable == null && response.data != null) {
                        adapter.addItems(response.data.businesses!!)
                    }
                })
    }

    override fun clearList() {
        adapter.clearAdapter()
    }

    override fun showProgress() {
        progressAnimation.playAnimation()
        progressAnimation.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressAnimation.cancelAnimation()
        progressAnimation.visibility = View.GONE
    }

    override fun showError() {
        hideProgress()
        errorAnim.playAnimation()
        errorContainer.visibility = View.VISIBLE
    }

    override fun hideError() {
        errorAnim.cancelAnimation()
        errorContainer.visibility = View.GONE
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_ACCESS_COARSE_LOCATION
            )
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        settings.longitude = location?.longitude
                        settings.latitude = location?.latitude

                        searchBusinessLiveData()
                    }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_ACCESS_COARSE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestPermission()
                } else {
                    val builder = AlertDialog.Builder(context, R.style.MyDialogTheme)
                    builder.setMessage(R.string.error_location_permission)
                            .setPositiveButton(R.string.lbl_retry
                            ) { dialog, _ ->
                                requestPermission()
                                dialog.dismiss()
                            }
                            .setNegativeButton(android.R.string.cancel
                            ) { dialog, _ ->
                                activity?.finish()
                                dialog.dismiss()
                            }
                    builder.create().show()
                }
                return
            }
        }
    }

    private fun initViews() {
        rvBusiness.layoutManager = LinearLayoutManager(context)
        rvBusiness.adapter = adapter

        errorContainer.setOnClickListener {
            searchBusinessLiveData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SEARCH) {
            if (resultCode == Activity.RESULT_OK) {
                val address = data?.getStringExtra(SearchFragment.EXTRA_ADDRESS)
                val businessName = data?.getStringExtra(SearchFragment.EXTRA_BUSINESS_NAME)
                settings.businessName = businessName ?: ""
                settings.address = address ?: ""
                searchBusinessLiveData()
            }
        } else if (requestCode == REQUEST_FILTER) {
            if (resultCode == Activity.RESULT_OK) {
                val settings = data?.getParcelableExtra<Settings>(DialogFragmentFilter.EXTRA_SETTINGS)!!
                sharedPreferenceUtil.write(SharedPreferenceUtil.Key.SORT_BY, settings.sortBy)
                sharedPreferenceUtil.write(SharedPreferenceUtil.Key.OPEN_NOW, settings.openNow)
                sharedPreferenceUtil.write(SharedPreferenceUtil.Key.RADIUS, settings.radius)
                sharedPreferenceUtil.write(SharedPreferenceUtil.Key.CATEGORIES, settings.categories)

                val listType = object : TypeToken<ArrayList<Category>>() {}.type
                val cat = Gson().fromJson<List<Category>>(settings.categories, listType)
                        .toTypedArray()
                        .joinToString(separator = ",") {
                            it.alias
                        }

                this.settings.apply {
                    sortBy = settings.sortBy
                    openNow = settings.openNow
                    radius = settings.radius
                    categories = cat
                }
                searchBusinessLiveData()
            }
        }
    }

    override fun onToolbarClicked(buttonId: Int) {
        when (buttonId) {
            MainActivity.OptionButton.FILTER.id -> {
                val ft = activity!!.supportFragmentManager.beginTransaction()
                ft.addToBackStack(DialogFragmentFilter.TAG)
                val dialogFragment = DialogFragmentFilter()
                dialogFragment.setTargetFragment(this, REQUEST_FILTER)
                dialogFragment.show(ft, DialogFragmentFilter.TAG)
            }
            MainActivity.OptionButton.SEARCH.id -> {
                val ft = activity!!.supportFragmentManager.beginTransaction()
                ft.addToBackStack(SearchFragment.TAG)
                val fragment = SearchFragment.newInstance(settings.businessName, settings.address)
                fragment.setTargetFragment(this, REQUEST_SEARCH)
                ft.add(R.id.fragmentContainer, fragment, SearchFragment.TAG).commit()
            }
        }
    }

    override fun onItemClick(business: Business) {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        ft.addToBackStack(BusinessDetailsFragment.TAG)
        val fragment = BusinessDetailsFragment.newInstance(business)
        fragment.setTargetFragment(this, REQUEST_SEARCH)
        ft.add(R.id.fragmentContainer, fragment, BusinessDetailsFragment.TAG).commit()
    }

    private fun getSettings(): Settings {
        val radius = sharedPreferenceUtil.getInt(SharedPreferenceUtil.Key.RADIUS, DEFAULT_RADIUS)
        val openNow = sharedPreferenceUtil.getBoolean(SharedPreferenceUtil.Key.OPEN_NOW, false)
        val sortBy = sharedPreferenceUtil.getString(SharedPreferenceUtil.Key.SORT_BY, DEFAULT_SORT_BY_ALIAS)
        val cat = sharedPreferenceUtil.getString(SharedPreferenceUtil.Key.CATEGORIES, "[]")
        val listType = object : TypeToken<ArrayList<Category>>() {}.type
        val categories = Gson().fromJson<List<Category>>(cat, listType)
                .toTypedArray()
                .joinToString(separator = ",") {
                    it.alias
                }
        return Settings(
                radius = radius,
                openNow = openNow,
                sortBy = sortBy,
                categories = categories
        )
    }

    companion object {
        val TAG = "BusinessListFragment"
        val REQUEST_SEARCH = 2
        val REQUEST_FILTER = 3
    }
}
