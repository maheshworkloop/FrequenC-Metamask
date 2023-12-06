package com.dev.frequenc.ui_codes.screens.Dashboard.savedevent

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.saved_event.SavedEventResponseItem
import com.dev.frequenc.databinding.ItemSavedEventsBinding
import com.dev.frequenc.util.ImageUtil
import com.dev.frequenc.ui_codes.util.ItemClickListener

class SavedEventAdapter(
    var savedEventList: List<SavedEventResponseItem>,
    val itemClickListener: ItemClickListener,
    val wholeItemClickListn: ItemClickListener
) : RecyclerView.Adapter<SavedEventAdapter.MyViewHolders>() {
    class MyViewHolders(val itemSavedEventsBinding: ItemSavedEventsBinding) :
        ViewHolder(itemSavedEventsBinding.root) {
        fun bind(savedEventResponseItem: SavedEventResponseItem) {
            itemSavedEventsBinding.savedEventResponseItem = savedEventResponseItem
            ImageUtil.loadImage(
                itemSavedEventsBinding.itemImg,
                savedEventResponseItem.eventImage[0]
            )
            itemSavedEventsBinding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolders {
        val itemSavedEventsBinding: ItemSavedEventsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_saved_events,
            parent,
            false
        )
        return MyViewHolders(itemSavedEventsBinding)
    }

    override fun getItemCount(): Int {
        return savedEventList.size
    }

    override fun onBindViewHolder(holder: MyViewHolders, position: Int) {
        val item_position = holder.bindingAdapterPosition
        holder.bind(savedEventList[item_position])
        holder.itemSavedEventsBinding.btnBookmark.setOnClickListener {
            itemClickListener.onItemClicked(
                item_position
            )
        }
        holder.itemSavedEventsBinding.mainLaysWithoutBookmarkBtnLays.setOnClickListener {
            wholeItemClickListn.onItemClicked(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(it: List<SavedEventResponseItem>) {
        this.savedEventList = it
        this.notifyDataSetChanged()
    }
}