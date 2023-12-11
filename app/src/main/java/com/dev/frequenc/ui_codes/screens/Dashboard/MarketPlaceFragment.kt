package com.dev.frequenc.ui_codes.screens.Dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.AllDataResponse
import com.dev.frequenc.ui_codes.data.BookmarkEventResponse
import com.dev.frequenc.ui_codes.data.BrowseByCatResponse
import com.dev.frequenc.ui_codes.data.TrendingArtistResponse
import com.dev.frequenc.ui_codes.data.TrendingArtistTokenResponse
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.ui_codes.data.TrendingEventsTokenResponse
import com.dev.frequenc.ui_codes.data.req.SavedEventsReq
import com.dev.frequenc.databinding.FragmentMarketPlaceBinding
import com.dev.frequenc.ui_codes.screens.Search.SearchActivity
import com.dev.frequenc.ui_codes.screens.ViewAllAll.ViewAllAllActivity
import com.dev.frequenc.ui_codes.screens.ViewAllCategory.ViewAllCategoryActivity
import com.dev.frequenc.ui_codes.screens.ViewAllTrendingArtist.ViewAllTrendingArtistActivity
import com.dev.frequenc.ui_codes.screens.ViewAllTrendingEvents.ViewAllTrendingEvents
import com.dev.frequenc.ui_codes.screens.login.LoginActivity
import com.dev.frequenc.ui_codes.screens.notification.NotificationActivity
import com.dev.frequenc.ui_codes.screens.Adapter.AllDataAdapter
import com.dev.frequenc.ui_codes.screens.Adapter.CategoryAdapter
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingArtistAdapter
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingEventAdapter
import com.dev.frequenc.ui_codes.screens.ArtistDetail.ArtistDetailsActivity
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.screens.intro.IntroduceYourselfActivity
import com.dev.frequenc.ui_codes.screens.intro.PresentationActivity
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.ui_codes.util.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.Locale

class MarketPlaceFragment : Fragment(), TrendingArtistAdapter.ListAdapterListener, TrendingEventAdapter.ListAdapterListener,
               AllDataAdapter.ListAdapterListener, CategoryAdapter.ListAdapterListerner {

    lateinit var ivNotification: ImageView
    lateinit var root : View
    lateinit var gifImageView : GifImageView
    lateinit var rvCategory : RecyclerView
    lateinit var rvTrendingEvents : RecyclerView
    lateinit var rvTrendingArtist : RecyclerView
    lateinit var rvAllData : RecyclerView
    lateinit var progressDialog : ImageView
    lateinit var mContext: Context
    lateinit var tvLocation: TextView
    lateinit var rlSearch: RelativeLayout
    lateinit var loginBtn : TextView
    lateinit var fusedLocationClient : FusedLocationProviderClient
    lateinit var trendingEventList : List<TrendingEventsResponse>
    lateinit var trendingArtistList : List<TrendingArtistResponse>
    lateinit var allDataList : List<AllDataResponse>

    lateinit var tvViewAllTrendingEvents : TextView
    lateinit var tvViewAllTrendingArtist : TextView
    lateinit var tvViewAllAll : TextView
    lateinit var rlLogin : RelativeLayout

    lateinit var rlLocation : RelativeLayout

    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var ivHamburger : ImageView
    var userRegistered : Boolean = false

    lateinit var rlTrendingEvent : RelativeLayout
    lateinit var rlTrendingArtist : RelativeLayout
    lateinit var rlAllData : RelativeLayout

    var isLogin = false



    lateinit var binding : FragmentMarketPlaceBinding

    companion object {
        fun newInstance() = MarketPlaceFragment()
        const val LOCATION_PERMISSION_REQUEST_CODE = 101

    }

    private lateinit var viewModel: MarketPlaceViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMarketPlaceBinding.inflate(layoutInflater)

        root = inflater.inflate(R.layout.fragment_market_place, container, false)



        gifImageView = root.findViewById<GifImageView>(R.id.ivAnimSplash)

        rvCategory = root.findViewById(R.id.rvCategory)

        rvTrendingEvents = root.findViewById(R.id.rvTrendingEvents)

        rvTrendingArtist = root.findViewById(R.id.rvTrendingArtist)

        rvAllData = root.findViewById(R.id.rvAllData)

        tvLocation = root.findViewById(R.id.tvLocation)

        progressDialog = root.findViewById(R.id.progress_bar)

        rlSearch = root.findViewById(R.id.rlSearch)

        loginBtn = root.findViewById(R.id.btnLogin)

        ivNotification = root.findViewById(R.id.ivNotification)

        ivHamburger = root.findViewById(R.id.ivHamburger)

        rlLocation = root.findViewById(R.id.rlLocation)

        rlAllData = root.findViewById(R.id.rlAll)

        rlTrendingArtist = root.findViewById(R.id.rlTrendingArtist)

        rlTrendingEvent = root.findViewById(R.id.rlTrendingEvents)

        tvViewAllTrendingArtist = root.findViewById(R.id.tvTrendingArtistViewAll)
        tvViewAllTrendingEvents = root.findViewById(R.id.tvTrendingEventsViewAll)

        tvViewAllAll = root.findViewById(R.id.tvAllViewAll)
        rlLogin = root.findViewById(R.id.llLogin)

        rlSearch.setOnClickListener{
            val intent = Intent(requireContext(),SearchActivity::class.java)
            startActivity(intent)
        }

        rlLocation.setOnClickListener {
            val intent = Intent(requireContext(),PresentationActivity::class.java)
//            val intent = Intent(requireContext(),IntroduceYourselfActivity::class.java)
            startActivity(intent)
        }

        Glide.with(requireContext()).asGif() .load(R.drawable.frequenc_loader).
             into(progressDialog)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        )
        {

            requestPermissions(arrayOf( android.Manifest.permission.ACCESS_COARSE_LOCATION
                ,android.Manifest.permission.ACCESS_FINE_LOCATION),LOCATION_PERMISSION_REQUEST_CODE)

            //request permission

        }
        else
        {
            //do action
            GetLocation()
        }


        Glide.with(this)
            .asGif()
            .load(R.drawable.frequenc_loader) // Replace with the actual resource ID of your GIF
            .into(gifImageView)

//        gifImageView.setOnClickListener{
//        }

        ivHamburger.setOnClickListener {
            (activity as MainActivity).binding.drawerLayout.openDrawer(GravityCompat.END)

        }

        loginBtn.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        ivNotification.setOnClickListener {
//            val intent = Intent(activity,  NotificationActivity::class.java)
//            startActivity(intent)
        }
        sharedPreferences = activity?.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!

         userRegistered = sharedPreferences.getBoolean(Constants.isUserTypeRegistered, false)

        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId,"-1").toString()

        if(userRegistered && !authorization.isNullOrEmpty() &&authorization!="-1" && !audience_id.isNullOrEmpty() )
        {

            Log.d("Audience Id",audience_id)
            Log.d("Bearer",authorization)
//            Toast.makeText(requireContext(),"Login Success", Toast.LENGTH_SHORT).show()
            loginBtn.visibility = View.INVISIBLE
            rlLogin.visibility = View.INVISIBLE
            ivHamburger.visibility =View.VISIBLE
            ivNotification.visibility =View.VISIBLE

            trendingEventTokenApi()
            trendingArtistTokenApi()
            isLogin = true

            (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE


        }
        else
        {
//            Toast.makeText(requireContext(),"Not Logged in Failure", Toast.LENGTH_SHORT).show()
            Log.e("Audience Id",audience_id)
            loginBtn.visibility = View.VISIBLE
            rlLogin.visibility = View.VISIBLE
            ivHamburger.visibility =View.INVISIBLE
            ivNotification.visibility =View.INVISIBLE

            trendingEventApi()

            trendingArtistApi()

            isLogin =false

            (activity as MainActivity).binding.bottomNavigationView.visibility = View.GONE

//            Log.e("Bearer",authorization)
        }



        browseByCatApi()

        allDataApi()

        return root
    }



    private fun allDataApi() {
        progressDialog.visibility = View.VISIBLE

        ApiClient.getInstance()!!.allData()!!.enqueue(object : Callback<List<AllDataResponse>>{
            override fun onResponse(
                call: Call<List<AllDataResponse>>,
                response: Response<List<AllDataResponse>>
            ) {


                progressDialog.visibility = View.GONE
                if (response.isSuccessful() && response.body() != null
                ) {

                    rlAllData.visibility = View.VISIBLE

                    Log.d(Constants.ApiResponse, "onResponse Retrofit All Data: " + response.body())
                    val res = response.body()

                    allDataList =res!!

                    for(i in res!!.indices)
                    {
                        Log.d(Constants.ApiResponse, " Body : ${res[i]!!.title}")

                    }

                    tvViewAllAll.setOnClickListener {
                        val intent = Intent(requireContext(), ViewAllAllActivity::class.java)
                        intent.putExtra("list",allDataList as Serializable)
                        startActivity(intent)
                    }

                    rvAllData.apply {
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                        adapter = AllDataAdapter(res,this@MarketPlaceFragment)
                    }


                }

            }

            override fun onFailure(call: Call<List<AllDataResponse>>, t: Throwable) {
                progressDialog.visibility = View.GONE
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)
            }

        })

    }

    private fun trendingArtistApi() {

        progressDialog.visibility = View.VISIBLE
        ApiClient.getInstance()!!.trendingArtist()!!.enqueue(object : Callback<List<TrendingArtistResponse>>{
            override fun onResponse(
                call: Call<List<TrendingArtistResponse>>,
                response: Response<List<TrendingArtistResponse>>
            ) {


                progressDialog.visibility = View.GONE
                if (response.isSuccessful() && response.body() != null
                ) {
                    rlTrendingArtist.visibility = View.VISIBLE

                    Log.d(Constants.ApiError, "onResponse Retrofit: " + response.body())

                    val res = response.body()

                    trendingArtistList = res!!

                    for(i in res!!.indices)
                    {
                        Log.d(Constants.ApiError, " Body : ${res[i]!!.artist_name}")

                    }

                    tvViewAllTrendingArtist.setOnClickListener {
                        val intent = Intent(requireContext(), ViewAllTrendingArtistActivity::class.java)
                        intent.putExtra("list",trendingArtistList as Serializable)
                        startActivity(intent)
                    }

                    rvTrendingArtist.apply {
                        layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
                        adapter = TrendingArtistAdapter(res,this@MarketPlaceFragment)
                    }


                }
                else {
                    hideTrendingArtistLay()
                }
            }

            override fun onFailure(call: Call<List<TrendingArtistResponse>>, t: Throwable) {

                progressDialog.visibility = View.GONE
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)

            }

        })

    }

    private fun hideTrendingArtistLay() {
//        bind
    }

    private fun trendingArtistTokenApi() {

        progressDialog.visibility = View.VISIBLE
        ApiClient.getInstance()!!.trendingArtistbyToken(authorization)!!.enqueue(object : Callback<TrendingArtistTokenResponse>{
            override fun onResponse(
                call: Call<TrendingArtistTokenResponse>,
                response: Response<TrendingArtistTokenResponse>
            ) {
                progressDialog.visibility = View.GONE
                if (response.isSuccessful() && response.body() != null
                ) {

                    rlTrendingArtist.visibility = View.VISIBLE
                    Log.d(Constants.ApiResponse, "onResponse Retrofit: " + response.body())

                    val res2 = response.body()

                    val res = res2!!.event

                    trendingArtistList = res!!

                    for(i in res!!.indices)
                    {
                        Log.d(Constants.ApiError, " Body : ${res[i]!!.artist_name}")

                    }

                    tvViewAllTrendingArtist.setOnClickListener {
                        val intent = Intent(requireContext(), ViewAllTrendingArtistActivity::class.java)
                        intent.putExtra("list",trendingArtistList as Serializable)
                        startActivity(intent)
                    }

                    rvTrendingArtist.apply {
                        layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
                        adapter = TrendingArtistAdapter(res,this@MarketPlaceFragment)
                    }


                }
            }

            override fun onFailure(call: Call<TrendingArtistTokenResponse>, t: Throwable) {

                progressDialog.visibility = View.GONE
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)

            }

        })

    }

    private fun trendingEventApi() {
        progressDialog.visibility = View.VISIBLE

        ApiClient.getInstance()!!.trendingEvents()!!.enqueue(object : Callback<List<TrendingEventsResponse>> {
            override fun onResponse(
                call: Call<List<TrendingEventsResponse>>,
                response: Response<List<TrendingEventsResponse>>
            ) {


                progressDialog.visibility = View.GONE

                if (response.isSuccessful() && response.body() != null
                ) {

                    rlTrendingEvent.visibility = View.VISIBLE

                    Log.d(Constants.ApiResponse, "onResponse Retrofit: " + response.body())

                    val res = response.body()

                    trendingEventList = res!!

                    tvViewAllTrendingEvents.setOnClickListener {
                        val intent = Intent(requireContext(),ViewAllTrendingEvents::class.java)
                        intent.putExtra("list",trendingEventList as Serializable)
                        startActivity(intent)
                    }

                    for(i in res!!.indices)
                    {
                        Log.d(Constants.ApiResponse, " Body : ${res[i]!!.eventTitle}")

                    }

                    rvTrendingEvents.apply {
                        layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
                        adapter = TrendingEventAdapter(res,this@MarketPlaceFragment,isLogin)
                    }


                } else {
//                    genericTypeResponse.postValue(null)
                    //                        if (response.code() )\
                }
            }

            override fun onFailure(call: Call<List<TrendingEventsResponse>>, t: Throwable) {
//                setIsLoading(false)
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)
                progressDialog.visibility = View.GONE

            }
        })

    }

    private fun trendingEventTokenApi() {
        progressDialog.visibility = View.VISIBLE


        ApiClient.getInstance()!!.trendingEventsbyToken(authorization)!!.enqueue(object : Callback<TrendingEventsTokenResponse> {
            override fun onResponse(
                call: Call<TrendingEventsTokenResponse>,
                response: Response<TrendingEventsTokenResponse>
            ) {


                progressDialog.visibility = View.GONE

                if (response.isSuccessful() && response.body() != null
                ) {
                    rlTrendingEvent.visibility = View.VISIBLE
                    Log.d(Constants.ApiResponse, "onResponse Retrofit: " + response.body())

                    val res2 = response.body()

                    val res =res2!!.event

                    trendingEventList = res

                    tvViewAllTrendingEvents.setOnClickListener {
                        val intent = Intent(requireContext(),ViewAllTrendingEvents::class.java)
                        intent.putExtra("list",trendingEventList as Serializable)
                        startActivity(intent)
                    }

                    for(i in res.indices)
                    {
                        Log.d(Constants.ApiResponse, " Body : ${res[i]!!.eventTitle}")

                    }

                    rvTrendingEvents.apply {
                        layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
                        adapter = TrendingEventAdapter(res,this@MarketPlaceFragment,isLogin)
                    }


                } else {
//                    genericTypeResponse.postValue(null)
                    //                        if (response.code() )\
                }
            }

            override fun onFailure(call: Call<TrendingEventsTokenResponse>, t: Throwable) {
//                setIsLoading(false)
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)
                progressDialog.visibility = View.GONE

            }
        })

    }

    private fun browseByCatApi() {
        progressDialog.visibility = View.VISIBLE

        ApiClient.getInstance()!!.browseByCat()!!.enqueue(object : Callback<List<BrowseByCatResponse?>> {
            override fun onResponse(
                call: Call<List<BrowseByCatResponse?>>,
                response: Response<List<BrowseByCatResponse?>>
            ) {

                Log.d(Constants.ApiResponse, "on Api Hit: " + response.body().toString())

                if (response.isSuccessful() && response.body() != null
                ) {
                    Log.d(Constants.ApiError, "onResponse Retrofit: " + response.body())

                    val res = response.body()

                    for(i in res!!.indices)
                    {
                        Log.d(Constants.ApiResponse, " Body : ${res[i]!!.name}")

                    }



                    rvCategory.apply {
                        layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
                        adapter = CategoryAdapter(res,this@MarketPlaceFragment)
                    }


                } else {
//                    genericTypeResponse.postValue(null)
                    //                        if (response.code() )\
                }
            }

            override fun onFailure(call: Call<List<BrowseByCatResponse?>>, t: Throwable) {
//                setIsLoading(false)
                Log.d(Constants.ApiError, "onFailure Retrofit: ", t)
                progressDialog.visibility = View.GONE

            }
        })

    }

    private fun bookmarkApi(item: TrendingEventsResponse)
    {
        Log.e("eventId",item._id)

        ApiClient.getInstance()!!.bookmarkEvent(authorization,SavedEventsReq(
            item._id,
            true
        )
        ).enqueue(object: retrofit2.Callback<BookmarkEventResponse>{
            override fun onResponse(
                call: Call<BookmarkEventResponse>,
                response: Response<BookmarkEventResponse>
            ) {
                if(response.isSuccessful && response.body()!=null)
                {
                    Toast.makeText(requireContext(),"BookMark !!!",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookmarkEventResponse>, t: Throwable) {

            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MarketPlaceViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onClickAtArtist(item: TrendingArtistResponse) {
//        Toast.makeText(mContext,"Artist Clicked",Toast.LENGTH_SHORT).show()

        val bundle = Bundle()
        val intent = Intent(requireContext(),ArtistDetailsActivity::class.java)
        bundle.putString("artist_id",item._id)
        intent.putExtras(bundle)
        startActivity(intent)

    }

    override fun onClickAtCard(item: TrendingEventsResponse) {
//        Toast.makeText(mContext,"Event Clicked",Toast.LENGTH_SHORT).show()

        val intent = Intent(mContext,EventDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString("eventid",item._id)
        Log.d("eventid",item._id)

        intent.putExtras(bundle)
        startActivity(intent)

    }

    override fun onClickAtBookmark(item: TrendingEventsResponse) {
//        Toast.makeText(requireContext(),"Bookmark",Toast.LENGTH_SHORT).show()
        if (userRegistered && !authorization.isNullOrEmpty() && !audience_id.isNullOrEmpty())
        {
            Log.d("bookmark","Bookmark started")

            bookmarkApi(item)
        }
        else
        {
            Toast.makeText(requireContext(),"Login Required to Bookmark Event",Toast.LENGTH_SHORT).show()
        }
    }

    private fun GetLocation()
    {

        if(ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED)
        {
            return
        }


        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->

            if(location!=null)
            {

                getCityName(location.latitude,location.longitude)
            }

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE)
        {
            if( grantResults.isNotEmpty() )
                if(  grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                )
                {
                    GetLocation()
                }
        }
    }


    private fun getCityName(lat : Double, long: Double)
    {
        try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val myAddress = geocoder.getFromLocation(lat,long,3)


            rlLocation.visibility =View.VISIBLE

            tvLocation.text = myAddress?.get(0)!!.locality + ", " + myAddress[0].countryName

        }
        catch (e:Exception)
        {
            //handle exception
        }
    }

    override fun onClickAtEvent(item: AllDataResponse) {

        val intent = Intent(requireContext(),EventDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString("eventid",item.eventid)
        intent.putExtras(bundle)
        startActivity(intent)

    }

    override fun onClickAtCategory(item: BrowseByCatResponse) {

        if(item.eventCount.toInt()>0)
        {
            val bundle = Bundle()
            bundle.putString("category",item.name)
            val intent = Intent(requireContext(),ViewAllCategoryActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        else
        {
            Toast.makeText(requireContext(),"No events to show",Toast.LENGTH_SHORT).show()
        }


    }


}