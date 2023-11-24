package com.dev.frequenc.ui_codes.screens.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.notification.Notification
import com.dev.frequenc.databinding.ItemNotificationBinding
import com.dev.frequenc.util.ImageUtil
import com.dev.frequenc.util.ItemClickListener

class NotificationAdapter(private var notificationList: List<Any>,val onItemClickListener: ItemClickListener) : Adapter<NotificationAdapter.MyViewHolder>() {
    class MyViewHolder(val itemNotificationBinding: ItemNotificationBinding) : ViewHolder(itemNotificationBinding.root) {
        fun bind(notificationItem: Notification) {
            notificationItem?.let {
                itemNotificationBinding.tvItemDetail.text= notificationItem.message
                itemNotificationBinding.tvTime.text = "${notificationItem.created_at}min ago"
                ImageUtil.loadImage(itemNotificationBinding.ivItemImage, notificationItem.notification_url)
                if ( notificationItem.isRead) {
                    itemNotificationBinding.btnRead.setImageResource(R.drawable.like)
                }
                else {
                    itemNotificationBinding.btnRead.setImageResource(R.drawable.dislike)
                }
             }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemNotificationBinding = ItemNotificationBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(itemNotificationBinding)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         val item_pos = holder.bindingAdapterPosition
        if (item_pos != -1 ) {
            val itemObjs = notificationList[item_pos]
            if (itemObjs != null) {
                holder.bind(itemObjs as Notification)
            }

            holder.itemNotificationBinding.btnRead.setOnClickListener { onItemClickListener.onItemClicked(item_pos) }

        }
        }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(it: List<Any>) {
        this.notificationList = it
        this.notifyDataSetChanged()
    }
}
