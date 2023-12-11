package com.dev.frequenc.ui_codes.util

class KeysConstant {

    companion object
    {
//        const val BASE_URL = "http://192.168.1.6:3001/api/v1/"

        val CHAT_User_Name: String = "Chat_User_Names"
        val CHAT_TOKEN: String = "Chat_Tokn"

        //        const val BASE_URL = "http://192.168.1.8:3001/api/v1/"

        const val BASE_URL = "http://ec2-43-204-108-136.ap-south-1.compute.amazonaws.com:3001/api/v1/"

        const val CHAT_BASE_URL = "a71.chat.agora.io"

        const val BROWSE_BY_CAT = "event/category/browse-by-category"

        const val TRENDING_EVENTS = "event/trendingevents"

        const val TRENDING_EVENTS_TOKEN = "event/home/trendingevents"

        const val TRENDING_ARTIST = "artist/trendingartist"

        const val TRENDING_ARTIST_TOKEN = "event/home/trendingartist"

        const val ALL_DATA = "event/all-data/all"
        const val MYPENDING_REQUEST = "connect/myPendingRequest"

        const val ALL_DATA_TOKEN = "event/all-data/all"

        const val Register_User = "auth/registerUser"
        const val ConfirmUserOtp = "auth/confirmUserOtp"

        const val UpdateUserType = "auth/updateUserType"


        const val Event_Detail = "event"
        const val Notification = "notification"
        const val SavedEvents = "bookmark/event"

        const val ARTIST_DETAIL_BY_ID = "artist"
        const val TransactionList = "payments/transaction-list"

        const val BOOKMARK_EVENT = "bookmark/savedEvents"
        const val Register_attendee = "auth/registerAttendee"
        const val Initiate_payment = "payments/initiate-payment"

        const val Vibe_Event = "connect/vibe-event"

        const val GET_VIBE_CATEGORY = "connect/vibe-categories"

        const val MATCH_VIBE_USER_LIST = "connect/match-vibe/{category}"

        const val My_Request = "connect/myRequest"
        const val MyConnection = "connect/myConnection"

        const val GET_QUOTE = "connect/quote"

        const val UPDATE_CONNECT_PROFILE = "audience/update-mobile-profile/{audience_id}"
    }


}