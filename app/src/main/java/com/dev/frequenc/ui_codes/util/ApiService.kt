package com.dev.frequenc.ui_codes.util

import com.dev.frequenc.ui_codes.data.SearchResponse
import com.dev.frequenc.ui_codes.data.RegisterUserResponse
import com.dev.frequenc.ui_codes.screens.login.LoginViewModel
import com.dev.frequenc.ui_codes.screens.login.UpdateUserReq
import com.dev.frequenc.ui_codes.data.AllDataResponse
import com.dev.frequenc.ui_codes.data.ArtistResponse
import com.dev.frequenc.ui_codes.data.AudienceDataResponse
import com.dev.frequenc.ui_codes.data.BookmarkEventResponse
import com.dev.frequenc.ui_codes.data.BrowseByCatResponse
import com.dev.frequenc.ui_codes.data.CityResponse
import com.dev.frequenc.ui_codes.data.CountryResponse
import com.dev.frequenc.ui_codes.data.EventResponse
import com.dev.frequenc.ui_codes.data.GetVibeCategoryResponse
import com.dev.frequenc.ui_codes.data.InitiatePaymentResponse
import com.dev.frequenc.ui_codes.data.MatchVibeListResponse
import com.dev.frequenc.ui_codes.data.ProfileRes
import com.dev.frequenc.ui_codes.data.ProfileSuccessResponse
import com.dev.frequenc.ui_codes.data.QuoteResponse
import com.dev.frequenc.ui_codes.data.SendInvitationResponse
import com.dev.frequenc.ui_codes.data.StateResponse
import com.dev.frequenc.ui_codes.data.TrendingArtistResponse
import com.dev.frequenc.ui_codes.data.TrendingArtistTokenResponse
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.ui_codes.data.TrendingEventsTokenResponse
import com.dev.frequenc.ui_codes.data.UpcomingEventResponse
import com.dev.frequenc.ui_codes.data.UpdateAgoraDetailResponse
import com.dev.frequenc.ui_codes.data.UpdateAgoraModel
import com.dev.frequenc.ui_codes.data.VenueDetailsResponse
import com.dev.frequenc.ui_codes.data.VibeEventResponse
import com.dev.frequenc.ui_codes.data.WalletBalenceData
import com.dev.frequenc.ui_codes.data.accept_invitation.RequestAcceptResponse
import com.dev.frequenc.ui_codes.data.confirmuserotp.ConfirmOtpResponse
import com.dev.frequenc.ui_codes.data.models.paymentInitiateReq
import com.dev.frequenc.ui_codes.data.myconnection.MyConnectionResponse
import com.dev.frequenc.ui_codes.data.myrequests.MyRequestsResponse
import com.dev.frequenc.ui_codes.data.notification.NotificationResponse
import com.dev.frequenc.ui_codes.data.pending_request.PendingRequestResponse
import com.dev.frequenc.ui_codes.data.req.SavedEventsReq
import com.dev.frequenc.ui_codes.data.req.UpdatePaymentRequest
import com.dev.frequenc.ui_codes.data.saved_event.SavedEventResponse
import com.dev.frequenc.ui_codes.data.transactionlist.TransactionListRes
import com.dev.frequenc.ui_codes.data.update_user_type.UpdateUserTypeResponse
import com.dev.frequenc.ui_codes.util.Constants.Companion.Authorization
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {


    @POST(KeysConstant.Register_User)
    fun register(@Body phone_no: LoginViewModel.registerUserReq ): Call<RegisterUserResponse>?

    @POST(KeysConstant.ConfirmUserOtp)
    fun confirmUserOtp(@Body verifyOtpReq: LoginViewModel.VerifyOtpReq) : Call<ConfirmOtpResponse>?

    @POST(KeysConstant.UpdateUserType)

    fun updateUserType(@Header("Authorization") tokenMsg: String, @Body updateUserReq : UpdateUserReq): Call<UpdateUserTypeResponse>?

    @GET(KeysConstant.BROWSE_BY_CAT)
    fun browseByCat(): Call<List<BrowseByCatResponse?>>?

    @GET(KeysConstant.TRENDING_EVENTS)
    fun trendingEvents(): Call<List<TrendingEventsResponse>>?

    @GET(KeysConstant.TRENDING_ARTIST)
    fun trendingArtist(): Call<List<TrendingArtistResponse>>?

    @GET(KeysConstant.ALL_DATA)
    fun allData(): Call<List<AllDataResponse>>?

    @GET("event/{id}")
    fun getEvent(@Path("id") id: String): Call<List<EventResponse>>

    @GET("artist/{id}")
    fun getArtistById(@Path("id") id: String): Call<ArtistResponse>

    @GET("artist/upcomingEvents/{id}")
    fun upcomingEventsByArtistId(@Path("id") id: String): Call<List<UpcomingEventResponse>>

    @GET("venue/{id}")
    fun venueDetailsById(@Path("id") id: String): Call<VenueDetailsResponse>

//    api/v1/event/all/{searchValue}

    @GET("event/all/{searchValue}")
    fun searchEvent(@Path("searchValue") searchValue: String): Call<MutableList<SearchResponse>>

    @GET("event/categoryList/{category}")
    fun browseByCatDetail(@Path("category") category: String): Call<List<TrendingEventsResponse>>
  
    @GET(KeysConstant.Notification)
    fun notification(@Header(Constants.Authorization) tokens: String): Call<NotificationResponse>

    @GET(KeysConstant.SavedEvents)
    fun savedEvents(@Header(Constants.Authorization) tokens: String ): Call<SavedEventResponse>
    @POST("bookmark/savedEvents")
    fun bookmarkEvent(@Header(Constants.Authorization) tokens: String, @Body savedEventsReq: SavedEventsReq): Call<BookmarkEventResponse>

    @GET("audience/{id}")
    fun getProfile(@Header(Constants.Authorization) tokens: String, @Path("id") id: String): Call<AudienceDataResponse>

    @GET(KeysConstant.TransactionList)
    fun getTransactionlist(@Header(Constants.Authorization) token: String): Call<TransactionListRes>


    @GET("metadata/countries")
    fun getCountry(): Call<List<CountryResponse>>

    @GET("metadata/countries/{isocode}/states")
    fun getState(@Path("isocode") isocode : String): Call<List<StateResponse>>

    @GET("metadata/cities/{stateId}")
    fun getCity(@Path("stateId") stateId : String): Call<List<CityResponse>>

//    @Multipart
    @FormUrlEncoded
    @PUT("audience/update-profile/{id}")
    fun updateProfile(
    @Header(Constants.Authorization) token : String,
    @Path("id") id : String,
//                      @Body profileKey : ProfileKey
    @Field("profile_pic") profile_pic: String,
    @Field("banner_image")     banner_image : String,
    @Field("fullName") fullName : String,
//    @Body updateProfileReq: UpdateProfileReq
    @Field("email") email : String,
    @Field("country") country : String,
    @Field("state") state : String,
    @Field("city") city : String,
    @Field("postalCode") postalCode : String,
    @Field("gender") gender : String,
    @Field("dob") dob : String
                      )
    : Call<ProfileSuccessResponse>


    @GET(KeysConstant.TRENDING_EVENTS_TOKEN)
    fun trendingEventsbyToken(@Header(Constants.Authorization) token : String): Call<TrendingEventsTokenResponse>?

    @GET(KeysConstant.TRENDING_ARTIST_TOKEN)
    fun trendingArtistbyToken(@Header(Authorization) token: String ): Call<TrendingArtistTokenResponse>?

    @GET(KeysConstant.ALL_DATA)
    fun allDatabyId(): Call<List<AllDataResponse>>?

    @POST(KeysConstant.Register_attendee)
    fun registerAttendee(@Body phone_no: JSONObject): Call<Any>?
    @GET("contract/getTokens/{eth_address}")
    fun metamaskBalence(@Path("eth_address") et_r: String): Call<WalletBalenceData>?
    @POST(KeysConstant.Initiate_payment)
    fun initiatePayment(@Header(Authorization) tokens : String,   @Body paymentInitiate : paymentInitiateReq): Call<InitiatePaymentResponse>?

    @POST(KeysConstant.Update_Payment)
    fun updatePaymentStatus(@Header(Authorization) tokens : String , @Body updatePaymentRequest : UpdatePaymentRequest): Call<InitiatePaymentResponse>?


    @GET(KeysConstant.Vibe_Event)
    fun vibeEvent(@Header(Authorization) tokens : String): Call<VibeEventResponse>
    @GET(KeysConstant.GET_VIBE_CATEGORY)
    fun getVibeCategory(): Call<GetVibeCategoryResponse>?

    @PUT("audience/update-vibes/{id}")
    fun updateVibe(@Header(Authorization) token: String,@Path("id")id : String, @Body vibes : String ): Call<AudienceDataResponse>?

    @GET(KeysConstant.MATCH_VIBE_USER_LIST)
    fun getMatchVibeList(@Header(Authorization) token: String,@Path("category")category : String): Call<MatchVibeListResponse>?
    fun updateVibe(@Header(Authorization) token: String,id : String ): Call<AudienceDataResponse>?
    @GET(KeysConstant.MyConnection)
    fun connectionList(@Header(Authorization) token: String): Call<MyConnectionResponse>?
    @GET(KeysConstant.My_Request)
    fun myRequestApi(@Header(Authorization)token: String): Call<MyRequestsResponse>?

    @GET(KeysConstant.GET_QUOTE)
    fun getQuoteApi(): Call<QuoteResponse>?

    @FormUrlEncoded
    @PUT(KeysConstant.UPDATE_CONNECT_PROFILE)
    fun updateConnectProfile(@Header(Authorization) token: String,@Path("audience_id")id : String,
                             @Field("fullName") fullName : String,
                             @Field("email") email : String,
                             @Field("location") location : String,
                             @Field("dob") dob : String,
                             @Field("gender") gender : String,

                             ) : Call<ProfileRes>?

    @Multipart
    @PUT(KeysConstant.UPDATE_CONNECT_PROFILE)
    fun updateConnectProfilePhoto(@Header(Authorization) token: String,@Path("audience_id")id : String,
                             @Part profile_images : List<MultipartBody.Part>
                             ) : Call<ProfileRes>?


    @GET("/{org_name}/{app_name}/users/{owner_username}/contacts/users")
    fun chatsList( @Header(Authorization) token: String ,@Path("org_name") org_name: String,@Path("app_name") app_name: String, @Path("owner_username") owner_username: String): Call<Any>?

    @GET(KeysConstant.MYPENDING_REQUEST)
    fun pendingRequests(@Header(Authorization) tokens: String): Call<PendingRequestResponse>?
    @GET("connect/acceptRequest/{connect_d}")
    fun acceptInvitation(@Header(Authorization) token: String ,@Path("connect_d") connect_d: String): Call<RequestAcceptResponse>?
    @GET("connect/rejectRequest/{connect_d}")
    fun rejectInvitation(@Header(Authorization) token: String ,@Path("connect_d") connect_d: String): Call<RequestAcceptResponse>?
    @GET("connect/sentRequest/{audienceId}")
    fun callInvitationApi(@Header(Authorization) token: String, @Path("audienceId") audienceId:  String) : Call<SendInvitationResponse>
    @POST("auth/updateAgora")
    fun getUpdateAgora(@Header(Authorization) token: String?,@Body updateAgoraBdy: UpdateAgoraModel ):Call<UpdateAgoraDetailResponse>?

/*
    @FormUrlEncoded
    @PUT("audience/update-mobile-profile/{id}")
    fun updateMobileProfile(
        @Header(Constants.Authorization) token : String,
        @Path("id") id : String,
        @Field("profile_images") profile_images: List<String>,
        @Field("description") description : String,
        @Field("occupation") occupation : String,
        @Field("gender") gender : String,
        @Field("education") education : String,
        @Field("location") location : String,
        @Field("height") height : String,
        @Field("smoking") smoking : String,
        @Field("drinking") drinking : String,
        @Field("pets") pets : String,
        @Field("zodiac_sign") zodiac_sign : String,
        @Field("religion") religion : String,
        @Field("language") language : ArrayList<String>
    )
            : Call<ProfileSuccessResponse>
*/

    @Multipart
    @PUT("audience/update-mobile-profile/{id}")
    fun updateMobileProfile(
        @Header(Constants.Authorization) token : String,
        @Path("id") id : String,
        @Part profile_images : List<MultipartBody.Part>,
        @Part("description") description : RequestBody,
        @Part("occupation") occupation : RequestBody,
        @Part("gender") gender : RequestBody,
        @Part("education") education : RequestBody,
        @Part("location") location : RequestBody,
        @Part("height") height : RequestBody,
        @Part("smoking") smoking : RequestBody,
        @Part("drinking") drinking : RequestBody,
        @Part("pets") pets : RequestBody,
        @Part("zodiac_sign") zodiac_sign : RequestBody,
        @Part("religion") religion : RequestBody,
//        @Part("language") language : RequestBody
    )
            : Call<ProfileSuccessResponse>

    @Multipart
    @PUT("audience/update-mobile-profile/{id}")
    fun updateMobileProfile2(
        @Header(Constants.Authorization) token : String,
        @Path("id") id : String,
        @Part("profile_images") profile_images : List<String>,
        @Part("description") description : RequestBody,
        @Part("occupation") occupation : RequestBody,
        @Part("gender") gender : RequestBody,
        @Part("education") education : RequestBody,
        @Part("location") location : RequestBody,
        @Part("height") height : RequestBody,
        @Part("smoking") smoking : RequestBody,
        @Part("drinking") drinking : RequestBody,
        @Part("pets") pets : RequestBody,
        @Part("zodiac_sign") zodiac_sign : RequestBody,
        @Part("religion") religion : RequestBody,
//        @Part("language") language : RequestBody
    )
            : Call<ProfileSuccessResponse>

}