package com.dev.frequenc.ui_codes.screens.ArtistDetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dev.frequenc.databinding.ActivityArtistDetailsBinding
import com.dev.frequenc.ui_codes.screens.Adapter.UpcomingEventAdapter
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity
import com.dev.frequenc.ui_codes.data.ArtistResponse
import com.dev.frequenc.ui_codes.data.UpcomingEventResponse
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingEventAdapter
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import retrofit2.Call
import retrofit2.Response

class ArtistDetailsActivity : AppCompatActivity(), UpcomingEventNewAdapter.ListAdapterListener { //end of Activity

    lateinit var id : String

    lateinit var binding : ActivityArtistDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArtistDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.ivBackBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val bundle = intent.extras

        if(bundle!=null)
        {
            id = bundle.getString("artist_id").toString()

            Log.d("artist_id_detail",id)

            artistDetailApi(id)

            upcomingEventsByArtistIdApi(id)

        }

    } //end of Create


    private fun artistDetailApi(id : String)
    {

        binding.progressBar.visibility = View.VISIBLE

        ApiClient.getInstance()!!.getArtistById(id).enqueue(object : retrofit2.Callback<ArtistResponse>{

            override fun onResponse(
                call: Call<ArtistResponse>,
                response: Response<ArtistResponse>
            ) {
                binding.progressBar.visibility = View.GONE

                if(response.isSuccessful() && response.body() != null)
                {
                    val artistResponse = response.body()
                    Log.d(Constants.ApiResponse, "onArtist Response: " + response.body())

                    Log.d("artist_name",artistResponse!!.artist_name)

                    binding.tvDescription.text = artistResponse!!.bio_description

                    binding.tvArtistName.text = artistResponse!!.artist_name

                    Glide.with(this@ArtistDetailsActivity).load(artistResponse!!.banner_pic).into(binding.ivBanner)

                    Glide.with(this@ArtistDetailsActivity).load(artistResponse!!.profile_pic).into(binding.ivProfile)

                }

            }   //onResponse end

            override fun onFailure(call: Call<ArtistResponse>, t: Throwable) {
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)
                binding.progressBar.visibility = View.GONE
            }
        })


    }

    private fun upcomingEventsByArtistIdApi(id: String)
    {
        binding.progressBar.visibility = View.VISIBLE

        ApiClient.getInstance()!!.upcomingEventsByArtistId(id).enqueue(object : retrofit2.Callback<List<UpcomingEventResponse>>{
            override fun onResponse(
                call: Call<List<UpcomingEventResponse>>,
                response: Response<List<UpcomingEventResponse>>
            ) {
                binding.progressBar.visibility = View.GONE

                if(response.isSuccessful() && response.body() != null)
                {
                    val res = response.body()
                    Log.d(Constants.ApiResponse, "onupcomingEventsByArtist Response: " + response.body())

                    for(i in res!!.indices)
                    {
                        Log.d(Constants.ApiResponse, "upcomingEventsByArtist : ${res[i]!!.eventTitle}")

                    }

                    binding.rvUpcomingEvents.apply {
                        layoutManager = LinearLayoutManager(this@ArtistDetailsActivity, LinearLayoutManager.HORIZONTAL,false)
                        adapter = UpcomingEventNewAdapter(res,this@ArtistDetailsActivity,false)
                    }



//                    binding.rvUpcomingEvents.addItemDecoration(SimpleDividerItemDecoration(this@ArtistDetailsActivity, getResources().getDrawable(R.drawable.line_divider) ))




                }


            }

            override fun onFailure(call: Call<List<UpcomingEventResponse>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)


            }
        })

    }

    override fun onClickAtEvent(item: UpcomingEventResponse) {

        val intent = Intent(this, EventDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString("eventid",item._id)
        Log.d("eventid",item._id)

        intent.putExtras(bundle)
        startActivity(intent)

    }

}