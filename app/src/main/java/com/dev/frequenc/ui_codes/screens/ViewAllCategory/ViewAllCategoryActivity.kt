package com.dev.frequenc.ui_codes.screens.ViewAllCategory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.databinding.ActivityViewAllCategoryBinding
import com.dev.frequenc.ui_codes.screens.ViewAllTrendingEvents.ViewAllTrendingEvents
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingEventAdapter
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class ViewAllCategoryActivity : AppCompatActivity(), TrendingEventAdapter.ListAdapterListener {

    lateinit var binding : ActivityViewAllCategoryBinding
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
                        adapter = TrendingEventAdapter(res,this@ViewAllCategoryActivity)
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