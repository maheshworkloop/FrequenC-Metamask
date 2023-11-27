package com.dev.frequenc.ui_codes.screens.Search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.frequenc.ui_codes.screens.Adapter.SearchAdapter
import com.dev.frequenc.ui_codes.data.SearchResponse
import com.dev.frequenc.databinding.ActivitySearchBinding
import com.dev.frequenc.ui_codes.screens.ArtistDetail.ArtistDetailsActivity
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity
import com.dev.frequenc.ui_codes.screens.VenueDetail.VenueDetailActivity
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import retrofit2.Call
import retrofit2.Response

class SearchActivity : AppCompatActivity(),SearchAdapter.ListAdapterLister {

    lateinit var binding : ActivitySearchBinding
    lateinit var mlist : MutableList<SearchResponse>
    lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        binding.ivBackBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if((s.toString().isNullOrBlank() || s.toString().isNullOrEmpty()))
                {
                    mlist.clear()
                    binding.rvSearch.adapter!!.notifyDataSetChanged()
                }
                else
                {
                    SearchApi(s.toString())

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {


            }
        })



    } // emd of onCreate


    private fun SearchApi(str : String)
    {
        Log.d("search",str.toString())

        binding.progressBar.visibility = View.VISIBLE

        ApiClient.getInstance()!!.searchEvent(str.toString()).enqueue(object :
            retrofit2.Callback<MutableList<SearchResponse>>{
            override fun onResponse(
                call: Call<MutableList<SearchResponse>>,
                response: Response<MutableList<SearchResponse>>
            ) {
                binding.progressBar.visibility = View.GONE

                if(response.isSuccessful && response.body()!=null)
                {
                    Log.d(Constants.ApiError, "onResponse Retrofit Search : " + response.body())

                    val res = response.body()
                    mlist = res!!
                    for( i in res!!)
                    {
                        Log.d("title",i.title)
                    }

                    if(!(binding.etSearch.text.toString().isNullOrEmpty() || binding.etSearch.text.toString().isNullOrEmpty()) )
                    {
                        binding.rvSearch.apply {
                            layoutManager = LinearLayoutManager(this@SearchActivity,LinearLayoutManager.VERTICAL,false)
                            adapter = SearchAdapter(mlist,this@SearchActivity)
                        }
                    }



                }
            }

            override fun onFailure(call: Call<MutableList<SearchResponse>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE

            }

        })
    }

    override fun onClick(item: SearchResponse) {
//        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
        if(item.type.equals("event"))
        {
            val intent = Intent(this, EventDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString("eventid",item.eventid)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        else if(item.type.equals("venue"))
        {
            val intent = Intent(this, VenueDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString("venue_id", item.id)
            Log.d("venue_id", item.id)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        else if(item.type.equals("artist"))
        {
            val bundle = Bundle()
            val intent = Intent(this, ArtistDetailsActivity::class.java)
            bundle.putString("artist_id",item.id)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

}