package com.dev.frequenc.ui_codes.connect.VibesProfileList

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.MatchVibeData
import java.time.LocalDate
import java.time.Period

class VibesProfileListAdapter(val mList : List<MatchVibeData>,val mListener : ListAdapterListener ) : RecyclerView.Adapter<VibesProfileListAdapter.ViewHolder>(){

    lateinit var mContext: Context
    interface ListAdapterListener{
         fun onClickAtProfile(item : MatchVibeData)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val root = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_vibe_user_list,parent,false)
        mContext = parent.context
        return  ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        val dateParts: List<String> = item.dob.split("-")
        val day = dateParts[2]
        val month = dateParts[1]
        val year = dateParts[0]
//        item.dob
        holder.tvAge.text = getAge(year.toInt(), month.toInt(),day.toInt()).toString()

        holder.clCard.setOnClickListener { mListener.onClickAtProfile(item) }

        Glide.with(mContext).load(item.profile_pic).into(holder.ivImage)

        if(!item.profile_pic.isNullOrEmpty())
            Glide.with(mContext).load(item.profile_pic).into(holder.ivImage)
        else
            Glide.with(mContext).load(R.drawable.baseline_person_2_24).into(holder.ivImage)

        holder.tvName.text = item.name + " , " + holder.tvAge.text

    }


    class  ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        var tvName = itemView.findViewById<TextView>(R.id.tvName)
        val tvAge = itemView.findViewById<TextView>(R.id.tvAge)
        val ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
        val clCard = itemView.findViewById<ConstraintLayout>(R.id.clCard)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }

}