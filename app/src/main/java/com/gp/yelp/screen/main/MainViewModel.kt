package com.gp.yelp.screen.main

import android.location.Location
import androidx.lifecycle.*
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.Business
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import com.gp.yelp.utils.SharedPreferenceUtil
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val businessRepositoryInteractor: BusinessRepositoryInteractor,
        private val sharedPreferenceUtil: SharedPreferenceUtil) :
    ViewModel(),
    LifecycleObserver {

    val liveDataProjects = MutableLiveData<ApiResponse<Business>>()

    private val disposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposable.clear()
    }

    fun getProjectList(term : String = "", location: Location) {
        disposable.add(businessRepositoryInteractor.getBusiness(term, location.latitude, location.longitude)
            .subscribe(
                { data ->
                    liveDataProjects.postValue(ApiResponse(data))
                }, { throwable ->
                    liveDataProjects.postValue(ApiResponse(throwable = throwable))
                }
            ))
    }

    fun saveSettings(key: String, value: Any) {
        sharedPreferenceUtil.write(key, value)
    }
}
