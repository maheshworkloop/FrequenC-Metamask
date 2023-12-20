package com.dev.frequenc.ui_codes.screens.ViewAllTrendingArtist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.frequenc.ui_codes.data.TrendingArtistResponse
import com.dev.frequenc.databinding.ActivityViewAllTrendingArtistBinding
import com.dev.frequenc.databinding.ActivityViewAllTrendingEventsBinding
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingArtistAdapter

class ViewAllTrendingArtistActivity : AppCompatActivity(),TrendingArtistAdapter.ListAdapterListener {

    lateinit var binding : ActivityViewAllTrendingArtistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewAllTrendingArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBackBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val mList = intent.getSerializableExtra("list") as List<TrendingArtistResponse>


        binding.rvViewAll.apply {
            layoutManager = GridLayoutManager(this@ViewAllTrendingArtistActivity,2,
                GridLayoutManager.VERTICAL,false)
            adapter = TrendingArtistAdapter(mList,this@ViewAllTrendingArtistActivity)
        }

    }



    override fun onClickAtArtist(item: TrendingArtistResponse) {


    }


}