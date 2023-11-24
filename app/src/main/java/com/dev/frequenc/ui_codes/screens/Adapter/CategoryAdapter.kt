package com.dev.frequenc.ui_codes.screens.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.BrowseByCatResponse
import de.hdodenhof.circleimageview.CircleImageView

class CategoryAdapter(private val mList: List<BrowseByCatResponse?>, mListerner : ListAdapterListerner) : RecyclerView.Adapter<CategoryAdapter.ViewHolder> (){

    lateinit var mContext : Context
    private var mListener = mListerner

    interface ListAdapterListerner{ fun onClickAtCategory(item : BrowseByCatResponse) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_browse_by_cat, parent, false)

        mContext = parent.context

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {

        val item = mList!![position]

        Glide.with(mContext)
            .load(item!!.image_url) // Replace with the actual resource ID of your GIF
            .into(holder.imageView )
        holder.textView.setText(item.name)
        holder.tvEventCount.setText(item.eventCount + " Events")

        holder.rlMain.setOnClickListener{
            mListener.onClickAtCategory(item)
        }

    }

    override fun getItemCount(): Int {
        return mList!!.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: CircleImageView = itemView.findViewById(R.id.ivEvent)
        val textView: TextView = itemView.findViewById(R.id.tvBrowseByCat)
        val tvEventCount: TextView = itemView.findViewById(R.id.tvEventCount)
        val rlMain : CardView = itemView.findViewById(R.id.rlMain)
    }

}