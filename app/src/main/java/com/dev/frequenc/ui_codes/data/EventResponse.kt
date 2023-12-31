package com.dev.frequenc.ui_codes.data

import java.io.Serializable

class EventResponse (
    val eventDetails: EventDetails,
    val artist: List<Artist>,
    val eventTicket: List<EventTicket>,
    val venueDetails: VenueDetails,
    val startPrice : Double
) : Serializable


data class EventDetails(
    val _id: String,
    val status: String,
    val eventTitle: String,
    val eventCapacity: String,
    val category: String,
    val sub_category: List<String>,
    val country: String,
    val state: String,
    val city: String,
    val user_id: String,
    val artist_id: List<String>,
    val vendor_id: List<String>,
    val audience_id: List<String>,
    val tags: List<String>,
    val eventImage: List<String>,
    val description: String,
    val tickets: List<String>,
    val venueApproval: Boolean,
    val bceventid: String,
    val eventStartDate: String,
    val eventEndDate: String,
    val gallery: List<String>,
    val trendingScore: Int,
    val event_created_by: String,
    val event_publish_type: String,
    val language: String,
    val is_published: Boolean,
    val created_at: String,
    val updated_at: String,
    val __v: Int,
    val venueid: String
) : Serializable

data class Artist(
    val artist_name: String,
    val category: String,
    val profile_pic: String,
    val _id: String
) : Serializable

data class EventTicket(
    val _id: String,
    val status: String,
    val event_id: String,
    val user_id: String,
    val no_of_tickets: Int,
    val left_tickets: Int,
    val ticket_type: String,
    val price: Double,
    val group_discount: Double,
    val minimum_order: Int,
    val maximum_order: Int,
    val ticket_description: String,
    val ticket_instruction: String,
    val ticket_visibility: Boolean,
    val ticketStartDate: String,
    val ticketEndDate: String,
    val created_at: String,
    val updated_at: String,
    val __v: Int,
    val id: String,
    var selected : Boolean = false,
    var count : Int = 0
) : Serializable

data class VenueDetails(
    val _id: String,
    val status: String,
    val basic_amenities: List<String>,
    val section: List<String>,
    val venue_type: List<String>,
    val managed_by: String,
    val manager: List<String>,
    val gallery: List<String>,
    val portfolio: List<String>,
    val phone_no: String,
    val tags: List<String>,
    val section_details: List<String>,
    val business_rules: List<String>,
    val event: List<String>,
    val blackout_dates: List<String>,
    val bank_id: List<String>,
    val kyc_id: List<String>,
    val created_at: String,
    val updated_at: String,
    val __v: Int,
    val address: String,
    val banner_pic: String,
    val city: String,
    val country: String,
    val description: String,
    val email: String,
    val fullName: String,
    val facebook_url: String,
    val instagram_url: String,
    val maximum_capacity: Int,
    val minimum_capacity: Int,
    val postalCode: String,
    val profile_pic: String,
    val state: String,
    val twitter_url: String,
    val venue_locality: String ="",
    val venue_name: String = "",
    val venue_price: Double,
    val youtube_url: String,
    val countryName: String,
    val cityName: String
) : Serializable
