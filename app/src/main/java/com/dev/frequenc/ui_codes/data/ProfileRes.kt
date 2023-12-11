package com.dev.frequenc.ui_codes.data

import com.google.gson.annotations.SerializedName

data class ProfileRes(
    @SerializedName("_id")
    val id: String,
    val status: String,
    @SerializedName("profile_images")
    val profileImages: List<String>,
    @SerializedName("mobile_no")
    val mobileNo: String,
    val tickets: List<Any>, // Change the type to the actual type if available
    @SerializedName("genreType")
    val genreType: List<Any>, // Change the type to the actual type if available
    @SerializedName("profileType")
    val profileType: String,
    @SerializedName("vibesDate")
    val vibesDate: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val __v: Int,
    val city: String,
    val country: String,
    val description: String,
    val dob: String,
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    val gender: String,
    val name: String,
    @SerializedName("postalCode")
    val postalCode: String,
    val state: String,
    @SerializedName("banner_image")
    val bannerImage: String,
    @SerializedName("profile_pic")
    val profilePic: String,
    val vibes: String

)