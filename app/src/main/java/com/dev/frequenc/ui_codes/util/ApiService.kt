package com.dev.frequenc.ui_codes.util

import com.dev.frequenc.ui_codes.data.SearchResponse
import com.dev.frequenc.ui_codes.data.RegisterUserResponse
import com.dev.frequenc.ui_codes.data.confirmuserotp.ConfirmUserOtpResponse
import com.dev.frequenc.ui_codes.data.confirmuserotp.UpdateUserTypeResponse
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
import com.dev.frequenc.ui_codes.data.ProfileSuccessResponse
import com.dev.frequenc.ui_codes.data.StateResponse
import com.dev.frequenc.ui_codes.data.TrendingArtistResponse
import com.dev.frequenc.ui_codes.data.TrendingArtistTokenResponse
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.ui_codes.data.TrendingEventsTokenResponse
import com.dev.frequenc.ui_codes.data.UpcomingEventResponse
import com.dev.frequenc.ui_codes.data.VenueDetailsResponse
import com.dev.frequenc.ui_codes.data.VibeEventResponse
import com.dev.frequenc.ui_codes.data.WalletBalenceData
import com.dev.frequenc.ui_codes.data.models.paymentInitiateReq
import com.dev.frequenc.ui_codes.data.myconnection.MyConnectionResponse
import com.dev.frequenc.ui_codes.data.myrequests.MyRequestsResponse
import com.dev.frequenc.ui_codes.data.notification.NotificationResponse
import com.dev.frequenc.ui_codes.data.req.SavedEventsReq
import com.dev.frequenc.ui_codes.data.saved_event.SavedEventResponse
import com.dev.frequenc.ui_codes.data.transactionlist.TransactionListRes
import com.dev.frequenc.ui_codes.screens.utils.KeysConstant
import com.dev.frequenc.util.Constants
import com.dev.frequenc.util.Constants.Companion.Authorization
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {



    @POST(KeysConstant.Register_User)
    fun register(@Body phone_no: LoginViewModel.registerUserReq ): Call<RegisterUserResponse>?

    @POST(KeysConstant.ConfirmUserOtp)
    fun confirmUserOtp(@Body verifyOtpReq: LoginViewModel.VerifyOtpReq) : Call<ConfirmUserOtpResponse>?

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
    fun getProfile(@Header(Constants.Authorization) tokens: String , @Path("id") id: String): Call<AudienceDataResponse>

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

}