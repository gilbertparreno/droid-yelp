package com.gp.yelp.network.service

import com.gp.yelp.network.model.Business
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.model.Reviews
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BusinessService {

    @GET("businesses/search?limit=20")
    fun searchBusiness(
            @Query("term") term: String?,
            @Query("latitude") lat: Double?,
            @Query("longitude") lng: Double?,
            @Query("radius") radius: Int?,
            @Query("sort_by") sortBy: String?,
            @Query("open_now") openNow: Boolean?,
            @Query("categories") categories: String?): Single<BusinessList>

    @GET("businesses/{id}")
    fun getBusinessById(@Path("id") businessId: String): Single<Business>

    @GET("https://api.yelp.com/v3/businesses/{id}/reviews")
    fun getReviews(@Path("id") businessId: String): Single<Reviews>
}