package com.gp.yelp.screen.main

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import com.gp.yelp.screen.businessList.BusinessListView
import com.gp.yelp.screen.businessList.Settings
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BusinessListViewModel @Inject constructor(
        private val businessRepositoryInteractor: BusinessRepositoryInteractor,
        private val businessListView: BusinessListView
) :
        ViewModel(),
        LifecycleObserver {

    private val disposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposable.clear()
    }

    fun searchBusiness(settings: Settings): LiveData<ApiResponse<BusinessList>> {
        val mutableLiveData = MutableLiveData<ApiResponse<BusinessList>>()
        val searchSingle = if (settings.address.isNotEmpty()) {
            businessRepositoryInteractor.searchBusinessByByAddress(
                    settings.businessName,
                    settings.radius,
                    settings.sortBy,
                    settings.openNow,
                    settings.categories,
                    settings.address
            )
        } else {
            businessRepositoryInteractor.searchBusinessByLatlng(
                    settings.businessName,
                    settings.latitude,
                    settings.longitude,
                    settings.radius,
                    settings.sortBy,
                    settings.openNow,
                    settings.categories
            )
        }

        disposable.add(searchSingle.doOnSubscribe {
            businessListView.clearList()
            businessListView.hideError()
            businessListView.showProgress()
        }
                .subscribe(
                        { data ->
                            mutableLiveData.postValue(ApiResponse(data))
                            businessListView.hideProgress()
                        }, { throwable ->
                    mutableLiveData.postValue(ApiResponse(throwable = throwable))
                    businessListView.hideProgress()
                    businessListView.showError()
                }
                ))

        return mutableLiveData
    }
}
