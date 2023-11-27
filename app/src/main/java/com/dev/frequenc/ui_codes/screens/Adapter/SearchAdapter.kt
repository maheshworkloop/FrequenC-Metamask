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
import com.dev.frequenc.ui_codes.data.SearchResponse

class SearchAdapter(private val mList : MutableList<SearchResponse>, mListener: ListAdapterLister ) : RecyclerView.Adapter<SearchAdapter.ViewHolder> ()
{

    lateinit var mContext : Context
    var mListener = mListener

    interface ListAdapterLister { fun onClick(item : SearchResponse) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_rv_search,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val item = mList[position]

        Glide.with(mContext).load(item.image).into(holder.ivEvent)

        holder.tvEventType.text = item.type

        holder.tvDescription.text = item.details

        holder.cvEvent.setOnClickListener { mListener.onClick(item) }

        holder.tvEventName.text = item.title


    }

    override fun getItemCount(): Int {
       return mList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val ivEvent = itemView.findViewById<ImageView>(R.id.ivEvent)
        val tvEventType = itemView.findViewById<TextView>(R.id.tvType)
        val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        val tvEventName = itemView.findViewById<TextView>(R.id.tvEventName)
        val cvEvent = itemView.findViewById<ConstraintLayout>(R.id.cvEvent)
    }

    fun updateList()
    {
        mList.clear()
        notifyDataSetChanged()
    }


}