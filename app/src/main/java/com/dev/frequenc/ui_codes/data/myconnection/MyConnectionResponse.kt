package com.dev.frequenc.ui_codes.data.myconnection

data class MyConnectionResponse(
    val count: Int,
    val `data`: List<ConnectionResponseData>
)

data class ConnectionResponseData(
    val __v: Int,
    val _id: String,
    val created_at: String,
    val from_user_id: FromUserId,
    val id: String,
    val request_status: String,
    val status: String,
    val to_user_id: ToUserId,
    val updated_at: String
)