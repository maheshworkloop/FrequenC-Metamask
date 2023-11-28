package com.dev.frequenc.ui_codes.data

data class GetVibeCategoryResponse(
    val `data`: List<CategoryDetail>,
    val message: String,
    val status: Boolean
)

data class CategoryDetail(
    val __v: Int,
    val _id: String,
    val created_at: String,
    val id: String,
    val image: String,
    val name: String,
    val status: String,
    val updated_at: String
)