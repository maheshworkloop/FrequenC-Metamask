package com.dev.frequenc.ui_codes.screens.SelectTicket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.EventTicket

class SelectTicketNewAdapter (private val mList : List<EventTicket>, mListener : ListAdapterListener,
                              private val ticketType : String )  : RecyclerView.Adapter<SelectTicketNewAdapter.ViewHolder>() {



    private val mListener = mListener
    lateinit var mContext: Context

    interface ListAdapterListener {
        fun onClickAtTicket(item : EventTicket)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_selectticket,parent,false)
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        holder.tvTicketType.text = item.ticket_type

        holder.tvSeats.text = " ${item.left_tickets} left / ${item.no_of_tickets} Seats"

        holder.tvPrice.text = item.price.toString()

        holder.cvLayout.setOnClickListener {
            mListener.onClickAtTicket(item)
        }

        if(ticketType == item.ticket_type)
        {
            Glide.with(mContext).load(mContext.getDrawable(R.drawable.ic_selected_ticket)).into(holder.ivCheckBox)
            holder.ivStar.visibility = View.VISIBLE
            holder.ivTriangle.visibility = View.VISIBLE

            holder.cvLayout.background = mContext.getDrawable(R.drawable.rv_select_ticket_selected)
        }

    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)
    {
        val tvTicketType: TextView = itemView.findViewById(R.id.tvTicketType)
        val cvLayout: ConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.cvLayout)
        val tvSeats : TextView = itemView.findViewById(R.id.tvSeats)
        val tvPrice : TextView = itemView.findViewById(R.id.tvPrice)
        val ivCheckBox : ImageView = itemView.findViewById(R.id.ivCheckBox)
        val ivStar : ImageView = itemView.findViewById(R.id.ivStar)
        val ivTriangle : ImageView = itemView.findViewById(R.id.ivTriangle)


    }

}