package com.dev.frequenc.ui_codes.screens.ViewAllAll

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.frequenc.ui_codes.data.AllDataResponse
import com.dev.frequenc.databinding.ActivityViewAllAllBinding
import com.dev.frequenc.ui_codes.screens.Adapter.AllDataAdapter
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity

class ViewAllAllActivity : AppCompatActivity(),AllDataAdapter.ListAdapterListener {

    lateinit var binding : ActivityViewAllAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewAllAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = intent.getSerializableExtra("list") as List<AllDataResponse>

        binding.rvViewAll.apply {
            layoutManager = GridLayoutManager(this@ViewAllAllActivity,2,
                GridLayoutManager.VERTICAL,false )
            adapter = AllDataAdapter(list,this@ViewAllAllActivity)
        }


    }

    override fun onClickAtEvent(item: AllDataResponse) {
        val intent = Intent(this, EventDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString("eventid",item.eventid)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}