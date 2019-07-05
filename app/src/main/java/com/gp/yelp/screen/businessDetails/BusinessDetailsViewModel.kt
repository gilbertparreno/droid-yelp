package com.gp.yelp.screen.main

import androidx.lifecycle.*
import com.google.android.libraries.places.api.net.PlacesClient
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.Business
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.model.Reviews
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

class BusinessDetailsViewModel
@Inject constructor(
        private val businessRepositoryInteractor: BusinessRepositoryInteractor) :
        ViewModel(),
        LifecycleObserver {

    private val disposable = CompositeDisposable()

    val liveDataBusinessDetails = MutableLiveData<ApiResponse<Pair<Business, Reviews>>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    fun getBusinessDetails(businessId: String) {
        disposable.add(
                businessRepositoryInteractor.getBusinessById(businessId).zipWith(
                        businessRepositoryInteractor.getReviews(businessId),
                        BiFunction<Business, Reviews, Pair<Business, Reviews>> { t1, t2 ->
                            Pair(t1, t2)
                        }).subscribe({
                    liveDataBusinessDetails.postValue(ApiResponse(it))
                }, {
                    liveDataBusinessDetails.postValue(ApiResponse(throwable = it))
                })
        )
    }
}
