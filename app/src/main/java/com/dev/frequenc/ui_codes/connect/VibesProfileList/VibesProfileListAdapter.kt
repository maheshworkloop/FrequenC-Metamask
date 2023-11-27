package com.dev.frequenc.ui_codes.connect.VibesProfileList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.VibesProfileResponse

class VibesProfileListAdapter(val mList : List<VibesProfileResponse>,val mListener : ListAdapterListener ) : RecyclerView.Adapter<VibesProfileListAdapter.ViewHolder>(){

    lateinit var mContext: Context
    interface ListAdapterListener{
         fun onClickAtProfile(item : VibesProfileResponse)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val root = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_vibe_user_list,parent,false)
        mContext = parent.context
        return  ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        holder.tvName.text = item.name
        holder.tvAge.text = item.age
        holder.clCard.setOnClickListener { mListener.onClickAtProfile(item) }
    }


    class  ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        var tvName = itemView.findViewById<TextView>(R.id.tvName)
        val tvAge = itemView.findViewById<TextView>(R.id.tvAge)
        val ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
        val clCard = itemView.findViewById<ConstraintLayout>(R.id.clCard)

    }

}