package com.gp.yelp.screen.main

import android.location.Location
import androidx.lifecycle.*
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.Business
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import com.gp.yelp.utils.DEFAULT_RADIUS
import com.gp.yelp.utils.DEFAULT_SORT_BY_ALIAS
import com.gp.yelp.utils.SharedPreferenceUtil
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val businessRepositoryInteractor: BusinessRepositoryInteractor,
    private val sharedPreferenceUtil: SharedPreferenceUtil
) :
    ViewModel(),
    LifecycleObserver {

    val liveDataProjects = MutableLiveData<ApiResponse<Business>>()

    private val disposable = CompositeDisposable()

    private var radius: Int = DEFAULT_RADIUS
    private var openNow = false
    private lateinit var sortBy: String

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        refreshFilters()
    }

    fun onLocationAvailable(location: Location) {
        getProjectList(location = location!!)
    }

    private fun refreshFilters() {
        radius = sharedPreferenceUtil.getInt(SharedPreferenceUtil.Key.RADIUS, DEFAULT_RADIUS)
        openNow = sharedPreferenceUtil.getBoolean(SharedPreferenceUtil.Key.OPEN_NOW, false)
        sortBy = sharedPreferenceUtil.getString(SharedPreferenceUtil.Key.SORT_BY, DEFAULT_SORT_BY_ALIAS)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposable.clear()
    }

    fun getProjectList(term: String = "", location: Location) {
        refreshFilters()
        disposable.add(businessRepositoryInteractor.getBusiness(term, location.latitude, location.longitude, radius, sortBy, openNow)
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
