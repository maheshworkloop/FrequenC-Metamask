package com.dev.frequenc.ui_codes.screens.VenueDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityVenueDetailBinding
import com.dev.frequenc.ui_codes.data.VenueDetailsResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.ui_codes.util.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Response

class VenueDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var id: String
    lateinit var binding : ActivityVenueDetailBinding
    var flagFaq : Boolean = false
    var flagTerms : Boolean = false
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVenueDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.ivBackBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val bundle = intent.extras

        if (bundle != null) {
            id = bundle.getString("venue_id").toString()
            venueDetailApi(id)
        }


        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)


        binding.cvFaq.setOnClickListener {

            flagFaq = !flagFaq

            if(flagFaq)
                binding.tvFaqIcon.setImageDrawable(getDrawable(R.drawable.minus))
            else
                binding.tvFaqIcon.setImageDrawable(getDrawable(R.drawable.plus))

        }

        binding.cvTerms.setOnClickListener {

            flagTerms = !flagTerms

            if(flagTerms)
                binding.tvTermsConditionIcon.setImageDrawable(getDrawable(R.drawable.minus))
            else
                binding.tvTermsConditionIcon.setImageDrawable(getDrawable(R.drawable.plus))

        }

    } //end of Create


    private fun venueDetailApi(id: String) {

        binding.progressBar.visibility = View.VISIBLE

        ApiClient.getInstance()!!.venueDetailsById(id)!!.enqueue(object : retrofit2.Callback<VenueDetailsResponse>{
            override fun onResponse(
                call: Call<VenueDetailsResponse>,
                response: Response<VenueDetailsResponse>
            ) {
                binding.progressBar.visibility = View.GONE

                if(response.isSuccessful() && response.body() != null) {
                    val venueResponse = response.body()
                    Log.d(Constants.ApiResponse, "onVenue Response: " + response.body())

                    Glide.with(this@VenueDetailActivity).load(venueResponse!!.banner_pic).into(binding.ivBanner)

                    Glide.with(this@VenueDetailActivity).load(venueResponse.profile_pic).into(binding.ivProfile)

                    binding.tvVenuName.text = venueResponse.venue_name

                    binding.tvDescription.text = venueResponse.description

                    if(!venueResponse.address.isNullOrEmpty())
                     binding.tvPropertyLocation.text = venueResponse.address
                }
            }

            override fun onFailure(call: Call<VenueDetailsResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)

            }

        })

       }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            val location = LatLng(37.7749, -122.4194) // San Francisco coordinates
            addMarker(MarkerOptions().position(location).title("Marker in San Francisco"))
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

}