package com.dev.frequenc.ui_codes.screens.PaymentDetail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.AudienceDataResponse
import com.dev.frequenc.ui_codes.data.EventResponse
import com.dev.frequenc.ui_codes.data.EventTicket
import com.dev.frequenc.databinding.ActivityPaymentDetailBinding
import com.dev.frequenc.ui_codes.screens.Profile.AudienceProfileActivity
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.AppCommonMethods
import com.dev.frequenc.util.Constants
import retrofit2.Call
import retrofit2.Response
import java.io.Serializable

class PaymentDetailActivity : AppCompatActivity() {

    lateinit var binding : ActivityPaymentDetailBinding

    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!

        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId,"-1").toString()


        val eventDetails = intent.getSerializableExtra("eventDetail") as EventResponse

        val item = intent.getSerializableExtra("item") as EventTicket

        val count = intent.getStringExtra("count")

        binding.ivBackBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        Glide.with(this).load(eventDetails.eventDetails.eventImage[0]).into(binding.ivEvent)

        binding.tvLocation.text = eventDetails.venueDetails.venue_locality

        binding.tvEventName.text = eventDetails.eventDetails.eventTitle

        var date = eventDetails.eventDetails.eventStartDate

        binding.tvCalendar.text =  AppCommonMethods.convertDateFormat2(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","dd MMM yyyy",date)

        binding.tvTicketDes.text = "$count Tickets (${item.ticket_type})"

        binding.tvAmount.text = "FRQ ${count!!.toInt()* item.price}"
        binding.tvTotal.text = "FRQ ${count!!.toInt()* item.price}"

        getProfileApi()

    }


    private fun getProfileApi()
    {
        binding.progressDialog.visibility = View.VISIBLE

        ApiClient.getInstance()!!.getProfile(authorization,audience_id).enqueue(object  : retrofit2.Callback<AudienceDataResponse>
        {
            override fun onResponse(
                call: Call<AudienceDataResponse>,
                response: Response<AudienceDataResponse>
            ) {
                binding.progressDialog.visibility = View.GONE
                if(response.isSuccessful && response.body()!=null)
                {
                    Log.d("Profile Api", "onResponse Retrofit Profile Data: " + response.body())
                    val res = response.body()

                    var item : AudienceDataResponse = res!!

                    binding.etName.editText!!.setText(res!!.fullName)
                    binding.etEmail.editText!!.setText(res!!.email)
                    binding.etMobile.editText!!.setText(res!!.mobile_no)


                }
            }

            override fun onFailure(call: Call<AudienceDataResponse>, t: Throwable) {
                binding.progressDialog.visibility = View.GONE
                Log.d("Profile Api", "onFailure Retrofit: " + t.localizedMessage)


            }

        })

    }


}