package com.gp.yelp.network.model


import com.google.gson.annotations.SerializedName

data class BusinessList(
        @SerializedName("total")
        val total: Int = 0,
        @SerializedName("region")
        val region: Region,
        @SerializedName("businesses")
        val businesses: List<Business>?) {
    data class Center(
            @SerializedName("latitude")
            val latitude: Double = 0.0,
            @SerializedName("longitude")
            val longitude: Double = 0.0
    )


    data class Region(
            @SerializedName("center")
            val center: Center
    )
}