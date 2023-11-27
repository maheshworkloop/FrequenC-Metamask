package com.dev.frequenc.ui_codes.data.transactionlist

data class PastBooking(
    val amount: Any,
    val contractAddress: String,
    val eventImage: List<String>,
    val eventStartDate: String,
    val eventTitle: String,
    val ownerAddress: String,
    val payment_status: String,
    val ticket_quantity: Int,
    val ticket_type: String,
    val tokenId: Int,
    val transactionHash: String
)