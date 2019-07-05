package com.gp.yelp.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(val placeId: String, val name: String, val subText: String) : Parcelable