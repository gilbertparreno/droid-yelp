package com.gp.yelp.network.model


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("country_blacklist")
    val countryBlacklist: List<String>?,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("parents")
    val parents: List<String>?
)