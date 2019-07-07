package com.gp.yelp.screen.main

import androidx.lifecycle.*
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import com.gp.yelp.screen.businessList.BusinessListView
import com.gp.yelp.screen.businessList.Settings
import com.gp.yelp.utils.AbsentLiveData
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BusinessListViewModel @Inject constructor(
        private val businessRepositoryInteractor: BusinessRepositoryInteractor,
        private val businessListView: BusinessListView
) :
        ViewModel(),
        LifecycleObserver {

    private val disposable = CompositeDisposable()

    var hasLocationPermission = false

    private val businessListMutableLiveData = MutableLiveData<ApiResponse<BusinessList>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposable.clear()
    }

    fun getBusinessListLiveData(): LiveData<ApiResponse<BusinessList>> = businessListMutableLiveData

    fun searchBusiness(settings: Settings) {
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
                            businessListMutableLiveData.postValue(ApiResponse(data))
                            businessListView.hideProgress()
                        }, { throwable ->
                    businessListMutableLiveData.postValue(ApiResponse(throwable = throwable))
                    businessListView.hideProgress()
                    businessListView.showError()
                }
                ))
    }
}
