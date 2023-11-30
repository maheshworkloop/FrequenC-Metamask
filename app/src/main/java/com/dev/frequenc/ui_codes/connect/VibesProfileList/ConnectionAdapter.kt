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
import com.dev.frequenc.ui_codes.data.ConnectionResponse

class ConnectionAdapter(var mList: List<ConnectionResponse>,val mListener : ListAdapterListener) : RecyclerView.Adapter<ConnectionAdapter.ViewHolder> () {

    lateinit var mContext : Context
    interface ListAdapterListener
    {
     fun onClickAtConnection(item : ConnectionResponse)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConnectionAdapter.ViewHolder {

        var root = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_connection,parent,false)

        mContext =parent.context
        return ViewHolder(root)

    }

    override fun onBindViewHolder(holder: ConnectionAdapter.ViewHolder, position: Int) {

        val item = mList[position]

        holder.ivImage.setBackgroundResource(item.image)

        holder.clConnection.setOnClickListener {
            mListener.onClickAtConnection(item)
        }

        if(item.status)
        {
            holder.tvStatus.background = mContext.getDrawable(R.drawable.bg_tv_status_active)
        }
        else
        {
            holder.tvStatus.background = mContext.getDrawable(R.drawable.bg_tv_status_inactive)

        }

    }

    override fun getItemCount(): Int {
        return  mList.size
    }

    fun update(itemLists : List<ConnectionResponse>) {
        this.mList = itemLists
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
        val tvStatus = itemView.findViewById<TextView>(R.id.tvStatus)
        val clConnection = itemView.findViewById<ConstraintLayout>(R.id.clConnection)
    }

}