package com.dev.frequenc.ui_codes.data

class InitiatePaymentResponse (
    val stripePayment: StripePayment,
    val ephemeralKey: EphemeralKey,
    val clientSecret : String
)

data class StripePayment(
    val clientSecret: String,
    val customerId: String
)

data class EphemeralKey(
    val id: String,
    val `object`: String,
    val associatedObjects: List<AssociatedObject>,
    val created: Long,
    val expires: Long,
    val livemode: Boolean,
    val secret: String
)

data class AssociatedObject(
    val id: String,
    val type: String
)