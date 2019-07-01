package com.gp.yelp.network.model


import com.google.gson.annotations.SerializedName

data class Business(
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("region")
    val region: Region,
    @SerializedName("businesses")
    val businesses: List<BusinessesItem>?
) {
    data class Center(
        @SerializedName("latitude")
        val latitude: Double = 0.0,
        @SerializedName("longitude")
        val longitude: Double = 0.0
    )


    data class Coordinates(
        @SerializedName("latitude")
        val latitude: Double = 0.0,
        @SerializedName("longitude")
        val longitude: Double = 0.0
    )


    data class CategoriesItem(
        @SerializedName("alias")
        val alias: String = "",
        @SerializedName("title")
        val title: String = ""
    )


    data class Region(
        @SerializedName("center")
        val center: Center
    )


    data class BusinessesItem(
        @SerializedName("distance")
        val distance: Double = 0.0,
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("rating")
        val rating: Float = 0.0f,
        @SerializedName("coordinates")
        val coordinates: Coordinates,
        @SerializedName("review_count")
        val reviewCount: Int = 0,
        @SerializedName("transactions")
        val transactions: List<String>?,
        @SerializedName("url")
        val url: String = "",
        @SerializedName("display_phone")
        val displayPhone: String = "",
        @SerializedName("phone")
        val phone: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("alias")
        val alias: String = "",
        @SerializedName("location")
        val location: Location,
        @SerializedName("id")
        val id: String = "",
        @SerializedName("categories")
        val categories: List<CategoriesItem>?,
        @SerializedName("is_closed")
        val isClosed: Boolean = false
    )


    data class Location(
        @SerializedName("country")
        val country: String = "",
        @SerializedName("address3")
        val address3: String = "",
        @SerializedName("address2")
        val address2: String = "",
        @SerializedName("city")
        val city: String = "",
        @SerializedName("address1")
        val address1: String = "",
        @SerializedName("display_address")
        val displayAddress: List<String>?,
        @SerializedName("state")
        val state: String = "",
        @SerializedName("zip_code")
        val zipCode: String = ""
    )
}