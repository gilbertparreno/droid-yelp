package com.gp.yelp.screen.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gp.yelp.R
import com.gp.yelp.app.App
import com.gp.yelp.network.model.Business
import com.gp.yelp.screen.base.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_business_details.*
import javax.inject.Inject


class BusinessDetailsFragment : BaseFragment() {

    @Inject
    lateinit var mainView: MainView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BusinessDetailsViewModel
    private lateinit var businessDetails: Business

    private val reviewsAdapter = ReviewsAdapter()
    private val operationTimeAdapter = OperationTimeAdapter()

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity = context as Activity

        let {
            DaggerBusinessDetailsComponent.builder()
                    .appComponent((activity.application as App).appComponent)
                    .businessDetailsModule(BusinessDetailsModule())
                    .mainView(activity as MainView)
                    .build().inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(EXTRA_BUSINESS)!!) {
            businessDetails = arguments!!.getParcelable(EXTRA_BUSINESS)!!
        } else {
            throw IllegalArgumentException("Must provide necessary arguments.")
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)[BusinessDetailsViewModel::class.java]

        viewModel.getBusinessDetails(businessDetails.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_business_details, container, false)
    }

    override fun setUpToolBar() {
        mainView.hideToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)

        initViews()

        viewModel.liveDataBusinessDetails.observe(viewLifecycleOwner, Observer { response ->
            if (response.throwable == null) {
                reviewsAdapter.addItems(response.data?.second?.reviews ?: listOf())
                businessDetailsFromNetwork(response.data?.first)
            } else {
                Toast.makeText(context!!, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initViews() {
        if (businessDetails.imageUrl.isNotEmpty()) {
            Picasso.get().load(businessDetails.imageUrl).resize(640, 360).centerCrop().into(imgBusiness)
        }

        tvBusinessName.text = businessDetails.name
        rbRating.rating = businessDetails.rating
        tvAddress.text = businessDetails.location.displayAddress?.joinToString(", ")
        tvReviews.text = resources.getQuantityString(
                R.plurals.lbl_quantity_reviews_2,
                businessDetails.reviewCount,
                businessDetails.reviewCount
        )

        imgClose.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        rvReviews.layoutManager = LinearLayoutManager(context)
        rvReviews.adapter = reviewsAdapter

        rvOperatingHours.layoutManager = LinearLayoutManager(context)
        rvOperatingHours.adapter = operationTimeAdapter

        ViewCompat.setNestedScrollingEnabled(rvReviews, false)
        ViewCompat.setNestedScrollingEnabled(rvOperatingHours, false)
    }

    private fun businessDetailsFromNetwork(businessDetails: Business?) {
        if (businessDetails == null) return
        if (businessDetails.hours != null && businessDetails.hours.isNotEmpty()) {
            val openHours = businessDetails.hours[0]
            operationTimeAdapter.addItems(openHours.open ?: listOf())
        }
    }

    override fun onToolbarClicked(buttonId: Int) {

    }

    companion object {

        val TAG = "BusinessDetailsFragment"
        val EXTRA_BUSINESS = "business"

        fun newInstance(business: Business): BusinessDetailsFragment {
            return BusinessDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_BUSINESS, business)
                }
            }
        }
    }
}
