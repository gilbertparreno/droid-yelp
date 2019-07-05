package com.gp.yelp.screen.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.gp.yelp.R
import com.gp.yelp.app.App
import com.gp.yelp.network.model.Address
import com.gp.yelp.screen.base.BaseFragment
import com.gp.yelp.screen.search.AddressAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class SearchFragment : BaseFragment(), AddressAdapter.OnItemClickListener {

    @Inject
    lateinit var mainView: MainView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var viewModel: SearchViewModel

    private var location: Location? = null

    private val REQUEST_ACCESS_COARSE_LOCATION = 1

    private var adapter = AddressAdapter(this)

    private lateinit var southwest: LatLng
    private lateinit var northeast: LatLng

    private lateinit var businessName: String
    private lateinit var address: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity = context as Activity

        let {
            DaggerSearchComponent.builder()
                    .appComponent((activity.application as App).appComponent)
                    .searchModule(SearchModule())
                    .mainView(activity as MainView)
                    .build().inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun setUpToolBar() {
        mainView.hideOptionMenus()
        mainView.showMenus(MainActivity.OptionButton.DONE, MainActivity.OptionButton.BACK)
        mainView.setHeaderText(getString(R.string.search_menu_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments!!.containsKey(EXTRA_BUSINESS_NAME) && arguments!!.containsKey(EXTRA_ADDRESS)) {
            businessName = arguments!!.getString(EXTRA_BUSINESS_NAME)!!
            address = arguments!!.getString(EXTRA_ADDRESS)!!
        } else {
            throw IllegalArgumentException("Must provide necessary arguments.")
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)[SearchViewModel::class.java]
        lifecycle.addObserver(viewModel)

        initViews()

        requestPermission()

        viewModel.liveDataAddress.observe(this, Observer { data ->
            if (data.throwable == null) {
                adapter.clearAdapter()
                adapter.addItems(data.data!!)
            } else {
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                        activity!!,
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
                        val latlng = LatLng(location!!.latitude, location.longitude)
                        southwest = SphericalUtil.computeOffset(latlng, 10000.0, 225.0)
                        northeast = SphericalUtil.computeOffset(latlng, 10000.0, (90 + 45).toDouble())
                        prefillSearch()
                    }
        }
    }

    private fun prefillSearch() {
        etBusinessName.setText(businessName)
        etPlaces.setText(address)
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
        etPlaces.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    adapter.clearAdapter()
                    return
                }

                viewModel.search(s.toString(), northeast, southwest)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        rvAddress.layoutManager = LinearLayoutManager(context)
        rvAddress.adapter = adapter
    }

    override fun onToolbarClicked(buttonId: Int) {
        when (buttonId) {
            MainActivity.OptionButton.DONE.id -> {
                val output = Intent()
                output.putExtra(EXTRA_ADDRESS, etPlaces.text.toString())
                output.putExtra(EXTRA_BUSINESS_NAME, etBusinessName.text.toString())
                val fragment = targetFragment
                fragment?.onActivityResult(BusinessListFragment.REQUEST_SEARCH, Activity.RESULT_OK, output)
                activity?.supportFragmentManager?.popBackStack()
            }
            MainActivity.OptionButton.BACK.id -> {
                val fragment = targetFragment
                fragment?.onActivityResult(BusinessListFragment.REQUEST_SEARCH, Activity.RESULT_CANCELED, null)
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    companion object {
        val TAG = "SearchFragment"
        val EXTRA_ADDRESS = "address"
        val EXTRA_BUSINESS_NAME = "business_name"

        fun newInstance(businessName: String, address: String): SearchFragment {
            return SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_BUSINESS_NAME, businessName)
                    putString(EXTRA_ADDRESS, address)
                }
            }
        }
    }

    override fun onItemClick(address: Address) {
        etPlaces.setText(address.name)
    }
}
