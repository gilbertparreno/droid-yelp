package com.gp.yelp.network.repository

import com.gp.yelp.network.model.Business
import com.gp.yelp.network.service.BusinessService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BusinessRepositoryInteractorImpl @Inject constructor(private val businessService: BusinessService) :
    BusinessRepositoryInteractor {

    override fun getBusiness(term: String, lat: Double, lng: Double): Single<Business> {
        return businessService.searchBusiness(term, lat, lng)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}