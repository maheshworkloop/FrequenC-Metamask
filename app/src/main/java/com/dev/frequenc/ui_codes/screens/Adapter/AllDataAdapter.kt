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
import com.dev.frequenc.ui_codes.data.AllDataResponse
import com.dev.frequenc.util.AppCommonMethods

class AllDataAdapter (private val mList: List<AllDataResponse>, mListener : ListAdapterListener ) : RecyclerView.Adapter<AllDataAdapter.ViewHolder> (){

    lateinit var mContext : Context
    var mListener = mListener

    interface ListAdapterListener{ fun onClickAtEvent(item : AllDataResponse) }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rv_all_data, parent, false)
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList!![position]

        Glide.with(mContext)
            .load(item!!.image)
            .into(holder.imageView )

        holder.textView.setText(item.title)

        holder.tvEventDate.setText(
            AppCommonMethods.convertDateFormat2(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","dd MMM yyyy",item.eventStartDate))
//        holder.tvEventCount.setText(item.eventCount + " Events")

        holder.cvRv.setOnClickListener {   mListener.onClickAtEvent(item) }

//        if(item.is_bookmark)
//        {
//            holder.rlBookmark.visibility = View.GONE
//        }
//        else
//        {
//            holder.rlBookmark.visibility = View.VISIBLE
//        }
//
//        holder.rlBookmark.setOnClickListener {
//            if(!authorization.isNullOrEmpty() &&authorization!="-1"  )
//            {
//                holder.rlBookmark.visibility = View.GONE
//            }
//            mListener.onClickAtBookmark(item) }

    }


    override fun getItemCount(): Int {
        return mList!!.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivAllData)
        val textView: TextView = itemView.findViewById(R.id.tvAllEventName)
        val tvEventDate: TextView = itemView.findViewById(R.id.tvAllEventDate)
        val cvRv : CardView = itemView.findViewById(R.id.cvRv)
//        val rlBookmark : RelativeLayout = itemView.findViewById(R.id.rlBookMark)
    }

}