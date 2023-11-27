package com.dev.frequenc.ui_codes.data.models

class paymentInitiateReq (
    val amount : Double,
    val eventId : String,
    val ticketQuantity : Int,
    val ticketType : String,
    val ticketPrice : Double,
    val billingInformation : List<BillingInformation>
)

class BillingInformation(
    val firstName : String,
    val lastName : String,
    val email : String,
    val phone : String,
    val address : String,
    val State : String,
    val City : String,
    val Country : String
)

