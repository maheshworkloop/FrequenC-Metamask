package com.dev.frequenc.ui_codes.data

import java.io.Serializable

class TrendingArtistResponse (
    val _id : String,
    val status : String,
    val eventCount : String,
    val artist_name : String,
    val banner_pic : String

) : Serializable