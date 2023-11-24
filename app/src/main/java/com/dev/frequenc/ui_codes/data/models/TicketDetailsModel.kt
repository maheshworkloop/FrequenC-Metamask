package com.dev.frequenc.ui_codes.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TicketDetailsModel (
        var amount: String,
        var contractAddress: String,
        var eventImage: List<String>,
        var eventStartDate: String,
        var eventTitle: String,
        var ownerAddress: String,
        var payment_status: String,
        var ticket_quantity: Int,
        var ticket_type: String,
        var tokenId: Int,
        var transactionHash: String
    ) : Parcelable