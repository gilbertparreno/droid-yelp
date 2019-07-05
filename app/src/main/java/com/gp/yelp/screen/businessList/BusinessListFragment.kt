package com.gp.yelp.screen.main

import android.Manifest
import android.app.Activity
import android.content.Context
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
import com.gp.yelp.R
import com.gp.yelp.app.App
import com.gp.yelp.network.model.Address
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.Business
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.screen.base.BaseFragment
import com.gp.yelp.screen.filter.DialogFragmentFilter
import com.gp.yelp.screen.filter.FilterListener
import kotlinx.android.synthetic.main.fragment_business_list.*
import javax.inject.Inject


class BusinessListFragment : BaseFragment(), FilterListener, BusinessAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mainView: MainView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var viewModel: BusinessListViewModel

    private val adapter = BusinessAdapter(this)

    private val REQUEST_ACCESS_COARSE_LOCATION = 1

    private var location: Location? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as Activity
        let {
            DaggerBusinessListComponent.builder()
                    .appComponent((activity.application as App).appComponent)
                    .mainView(activity as MainView)
                    .businessListModule(BusinessListModule())
                    .build().inject(this)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_business_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        viewModel.liveDataProjects.observe(this,
                Observer<ApiResponse<BusinessList>> { response ->
                    if (response.throwable == null && response.data != null) {
                        adapter.clearAdapter()
                        adapter.addItems(response.data.businesses!!)
                    }
                })

        viewModel.liveDataPlaceIdtoLatLng.observe(this, Observer { response ->
            if (response.throwable != null) {
                Toast.makeText(context, response.throwable.message
                        ?: getString(R.string.error_generic), Toast.LENGTH_LONG).show()
            }
        })

        initViews()

        requestPermission()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_ACCESS_COARSE_LOCATION)
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        this.location = location
                        viewModel.onLocationAvailable(location!!)
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
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    private fun initViews() {
        rvBusiness.layoutManager = LinearLayoutManager(context)
        rvBusiness.adapter = adapter
    }

    override fun applyFilter() {
        viewModel.getProjectList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SEARCH) {
            if (resultCode == Activity.RESULT_OK) {
                val address = data?.getParcelableExtra<Address>(SearchFragment.EXTRA_ADDRESS)
                val businessName = data?.getStringExtra(SearchFragment.EXTRA_BUSINESS_NAME)
                viewModel.searchByLatlngAndName(businessName ?: "", address)
            }
        }
    }

    override fun onToolbarClicked(buttonId: Int) {
        when (buttonId) {
            MainActivity.OptionButton.FILTER.id -> {
                val ft = activity!!.supportFragmentManager.beginTransaction()
                ft.addToBackStack(DialogFragmentFilter.TAG)
                val dialogFragment = DialogFragmentFilter(this)

                dialogFragment.show(ft, DialogFragmentFilter.TAG)
            }
            MainActivity.OptionButton.SEARCH.id -> {
                val ft = activity!!.supportFragmentManager.beginTransaction()
                ft.addToBackStack(SearchFragment.TAG)
                val fragment = SearchFragment.newInstance(
                        viewModel.businessName ?: "",
                        if (viewModel.address != null) viewModel.address?.name ?: "" else "")
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

    companion object {
        val TAG = "BusinessListFragment"
        val REQUEST_SEARCH = 2
    }
}
