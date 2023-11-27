package com.dev.frequenc.ui_codes.data.transactionlist

data class TransactionListRes(
    val categoryBookings: CategoryBookings,
    val categoryCounts: List<CategoryCount>,
    val pastBooking: List<PastBooking>,
    val upcomingBooking: List<UpcomingBooking>
)