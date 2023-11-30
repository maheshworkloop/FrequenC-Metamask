package com.dev.frequenc.ui_codes.data

data class QuoteResponse(
    val `data`: List<QuoteResponseData>,
    val message: String,
    val status: Boolean
)

data class QuoteResponseData(
    val __v: Int,
    val _id: String,
    val created_at: String,
    val id: String,
    val name: String,
    val status: String,
    val updated_at: String
)