package com.gp.yelp.screen.businessList

import android.os.Parcelable
import com.gp.yelp.utils.DEFAULT_RADIUS
import com.gp.yelp.utils.DEFAULT_SORT_BY_ALIAS
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Settings(
        var businessName: String = "",
        var latitude: Double? = null,
        var longitude: Double? = null,
        var radius: Int = DEFAULT_RADIUS,
        var sortBy: String = DEFAULT_SORT_BY_ALIAS,
        var openNow: Boolean = false,
        var categories: String = "",
        var address: String = ""
) : Parcelable