package com.gp.yelp.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
        @SerializedName("country_blacklist")
        val countryBlacklist: List<String> = listOf(),
        @SerializedName("alias")
        val alias: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("parents")
        val parents: List<String> = listOf(),
        var isSelected: Boolean = false
) : Parcelable