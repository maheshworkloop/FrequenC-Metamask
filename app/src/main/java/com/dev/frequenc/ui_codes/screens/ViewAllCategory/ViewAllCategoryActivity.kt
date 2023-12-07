package com.dev.frequenc.ui_codes.screens.ViewAllCategory

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.databinding.ActivityViewAllCategoryBinding
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingEventAdapter
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity
import com.dev.frequenc.ui_codes.screens.ViewAllTrendingEvents.TrendingEventAdapterViewAll
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.ui_codes.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllCategoryActivity : AppCompatActivity(), TrendingEventAdapterViewAll.ListAdapterListener {

    lateinit var binding : ActivityViewAllCategoryBinding
    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences
    var userRegistered : Boolean = false
    var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewAllCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val bundle = intent.extras
        if(bundle!=null)
        {
            val category = bundle.getString("category").toString()

            binding.tvCategory.text = category
            browseByCatDetailsApi(category)
        }

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




    }

    private fun browseByCatDetailsApi(category : String) {
        binding.progressDialog.visibility = View.VISIBLE

        ApiClient.getInstance()!!.browseByCatDetail(category)!!.enqueue(object :
            Callback<List<TrendingEventsResponse>> {
            override fun onResponse(
                call: Call<List<TrendingEventsResponse>>,
                response: Response<List<TrendingEventsResponse>>
            ) {


                binding.progressDialog.visibility = View.GONE

                if (response.isSuccessful() && response.body() != null
                ) {
                    Log.d(Constants.ApiResponse, "onResponse Retrofit: " + response.body())

                    val res = response.body()

                    for(i in res!!.indices)
                    {
                        Log.d(Constants.ApiResponse, " Body : ${res[i]!!.eventTitle}")

                    }

                    binding.rvViewAll.apply {
                        layoutManager = GridLayoutManager(this@ViewAllCategoryActivity,2,
                            GridLayoutManager.VERTICAL,false)
                        adapter = TrendingEventAdapterViewAll(res,this@ViewAllCategoryActivity,isLogin)
                    }


                } else {
//                    genericTypeResponse.postValue(null)
                    //                        if (response.code() )\
                }
            }

            override fun onFailure(call: Call<List<TrendingEventsResponse>>, t: Throwable) {
//                setIsLoading(false)
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)
                binding.progressDialog.visibility = View.GONE

            }
        })

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