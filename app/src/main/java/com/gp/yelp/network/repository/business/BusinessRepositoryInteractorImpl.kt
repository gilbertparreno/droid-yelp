package com.gp.yelp.network.repository

import com.gp.yelp.network.model.Business
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.model.Reviews
import com.gp.yelp.network.service.BusinessService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BusinessRepositoryInteractorImpl @Inject constructor(private val businessService: BusinessService) :
        BusinessRepositoryInteractor {

    override fun searchBusinessByLatlng(term: String?, lat: Double?, lng: Double?, radius: Int?, sortBy: String?, openNow: Boolean?, categories: String?): Single<BusinessList> {
        return businessService.searchBusinessByLatlng(term, lat, lng, radius, sortBy, openNow, categories)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun searchBusinessByByAddress(term: String?, radius: Int?, sortBy: String?, openNow: Boolean?, categories: String?, location: String?): Single<BusinessList> {
        return businessService.searchBusinessByByAddress(term, radius, sortBy, openNow, categories, location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getBusinessById(businessId: String): Single<Business> {
        return businessService.getBusinessById(businessId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getReviews(businessId: String): Single<Reviews> {
        return businessService.getReviews(businessId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}