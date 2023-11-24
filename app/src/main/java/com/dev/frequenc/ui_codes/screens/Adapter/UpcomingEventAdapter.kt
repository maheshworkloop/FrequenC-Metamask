package com.dev.frequenc.ui_codes.screens.Adapter

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
import com.dev.frequenc.ui_codes.data.UpcomingEventResponse

class UpcomingEventAdapter (private val mList: List<UpcomingEventResponse>,
                            mListener: ListAdapterListener) : RecyclerView.Adapter<UpcomingEventAdapter.ViewHolder> (){

    lateinit var mContext : Context

    private var mListener  = mListener



    interface ListAdapterListener{

        fun onClickAtEvent(item : UpcomingEventResponse)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rv_upcoming_event_artist, parent, false)

        mContext = parent.context

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList!![position]

        Glide.with(mContext)
            .load(item!!.eventImage[0])
            .into(holder.ivEvent )

        holder.tvEventName.setText(item.eventTitle)

        holder.tvLocation.setText(item.venueid.address)

        holder.cvUpcomingEvent.setOnClickListener{ mListener.onClickAtEvent(item) }
    }


    override fun getItemCount(): Int {
        return mList!!.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivEvent: ImageView
        val tvEventName: TextView
        val tvLocation: TextView
        val cvUpcomingEvent : ConstraintLayout

        init {
            ivEvent = itemView.findViewById(R.id.ivEvent)
            tvEventName = itemView.findViewById(R.id.tvEventName)
            tvLocation =  itemView.findViewById(R.id.tvLocation)
            cvUpcomingEvent =  itemView.findViewById(R.id.cvUpcomingEvent)
        }
    }

}