package com.dev.frequenc.ui_codes.screens.VenueDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.dev.frequenc.databinding.ActivityVenueDetailBinding
import com.dev.frequenc.ui_codes.data.VenueDetailsResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import retrofit2.Call
import retrofit2.Response

class VenueDetailActivity : AppCompatActivity() {
    lateinit var id: String
    lateinit var binding : ActivityVenueDetailBinding

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
                }
            }

            override fun onFailure(call: Call<VenueDetailsResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)

            }

        })

       }

}