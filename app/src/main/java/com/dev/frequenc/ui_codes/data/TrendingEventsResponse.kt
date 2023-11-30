package com.dev.frequenc.ui_codes.data

import java.io.Serializable

class TrendingEventsResponse (

    val _id : String,
    val status : String,
    val eventTitle : String,
    val eventid : String,
    val eventCapacity : String,
    val sub_category : List<String>,
    val country : String,
    val eventImage : List<String>,
    val eventStartDate : String,
    val eventEndDate : String,
    val category : String,
    val is_bookmark : Boolean,
    val venueDetails : VenueDetails
) : Serializable


