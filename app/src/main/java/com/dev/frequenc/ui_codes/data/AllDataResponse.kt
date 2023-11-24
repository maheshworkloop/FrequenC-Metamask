package com.dev.frequenc.ui_codes.data

import java.io.Serializable

class AllDataResponse (
    val id : String,
    val eventid: String,
    val type : String,
    val title : String,
    val eventStartDate : String,
    val image : String)
 : Serializable