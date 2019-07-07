package com.gp.yelp.screen.filter

import androidx.lifecycle.*
import com.gp.yelp.screen.businessList.Settings
import com.gp.yelp.utils.DEFAULT_RADIUS
import com.gp.yelp.utils.DEFAULT_SORT_BY_ALIAS
import com.gp.yelp.utils.SharedPreferenceUtil
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val sharedPreferenceUtil: SharedPreferenceUtil) : ViewModel(),
        LifecycleObserver {

    var settings: Settings = Settings(
            radius = sharedPreferenceUtil.getInt(SharedPreferenceUtil.Key.RADIUS, DEFAULT_RADIUS),
            openNow = sharedPreferenceUtil.getBoolean(SharedPreferenceUtil.Key.OPEN_NOW, false),
            sortBy = sharedPreferenceUtil.getString(SharedPreferenceUtil.Key.SORT_BY, DEFAULT_SORT_BY_ALIAS),
            categories = sharedPreferenceUtil.getString(SharedPreferenceUtil.Key.CATEGORIES, "[]"))
}
