package com.gp.yelp.network.service

import com.gp.yelp.network.model.Business
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface BusinessService {

    @GET("businesses/search?limit=20")
    fun searchBusiness(
            @Query("term") term: String?,
            @Query("latitude") lat: Double?,
            @Query("longitude") lng: Double?,
            @Query("radius") radius: Int?,
            @Query("sort_by") sortBy: String?,
            @Query("open_now") openNow: Boolean?): Single<Business>
}