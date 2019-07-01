package com.gp.yelp.screen.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gp.yelp.R
import com.gp.yelp.app.App
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.Business
import com.gp.yelp.screen.base.BaseActivity
import com.gp.yelp.screen.filter.DialogFragmentFilter
import com.gp.yelp.utils.SharedPreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class MainActivity : BaseActivity() {
//
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
//
//    @Inject
//    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var viewModel: MainViewModel

    private val adapter = BusinessAdapter()

    private val REQUESTION_ACCESS_COARSE_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        let {
            DaggerMainComponent.builder()
                .appComponent((application as App).appComponent)
                .mainModule(MainModule())
                .build().inject(this)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.liveDataProjects.observe(this,
            Observer<ApiResponse<Business>> { response ->
                if (response.throwable == null) {
                    adapter.addItems(response.data?.businesses!!)
                }
            })

        initViews()

        requestPermission()
    }

    private fun requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUESTION_ACCESS_COARSE_LOCATION
            )
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    viewModel.getProjectList(location = location!!)
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUESTION_ACCESS_COARSE_LOCATION -> {
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
        rvBusiness.layoutManager = LinearLayoutManager(this)
        rvBusiness.adapter = adapter

        toolbarFilter.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag(DialogFragmentFilter.TAG)
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            val dialogFragment = DialogFragmentFilter()

            dialogFragment.show(ft, DialogFragmentFilter.TAG)
        }

        toolBarSearch.setOnClickListener {

        }
    }
}
