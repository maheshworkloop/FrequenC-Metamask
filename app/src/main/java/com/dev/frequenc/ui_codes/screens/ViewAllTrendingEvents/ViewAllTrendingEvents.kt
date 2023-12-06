package com.dev.frequenc.ui_codes.screens.ViewAllTrendingEvents

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.databinding.ActivityViewAllTrendingEventsBinding
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingEventAdapterold
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity
import com.dev.frequenc.ui_codes.util.Constants

class ViewAllTrendingEvents : AppCompatActivity(),TrendingEventAdapterold.ListAdapterListener {

    lateinit var binding : ActivityViewAllTrendingEventsBinding
    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences
    var userRegistered : Boolean = false
    var isLogin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityViewAllTrendingEventsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val mList = intent.getSerializableExtra("list") as List<TrendingEventsResponse>

        binding.ivBackBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        sharedPreferences = getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!
        userRegistered = sharedPreferences.getBoolean(Constants.isUserTypeRegistered, false)
        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId,"-1").toString()

        if(userRegistered && !authorization.isNullOrEmpty() &&authorization!="-1" && !audience_id.isNullOrEmpty() )
        {

            Log.d("Audience Id",audience_id)
            Log.d("Bearer",authorization)

            isLogin =true

        }
        else
        {
            Log.e("Audience Id",audience_id)
            isLogin = false
        }



        binding.rvViewAll.apply {
            layoutManager = GridLayoutManager(this@ViewAllTrendingEvents,2,
                GridLayoutManager.VERTICAL,false)
            adapter = TrendingEventAdapterold(mList,this@ViewAllTrendingEvents, isLogin)
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