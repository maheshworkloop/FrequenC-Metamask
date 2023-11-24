package com.dev.frequenc.ui_codes.data.confirmuserotp

import com.dev.frequenc.ui_codes.data.confirmuserotp.User

data class Data(
    val isEmailRegistered: Boolean,
    val isEventType: Boolean,
    val message: String,
    val user: User,
    val isUserType: Boolean
)