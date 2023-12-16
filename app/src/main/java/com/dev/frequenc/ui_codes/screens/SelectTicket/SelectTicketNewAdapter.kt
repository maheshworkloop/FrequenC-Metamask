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

    var selectedPos = -1
    var prePos = -1
    var count = 1
    var flagChange = false

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



        val item = mList[position]
        holder.tvTicketType.text = item.ticket_type

        holder.tvSeats.text = "${item.left_tickets} spots left"

        if(selectedPos != -1 && selectedPos!=prePos && holder.bindingAdapterPosition==selectedPos)
        {
            holder.cvLayout.background = mContext.getDrawable(R.drawable.rv_select_ticket_selected_big)
            holder.rl_bg.setBackgroundResource(R.drawable.select_ticket)
            holder.tvCount.setTextColor(mContext.getColor(R.color.white)  )
            holder.tvCount.text = "1"
        }

        if(selectedPos != -1 && selectedPos!=prePos && holder.bindingAdapterPosition==prePos )
        {
            holder.cvLayout.background = mContext.getDrawable(R.drawable.rv_select_ticket_big)
            holder.rl_bg.setBackgroundResource(R.drawable.not_select_ticket)
            holder.tvCount.setTextColor(mContext.getColor(R.color.black)  )
            holder.tvCount.text = "0"
            holder.tvPrice.text =  "x ${item.price} = ${item.price.toFloat() * 0 }"

        }

        if(ticketType == item.ticket_type && selectedPos==-1)
        {
            item.selected = true
            holder.rl_bg.setBackgroundResource(R.drawable.select_ticket)
            holder.tvCount.setTextColor(mContext.getColor(R.color.white)  )
            holder.tvCount.text = "1"
            holder.tvPrice.text =  "x ${item.price.toString()} = ${item.price.toFloat() * count }"
            mListener.onClickAtCount(item,1)

        }


        holder.cvPlus.setOnClickListener {


            if(count<item.left_tickets)
            {
                count++
                prePos = selectedPos
                selectedPos = holder.bindingAdapterPosition

                if (prePos == selectedPos) {

                    holder.tvCount.text = (count).toString()
                    holder.tvPrice.text =  "x ${item.price.toString()} = ${item.price.toFloat() * count }"

                          }
                else
                {
                    count = 1
                    holder.tvCount.text = (count).toString()
                    holder.tvPrice.text =  "x ${item.price.toString()} = ${item.price.toFloat() * count }"
                    notifyItemChanged(prePos)
                    notifyItemChanged(selectedPos)

                    Log.d("pos","prepos - $prePos")
                    Log.d("pos","selectedpos - $selectedPos")

                    Log.d("count",holder.tvCount.text.toString())
                }

                mListener.onClickAtCount(item,count)

            }


        }

        holder.cvMinus.setOnClickListener {

            if(count>1)
            {


                if (selectedPos == holder.bindingAdapterPosition) {

                    count--
                    holder.tvCount.text = (count).toString()
                    holder.tvPrice.text =  "x ${item.price.toString()} = ${item.price.toFloat() * count }"

                }

                mListener.onClickAtCount(item,count)
            }

        }    // cvMinus click listener

    } // end of bind view holder

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)
    {
        val tvTicketType: TextView = itemView.findViewById(R.id.tvTicketType)
        val cvLayout: CardView = itemView.findViewById<CardView>(R.id.cvLayout)
        val tvSeats : TextView = itemView.findViewById(R.id.tvSeats)
        val tvPrice : TextView = itemView.findViewById(R.id.tvPrice)
        val tvCount : TextView = itemView.findViewById(R.id.tvCount)
        val cvPlus : RelativeLayout = itemView.findViewById(R.id.rlPlus)
        val cvMinus : RelativeLayout = itemView.findViewById(R.id.rlMinus)
        val rl_bg : RelativeLayout = itemView.findViewById(R.id.rl_bg)
        val tvSelect : RelativeLayout = itemView.findViewById(R.id.rl_bg)


    }

}