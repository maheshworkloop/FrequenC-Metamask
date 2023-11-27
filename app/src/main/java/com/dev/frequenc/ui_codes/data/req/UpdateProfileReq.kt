package com.dev.frequenc.ui_codes.data.req

import android.graphics.Bitmap

data class UpdateProfileReq (
    val fullName: String,
    val profile_pic: String,
    val banner_image: String,
    val email: String,
    val country: String,
    val state: String,
    val city: String,
    val postalCode: String,
    val gender: String,
    val dob: String,
    val description: String
)