package com.dev.frequenc.ui_codes.connect.VibesProfileList

import android.annotation.SuppressLint
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
import com.dev.frequenc.util.ImageUtil

class ConnectionAdapter(
    var mList: List<ConnectionResponse>,
    var isOnlineList: List<Boolean>,
    val mListener: ListAdapterListener
) : RecyclerView.Adapter<ConnectionAdapter.ViewHolder>() {

    lateinit var mContext: Context

    interface ListAdapterListener {
        fun onClickAtConnection(item: ConnectionResponse)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        var root = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_recycler_connection, parent, false)

        mContext = parent.context
        return ViewHolder(root)

    }

    override fun onBindViewHolder(holder: ConnectionAdapter.ViewHolder, position: Int) {
        try {

            val item = mList[position]

            if (!item.image.isNullOrEmpty()) {
                ImageUtil.loadImage(holder.ivImage, item.image)
            }

            holder.clConnection.setOnClickListener {
                mListener.onClickAtConnection(item)
            }

            if (isOnlineList[position]) {
                holder.tvStatus.background = mContext.getDrawable(R.drawable.bg_tv_status_active)
            } else {
                holder.tvStatus.background = mContext.getDrawable(R.drawable.bg_tv_status_inactive)

            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
//        return 7
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(itemLists: List<ConnectionResponse>) {
        this.mList = itemLists
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateOnlineStatus(isOnlineLists: List<Boolean>) {
        this.isOnlineList = isOnlineLists
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
        val tvStatus = itemView.findViewById<TextView>(R.id.tvStatus)
        val clConnection = itemView.findViewById<ConstraintLayout>(R.id.clConnection)
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
    }

}