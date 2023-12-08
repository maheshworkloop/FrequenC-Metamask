package com.dev.frequenc.ui_codes.data.pending_request

data class PendingRequestResponse(
    val count: Int,
    val `data`: List<Data>,
    val requestCount: Int
)