package com.dev.frequenc.ui_codes.data.notification

data class Notification(
    val __v: Int,
    val _id: String,
    val created_at: String,
    val id: String,
    val isRead: Boolean,
    val message: String,
    val notification_type: String,
    val notification_url: String,
    val status: String,
    val updated_at: String,
    val user_id: String
)