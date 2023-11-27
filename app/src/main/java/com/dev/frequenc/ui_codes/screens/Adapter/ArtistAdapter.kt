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
import com.dev.frequenc.ui_codes.data.Artist

class ArtistAdapter (private val mList: List<Artist>,
                     mListener: ListAdapterListener) : RecyclerView.Adapter<ArtistAdapter.ViewHolder> (){

    lateinit var mContext : Context

    private var mListener  = mListener



    interface ListAdapterListener{

        fun onClickAtArtist(item : Artist)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rv_trending_artist, parent, false)

        mContext = parent.context

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList!![position]

        Glide.with(mContext)
            .load(item!!.profile_pic)
            .into(holder.imageView )

        holder.textView.setText(item.artist_name)

        holder.tvEventCount.setText(item.category)

        holder.cardView.setOnClickListener{ mListener.onClickAtArtist(item) }
    }


    override fun getItemCount(): Int {
        return mList!!.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView
        val textView: TextView
        val tvEventCount: TextView
        val cardView : CardView

        init {
            imageView = itemView.findViewById(R.id.ivTrendingArtist)
            textView = itemView.findViewById(R.id.tvTrendingArtistName)
            tvEventCount =  itemView.findViewById(R.id.tvTrendingArtistEvent)
            cardView =  itemView.findViewById(R.id.cvRv)
        }
    }

}