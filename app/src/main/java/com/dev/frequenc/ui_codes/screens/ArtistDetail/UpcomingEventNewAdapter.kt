package com.dev.frequenc.ui_codes.screens.ArtistDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.ui_codes.data.UpcomingEventResponse
import com.dev.frequenc.ui_codes.screens.Adapter.UpcomingEventAdapter
import com.dev.frequenc.util.AppCommonMethods

class UpcomingEventNewAdapter (private val mList: List<UpcomingEventResponse>,
                               mListener: UpcomingEventNewAdapter.ListAdapterListener, val isLogin : Boolean) : RecyclerView.Adapter<UpcomingEventNewAdapter.ViewHolder> (){

    lateinit var mContext : Context

    private var mListener  = mListener



    interface ListAdapterListener{
        fun onClickAtEvent(item : UpcomingEventResponse)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_recyclerview_trending_events_new, parent, false)

        mContext = parent.context

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList!![position]

        Glide.with(mContext)
            .load(item!!.eventImage[0])
            .into(holder.imageView )

        holder.textView.setText(item.eventTitle)

        holder.tvEventDate.setText(
            AppCommonMethods.convertDateFormat2(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","dd MMM yyyy",item.eventStartDate))
        holder.tvTrendingEventType.setText(item.category + " Events")

//        if(!item.venueDetails.venue_locality.isNullOrEmpty())
//          holder.tvLocation.setText(item.venueDetails.venue_locality)
//        else if(!item.venueDetails.venue_name.isNullOrEmpty())
//          holder.tvLocation.setText(item.venueDetails.venue_name)
//        else if(!item.venueDetails.address.isNullOrEmpty())
//            holder.tvLocation.setText(item.country)

        holder.cardView.setOnClickListener {  mListener.onClickAtEvent(item) }

        if(isLogin)
        {
            if(item.is_bookmark)
            {
                holder.rlBookmark.visibility = View.GONE
            }
            else
            {
                holder.rlBookmark.visibility = View.VISIBLE
            }
        }
        else
        {
            holder.rlBookmark.visibility = View.GONE
        }

    }


    override fun getItemCount(): Int {
        return mList!!.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivTrendingEvent)
        val textView: TextView = itemView.findViewById(R.id.tvTrendingEventName)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val tvTrendingEventType: TextView = itemView.findViewById(R.id.tvTrendingEventType)
        val tvEventDate: TextView = itemView.findViewById(R.id.tvTrendingEventTime)
        val cardView : ConstraintLayout = itemView.findViewById(R.id.cvRv)
        val rlBookmark : RelativeLayout = itemView.findViewById(R.id.rlBookMark)
    }

}