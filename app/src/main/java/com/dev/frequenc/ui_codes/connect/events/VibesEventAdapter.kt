package com.dev.frequenc.ui_codes.connect.events

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
import com.dev.frequenc.ui_codes.data.Data
import com.dev.frequenc.ui_codes.data.VibeEventResponse
import com.dev.frequenc.ui_codes.data.VibeResponse

class VibesEventAdapter  (private val mData:VibeEventResponse, mListener : ListAdapterListener) : RecyclerView.Adapter<VibesEventAdapter.ViewHolder> (){

    lateinit var mContext : Context
    var mListener = mListener
    val mList = mData.data


    interface ListAdapterListener{ fun onClickAtVibe(item : VibeResponse) }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_yourvibe, parent, false)
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val item = mList!![position]

        Glide.with(mContext)
            .load(item!!.eventImage[0])
            .into(holder.imageView )

        holder.textView.setText(item!!.eventTitle)

        holder.clYourVibe.setOnClickListener { mListener.onClickAtVibe(item) }
    }


    override fun getItemCount(): Int {
        return mList!!.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivImage)
        val textView: TextView = itemView.findViewById(R.id.tvTitle)
        val clYourVibe : ConstraintLayout = itemView.findViewById(R.id.clYourVibe)
    }

}