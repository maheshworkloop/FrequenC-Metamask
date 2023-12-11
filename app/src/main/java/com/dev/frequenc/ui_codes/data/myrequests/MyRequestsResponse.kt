package com.dev.frequenc.ui_codes.data.myrequests

data class MyRequestsResponse(
    val connectionCount: Int,
    val count: Int,
    val `data`: List<Data>,
    val pendingCount: Int
)