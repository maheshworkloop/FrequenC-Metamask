package com.dev.frequenc.ui_codes.data.saved_event

data class SavedEventResponseItem(
    val __v: Int,
    val _id: String,
    val artist_id: List<String>,
    val audience_id: List<Any>,
    val bceventid: String,
    val category: String,
    val city: String,
    val country: String,
    val created_at: String,
    val description: String,
    val eventCapacity: String,
    val eventEndDate: String,
    val eventImage: List<String>,
    val eventStartDate: String,
    val eventTitle: String,
    val event_created_by: String,
    val event_publish_type: String,
    val gallery: List<Any>,
    val is_published: Boolean,
    val language: String,
    val state: String,
    val status: String,
    val sub_category: List<String>,
    val tags: List<String>,
    val tickets: List<Any>,
    val trendingScore: Int,
    val updated_at: String,
    val user_id: String,
    val vendor_id: List<Any>,
    val venueApproval: Boolean,
    val venueid: Venueid
)