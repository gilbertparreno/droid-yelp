package com.gp.yelp.network.repository

import com.gp.yelp.network.model.Business
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.model.Reviews
import io.reactivex.Single

interface BusinessRepositoryInteractor {
    fun searchBusiness(term: String?,
                       lat: Double?,
                       lng: Double?,
                       radius: Int?,
                       sortBy: String?,
                       openNow: Boolean?,
                       categories: String?): Single<BusinessList>

    fun getBusinessById(businessId: String): Single<Business>

    fun getReviews(businessId: String): Single<Reviews>
}