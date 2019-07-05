package com.gp.yelp.screen.filter

import androidx.lifecycle.*
import com.gp.yelp.utils.DEFAULT_RADIUS
import com.gp.yelp.utils.DEFAULT_SORT_BY_ALIAS
import com.gp.yelp.utils.SharedPreferenceUtil
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val sharedPreferenceUtil: SharedPreferenceUtil) : ViewModel(),
    LifecycleObserver {

    val liveDataRadius = MutableLiveData<Int>()
    val liveDataOpenNow = MutableLiveData<Boolean>()
    val liveDataSortBy = MutableLiveData<String>()
    val liveDataCategories = MutableLiveData<String>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        liveDataRadius.postValue(sharedPreferenceUtil.getInt(SharedPreferenceUtil.Key.RADIUS, DEFAULT_RADIUS))
        liveDataOpenNow.postValue(sharedPreferenceUtil.getBoolean(SharedPreferenceUtil.Key.OPEN_NOW, false))
        liveDataSortBy.postValue(sharedPreferenceUtil.getString(SharedPreferenceUtil.Key.SORT_BY, DEFAULT_SORT_BY_ALIAS))
        liveDataCategories.postValue(sharedPreferenceUtil.getString(SharedPreferenceUtil.Key.CATEGORIES, "[]"))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {

    }

    fun saveSettings(key: String, value: Any) {
        sharedPreferenceUtil.write(key, value)
    }
}
