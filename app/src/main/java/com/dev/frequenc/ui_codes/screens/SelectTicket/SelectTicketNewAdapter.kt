package com.dev.frequenc.ui_codes.screens.SelectTicket

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.EventTicket

class SelectTicketNewAdapter (private val mList : List<EventTicket>, mListener : ListAdapterListener,
                              private val ticketType : String )  : RecyclerView.Adapter<SelectTicketNewAdapter.ViewHolder>() {



    private val mListener = mListener
    lateinit var mContext: Context
    var countList = ArrayList<Int>()


    interface ListAdapterListener {
        fun onClickAtTicket(item : EventTicket)
        fun onClickAtCount(item : EventTicket,count : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_selectticketnew,parent,false)
        mContext = parent.context


        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       countList.add(0)


        val item = mList[position]
        holder.tvTicketType.text = item.ticket_type

        holder.tvSeats.text = " ${item.left_tickets} spots left"


       val count = holder.tvCount.text.toString().toInt()

        holder.tvPrice.text =  "x ${item.price.toString()} = ${item.price.toFloat() * count }"

        holder.cvLayout.setOnClickListener {
            mListener.onClickAtTicket(item)
        }

        if(ticketType == item.ticket_type)
        {

            holder.cvLayout.background = mContext.getDrawable(R.drawable.rv_select_ticket_selected)
        }

        holder.cvPlus.setOnClickListener {

//            if(holder.tvCount.text.toString().toInt()==0)
//            {
//
//            }

            if(count<item.left_tickets)
            {

                val count = Integer.valueOf(holder.tvCount.text.toString()) + 1
                holder.tvCount.text = (count).toString()
                holder.tvPrice.text =  "x ${item.price.toString()} = ${item.price.toFloat() * count }"
                Log.d("count",holder.tvCount.text.toString())
                mListener.onClickAtCount(item,count)

            }



        }

        holder.cvMinus.setOnClickListener {


            if(count>0)
            {
                val count = (Integer.valueOf(holder.tvCount.text.toString()) - 1)
                holder.tvCount.text = ( count).toString()
                Log.d("count",holder.tvCount.text.toString())
                holder.tvPrice.text =  "x ${item.price.toString()} = ${item.price.toFloat() * count }"

                mListener.onClickAtCount(item,count)
            }



        }




    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)
    {
        val tvTicketType: TextView = itemView.findViewById(R.id.tvTicketType)
        val cvLayout: ConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.cvLayout)
        val tvSeats : TextView = itemView.findViewById(R.id.tvSeats)
        val tvPrice : TextView = itemView.findViewById(R.id.tvPrice)
        val tvCount : TextView = itemView.findViewById(R.id.tvCount)
        val cvPlus : CardView = itemView.findViewById(R.id.cvPlus)
        val cvMinus : CardView = itemView.findViewById(R.id.cvMinus)


    }

}