package com.dev.frequenc.ui_codes.data.transactionlist

data class CategoryBookings(
    val Concert: List<Concert>,
    val Dance: List<Dance>,
    val Exhibitions: List<Exhibition>,
    val Sports: List<Sport>,
    val Theatre: List<Theatre>
)