package com.gp.yelp.screen.filter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.gp.yelp.utils.SharedPreferenceUtil
import timber.log.Timber
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val sharedPreferenceUtil: SharedPreferenceUtil) : ViewModel(),
    LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Timber.d("TODO: onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {

    }

    fun saveSettings(key: String, value: Any) {
        sharedPreferenceUtil.write(key, value)
    }
}
