package com.dev.frequenc.ui_codes.screens.ViewAllTrendingEvents

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.databinding.ActivityViewAllTrendingEventsBinding
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingEventAdapterold
import com.dev.frequenc.ui_codes.util.Constants

class ViewAllTrendingEvents : AppCompatActivity(),TrendingEventAdapterViewAll.ListAdapterListener {

    lateinit var binding : ActivityViewAllTrendingEventsBinding
    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences
    var userRegistered : Boolean = false
    var isLogin = false
    lateinit var search : String
    lateinit var trendingEventAdapterViewAll : TrendingEventAdapterViewAll
    lateinit var mList : List<TrendingEventsResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityViewAllTrendingEventsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mList = intent.getSerializableExtra("list") as List<TrendingEventsResponse>
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


        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {


            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                search = s.toString()
                if(search!=null)
                    if(mList!=null && trendingEventAdapterViewAll!=null)
                    {
                        filter(search)
                    }
//                       mlistener.searchDish(search)
            }
        })


        trendingEventAdapterViewAll = TrendingEventAdapterViewAll(mList,this@ViewAllTrendingEvents, isLogin)
        binding.rvViewAll.apply {
            layoutManager = GridLayoutManager(this@ViewAllTrendingEvents,2,
                GridLayoutManager.VERTICAL,false)
            adapter = trendingEventAdapterViewAll
        }

        binding.ivSearchAppBar.setOnClickListener {

            binding.rlSearch.visibility = View.VISIBLE
            binding.ivSearchAppBar.visibility =View.GONE

        }

        binding.ivSearch.setOnClickListener {
            binding.rlSearch.visibility = View.GONE
            binding.ivSearchAppBar.visibility =View.VISIBLE
        }

    }



    override fun onClickAtCard(item: TrendingEventsResponse) {

    }

    override fun onClickAtBookmark(item: TrendingEventsResponse) {

    }

    fun filter(text: String) {
        val temp: ArrayList<TrendingEventsResponse> = java.util.ArrayList()
        for (d in mList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.eventTitle!!.contains(text, ignoreCase = true)

                ) {

                temp.add(d)
            }
        }
        //update recyclerview
        if(trendingEventAdapterViewAll!=null) {
            trendingEventAdapterViewAll!!.updateList(temp)
        }
    }



}