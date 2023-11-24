package com.dev.frequenc.ui_codes.screens.ViewAllTrendingEvents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.frequenc.MainActivity
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.databinding.ActivityViewAllTrendingEventsBinding
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingArtistAdapter
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingEventAdapter
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllTrendingEvents : AppCompatActivity(),TrendingEventAdapter.ListAdapterListener {

    lateinit var binding : ActivityViewAllTrendingEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityViewAllTrendingEventsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val mList = intent.getSerializableExtra("list") as List<TrendingEventsResponse>

        binding.ivBackBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.rvViewAll.apply {
            layoutManager = GridLayoutManager(this@ViewAllTrendingEvents,2,
                GridLayoutManager.VERTICAL,false)
            adapter = TrendingEventAdapter(mList,this@ViewAllTrendingEvents)
        }

    }



    override fun onClickAtCard(item: TrendingEventsResponse) {
        val intent = Intent(this, EventDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString("eventid",item._id)
        Log.d("eventid",item._id)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onClickAtBookmark(item: TrendingEventsResponse) {

    }


}