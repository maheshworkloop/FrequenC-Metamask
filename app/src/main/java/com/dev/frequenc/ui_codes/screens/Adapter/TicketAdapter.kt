package com.dev.frequenc.ui_codes.screens.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.EventTicket

class TicketAdapter (private val mList: List<EventTicket>,
                     mListener: ListAdapterListener
) : RecyclerView.Adapter<TicketAdapter.ViewHolder> (){

    lateinit var mContext : Context
    private var mListener = mListener
    private var selectedTicketPos: Int = -1

    interface ListAdapterListener{
        fun onClickAtTicket(item : EventTicket,mList: List<EventTicket>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rv_tickets, parent, false)

        mContext = parent.context

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList!![position]

        holder.tvTicket.setText(item.ticket_type)

        holder.tvSeat.setText("${item.left_tickets} left / ${item.no_of_tickets} seats")

        holder.clTicket.setOnClickListener { mListener.onClickAtTicket(item,mList)
            val prev_pos = selectedTicketPos
            selectedTicketPos = position
            notifyItemChanged(selectedTicketPos)
            notifyItemChanged(prev_pos)
        }

        if (position == selectedTicketPos ) {
            holder.clTicket.background = holder.clTicket.resources.getDrawable(R.drawable.selectedrv_tickets)
        }
        else {
            holder.clTicket.background = holder.clTicket.resources.getDrawable(R.drawable.rv_select_ticket)
        }
    }


    override fun getItemCount(): Int {
        return mList!!.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvTicket: TextView = itemView.findViewById(R.id.tvTicketType)
        val tvSeat: TextView = itemView.findViewById(R.id.tvSeats)
        val clTicket : ConstraintLayout = itemView.findViewById(R.id.clTicket)
    }

}