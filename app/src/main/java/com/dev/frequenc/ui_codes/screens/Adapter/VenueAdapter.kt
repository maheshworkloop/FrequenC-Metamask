package com.dev.frequenc.ui_codes.screens.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.VenueDetails

class VenueAdapter (private val mList: VenueDetails,
                    mListener: ListAdapterListener) : RecyclerView.Adapter<VenueAdapter.ViewHolder> (){

    lateinit var mContext : Context

    private var mListener  = mListener



    interface ListAdapterListener{

        fun onClickAtVenue(item : VenueDetails)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rv_venue, parent, false)

        mContext = parent.context

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList!!

        Glide.with(mContext)
            .load(item!!.profile_pic)
            .into(holder.imageView )

        holder.tvLocation.setText(item.venue_locality)

        holder.tvVenue.setText(item.venue_name)

        holder.cardView.setOnClickListener{ mListener.onClickAtVenue(item) }
    }


    override fun getItemCount(): Int {
        return 1
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView
        val tvVenue: TextView
        val tvLocation: TextView
        val cardView : CardView

        init {
            imageView = itemView.findViewById(R.id.ivVenue)
            tvVenue = itemView.findViewById(R.id.tvVenue)
            tvLocation =  itemView.findViewById(R.id.tvVenueLoc)
            cardView =  itemView.findViewById(R.id.cvRv)
        }
    }

}