package com.dev.frequenc.ui_codes.connect.VibesProfileList

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.MatchVibeData
import com.dev.frequenc.ui_codes.data.QuoteResponse
import java.time.LocalDate
import java.time.Period

class QuoteAdapter (val mData : QuoteResponse ) : RecyclerView.Adapter<QuoteAdapter.ViewHolder>(){

    lateinit var mContext: Context

    val mList = mData.data


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val root = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_quote,parent,false)
        mContext = parent.context
        return  ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.tvQuote.text = item.name

        Log.d("rv",item.name)

        }


    class  ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        var tvQuote = itemView.findViewById<TextView>(R.id.tvQuote)
    }


}