package com.gp.yelp.network.model


import com.google.gson.annotations.SerializedName

data class Reviews(
        @SerializedName("total")
        val total: Int = 0,
        @SerializedName("possible_languages")
        val possibleLanguages: List<String>?,
        @SerializedName("reviews")
        val reviews: List<Review>?) {
    data class Review(
            @SerializedName("rating")
            val rating: Int = 0,
            @SerializedName("time_created")
            val timeCreated: String = "",
            @SerializedName("id")
            val id: String = "",
            @SerializedName("text")
            val text: String = "",
            @SerializedName("user")
            val user: User,
            @SerializedName("url")
            val url: String = "")


    data class User(
            @SerializedName("profile_url")
            val profileUrl: String = "",
            @SerializedName("image_url")
            val imageUrl: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("id")
            val id: String = "")
}


