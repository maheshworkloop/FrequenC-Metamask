package com.dev.frequenc.ui_codes.connect.yourvibes

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
import com.dev.frequenc.ui_codes.connect.home.YourVibeResponse
import com.dev.frequenc.ui_codes.data.CategoryDetail
import com.dev.frequenc.ui_codes.data.GetVibeCategoryResponse

class ShareVibesAdapter  (private val mData: GetVibeCategoryResponse, mListener : ListAdapterListener) : RecyclerView.Adapter<ShareVibesAdapter.ViewHolder> (){

    lateinit var mContext : Context
    var mListener = mListener

    val mList = mData.data

    interface ListAdapterListener{ fun onClickAtShareVibe(item : CategoryDetail) }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_recycler_share_vibes, parent, false)
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val item = mList!![position]

        Glide.with(mContext)
            .load(item!!.image)
            .into(holder.imageView )

        holder.textView.setText(item.name)

        holder.clCard.setOnClickListener { mListener.onClickAtShareVibe(item) }
    }


    override fun getItemCount(): Int {
        return mList!!.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivImage)
        val textView: TextView = itemView.findViewById(R.id.tvTitle)
        val clCard: ConstraintLayout = itemView.findViewById(R.id.clCard)
    }

}