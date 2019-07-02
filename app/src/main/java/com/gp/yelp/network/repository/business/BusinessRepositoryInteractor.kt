package com.gp.yelp.network.repository

import com.gp.yelp.network.model.Business
import io.reactivex.Single

interface BusinessRepositoryInteractor {
    fun getBusiness(term: String?,
                    lat: Double?,
                    lng: Double?,
                    radius: Int?,
                    sortBy: String?,
                    openNow: Boolean?) : Single<Business>

    interface Callback {
        fun onSubscribe()
        fun onError()
        fun onSuccess()
    }
}