package com.gp.yelp.network.repository

import com.gp.yelp.network.model.Business
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.model.Reviews
import io.reactivex.Single
import retrofit2.http.Query

interface BusinessRepositoryInteractor {
    fun searchBusinessByLatlng(
            term: String?,
            lat: Double?,
            lng: Double?,
            radius: Int?,
            sortBy: String?,
            openNow: Boolean?,
            categories: String?): Single<BusinessList>

    fun searchBusinessByByAddress(
            term: String?,
            radius: Int?,
            sortBy: String?,
            openNow: Boolean?,
            categories: String?,
            location: String?): Single<BusinessList>

    fun getBusinessById(businessId: String): Single<Business>

    fun getReviews(businessId: String): Single<Reviews>
}