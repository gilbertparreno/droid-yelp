package com.gp.yelp.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Business(
        @SerializedName("hours")
        val hours: List<HoursItem>?,
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("rating")
        val rating: Float = 0.0f,
        @SerializedName("coordinates")
        val coordinates: Coordinates,
        @SerializedName("review_count")
        val reviewCount: Int = 0,
        @SerializedName("photos")
        val photos: List<String>?,
        @SerializedName("url")
        val url: String = "",
        @SerializedName("is_claimed")
        val isClaimed: Boolean = false,
        @SerializedName("display_phone")
        val displayPhone: String = "",
        @SerializedName("phone")
        val phone: String = "",
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
        val isClosed: Boolean = false,
        @SerializedName("distance")
        val distance: Float) : Parcelable {

    @Parcelize
    data class Coordinates(
            @SerializedName("latitude")
            val latitude: Double = 0.0,
            @SerializedName("longitude")
            val longitude: Double = 0.0) : Parcelable


    @Parcelize
    data class CategoriesItem(
            @SerializedName("alias")
            val alias: String = "",
            @SerializedName("title")
            val title: String = "") : Parcelable


    @Parcelize
    data class HoursItem(
            @SerializedName("is_open_now")
            val isOpenNow: Boolean = false,
            @SerializedName("hours_type")
            val hoursType: String = "",
            @SerializedName("open")
            val open: List<OpenItem>?) : Parcelable


    @Parcelize
    data class OpenItem(
            @SerializedName("is_overnight")
            val isOvernight: Boolean = false,
            @SerializedName("start")
            val start: String = "",
            @SerializedName("end")
            val end: String = "",
            @SerializedName("day")
            val day: Int = 0) : Parcelable


    @Parcelize
    data class Location(
            @SerializedName("country")
            val country: String = "",
            @SerializedName("cross_streets")
            val crossStreets: String = "",
            @SerializedName("address3")
            val address1: String = "",
            @SerializedName("address2")
            val address2: String = "",
            @SerializedName("city")
            val city: String = "",
            @SerializedName("address1")
            val address3: String = "",
            @SerializedName("display_address")
            val displayAddress: List<String>?,
            @SerializedName("state")
            val state: String = "",
            @SerializedName("zip_code")
            val zipCode: String = "") : Parcelable
}