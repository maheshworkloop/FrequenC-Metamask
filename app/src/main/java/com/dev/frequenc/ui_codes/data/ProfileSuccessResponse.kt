package com.dev.frequenc.ui_codes.data

data class ProfileSuccessResponse (
val ekyc: Boolean,
val kyb_email_verified: Boolean,
val _id: String,
val status: String,
val isOtpVerified: Boolean,
val phone_no: String,
val user_type: String,
val audience_id: String,
val venue_id: List<String>,
val device_type: String,
val createdAt: String,
val updatedAt: String,
val __v: Int,
val email: String,
val city: String,
val country: String,
val dob: String,
val gender: String,
val lastName: String,
val marital_status: String,
val postalCode: String,
val state: String,
val wallet: Double,
val fullName: String?,
val id: String
)
