package com.dev.frequenc.ui_codes.screens.SelectTicket

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
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

    var selectedPos = -1

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

        holder.tvSeats.text = "${item.left_tickets} spots left"

        if(holder.tvCount.text.equals("0"))
        {
            holder.rl_bg.setBackgroundResource(R.drawable.not_select_ticket)
            holder.tvCount.setTextColor(mContext.getColor(R.color.black)  )
            Log.d("tvcount",holder.tvCount.text.toString())
            holder.cvLayout.setBackgroundResource(R.drawable.rv_select_ticket)

        }
        else
        {
            holder.rl_bg.setBackgroundResource(R.drawable.select_ticket)
            Log.d("tvcount",holder.tvCount.text.toString())
            holder.tvCount.setTextColor(mContext.getColor(R.color.white)  )

            holder.cvLayout.setBackgroundResource(R.drawable.rv_select_ticket_selected)

        }


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

            val count = Integer.valueOf(holder.tvCount.text.toString()) + 1

            if(count<item.left_tickets)
            {

                selectedPos = holder.bindingAdapterPosition

                holder.tvCount.text = (count).toString()
                holder.tvPrice.text =  "x ${item.price.toString()} = ${item.price.toFloat() * count }"
                Log.d("count",holder.tvCount.text.toString())
                mListener.onClickAtCount(item,count)


                if(count==1)
                 notifyDataSetChanged()



            }



        }

        holder.cvMinus.setOnClickListener {

            val count = (Integer.valueOf(holder.tvCount.text.toString()) - 1)

            if(count>-1)
            {
                holder.tvCount.text = ( count).toString()
                Log.d("count",holder.tvCount.text.toString())
                holder.tvPrice.text =  "x ${item.price.toString()} = ${item.price.toFloat() * count }"
                mListener.onClickAtCount(item,count)

                if(count == 0)
                {
                    holder.tvCount.setTextColor(mContext.resources.getColor(R.color.black) )
                    holder.rl_bg.background = mContext.getDrawable(R.drawable.not_select_ticket)

                }

            }
            else
            {
                holder.tvCount.setTextColor(mContext.resources.getColor(R.color.black) )
                holder.rl_bg.background = mContext.getDrawable(R.drawable.not_select_ticket)

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
        val cvPlus : ImageView = itemView.findViewById(R.id.ivPlus)
        val cvMinus : ImageView = itemView.findViewById(R.id.ivMinus)
        val rl_bg : RelativeLayout = itemView.findViewById(R.id.rl_bg)
        val tvSelect : RelativeLayout = itemView.findViewById(R.id.rl_bg)


    }

}