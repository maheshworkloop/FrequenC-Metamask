package com.dev.frequenc.ui_codes.screens.SelectTicket

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.frequenc.ui_codes.data.EventResponse
import com.dev.frequenc.ui_codes.data.EventTicket
import com.dev.frequenc.databinding.ActivitySelectTicketBinding
import com.dev.frequenc.ui_codes.screens.PaymentDetail.PaymentDetailActivity
import com.dev.frequenc.ui_codes.screens.login.LoginActivity
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity
import com.dev.frequenc.util.Constants
import java.io.Serializable

class SelectTicketActivity : AppCompatActivity(),SelectTicketNewAdapter.ListAdapterListener {

//    private lateinit var selectedDate: String
    lateinit var binding : ActivitySelectTicketBinding
    lateinit var item : EventTicket
    lateinit var list : List<EventTicket>
    lateinit var eventDetail : EventResponse
    var userRegistered : Boolean = false

    var price = 0
    var count = "1"

    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        item = intent.getSerializableExtra("item") as EventTicket
        list = intent.getSerializableExtra("list") as List<EventTicket>
        eventDetail = intent.getSerializableExtra("eventDetail") as EventResponse
//        selectedDate = intent.getSerializableExtra("date") as String

        price = item.price.toInt()


        binding = ActivitySelectTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBackBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.tvAmount.text = "FRQ  0"


//        binding.seekBar.max = item.left_tickets

//        binding.seekBar.progress = 1

//        binding.tvTotalSeats.text = item.left_tickets.toString() + " Seats"

        binding.rvTicket.apply {
            adapter = SelectTicketNewAdapter(list,this@SelectTicketActivity,item.ticket_type)
            layoutManager= LinearLayoutManager(this@SelectTicketActivity,LinearLayoutManager.VERTICAL,false)
        }

        sharedPreferences = getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!
        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId,"-1").toString()
        userRegistered = sharedPreferences.getBoolean(Constants.isUserTypeRegistered, false)


        binding.btnLogin.setOnClickListener {

            if(userRegistered && !authorization.isNullOrEmpty() && !audience_id.isNullOrEmpty() )
            {
                val intent = Intent (this,PaymentDetailActivity::class.java)
                intent.putExtra("item",item as Serializable)
                intent.putExtra("count",count)
                intent.putExtra("eventDetail", eventDetail as Serializable)
                startActivity(intent)
            }
            else
            {
                val intent = Intent (this,LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this,"You must login first to book ticket",Toast.LENGTH_SHORT).show()
            }


        }


/*

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                Toast.makeText(applicationContext, "seekbar progress: $progress", Toast.LENGTH_SHORT).show()

                val value = progress * (seekBar!!.width - 2 * seekBar.thumbOffset) / seekBar.max
                binding.tvSeekBar.setText(progress.toString())
                binding.tvSeekBar.setX(seekBar.x + value + seekBar.thumbOffset / 2)

                Log.d("SeekBar",progress.toString())
                binding.tvNumberofSeatsTag.text = "Number of Seats : $progress"

                binding.tvAmount.text = "FRQ " + progress* price

                count = progress.toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }




        } )

 */

    }

    override fun onClickAtTicket(mitem: EventTicket) {
//        Toast.makeText(this,"Ticket Clicked " + item.ticket_type,Toast.LENGTH_SHORT).show()

//        if(item.ticket_type!= mitem.ticket_type)
//        {
//            binding.rvTicket.adapter = SelectTicketNewAdapter(list,this@SelectTicketActivity,mitem.ticket_type)
//            binding.seekBar.max = mitem.left_tickets
//            binding.tvTotalSeats.text = mitem.left_tickets.toString() + " Seats"
//            binding.seekBar.progress = 1

//            price = mitem.price.toInt()

//            binding.tvAmount.text = "FRQ  ${price.toString()}"


//        }
//        else
//        {
//            Toast.makeText(this,"Ticket Already Selected",Toast.LENGTH_SHORT).show()
//        }



    }

    override fun onClickAtCount(item: EventTicket, count: Int) {

        binding.btnLogin.text = "Purchase"

        price = item.price.toInt()

        binding.tvAmount.text = "FRQ  ${price.toString().toInt() * count}"

    }
}