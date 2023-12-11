package com.dev.frequenc.ui_codes.connect.chat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.frequenc.databinding.ItemUserChatPendingListBinding
import com.dev.frequenc.databinding.ItemUserChatRequestsBinding
import com.dev.frequenc.databinding.ItemUserListBinding
import com.dev.frequenc.ui_codes.data.ChatUserModel
import io.agora.chat.Conversation


class ChatListAdapter(
    private val chatList: ArrayList<Any>,
    private var useType: Int,
    private val itemListListener: ItemListListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val ItemUserListLay = 1
        private const val ItemUserChatPendingListLay = 2
        private const val ItemUserChatRequestsLay: Int = 3

    }

    private var mContext: Context? = null

    interface ItemListListener {
        fun onItemClicked(itemPosition: Int, useType: Int, action: String)
    }

    class MyUserListViewHolder(val binding: ItemUserListBinding) : ViewHolder(binding.root) {


        fun bindUserListViews(
            chatItem: Any,
            position: Int,
            itemListListener: ItemListListener
        ) {
            (chatItem as ChatUserModel)?.let {
                val time = it.chatTime.toInt()/60000
                val user_image = it.chatPersonImage.toInt().toString()

                binding.tvLastMsg.text = "You: ${it.lastMessage}"
                binding.tvTime.text = "$time min ago"
                binding.tvProfileName.text = it.profileName

//            ImageUtil.loadImage(itemUserListBinding.cvProfile, )
                binding.cvProfile.setOnClickListener {
                    itemListListener.onItemClicked(position, ItemUserListLay, "goProfile")
                }
                binding.btnEdit.setOnClickListener {
                    itemListListener.onItemClicked(position, ItemUserListLay, "showMenu")
                }

                binding.itemLays.setOnClickListener {
                    itemListListener.onItemClicked(position, ItemUserListLay, "goChat")
                }
            }
        }
    }

    class MyPendingRequestViewHolder(val binding: ItemUserChatPendingListBinding) :
        ViewHolder(binding.root) {
        fun bindPendingRequestViews(
            position: Int,
            itemListListener: ItemListListener,
            any: Any
        ) {

            binding.cvProfile.setOnClickListener {
                itemListListener.onItemClicked(position, ItemUserListLay, "goProfile")
            }
            binding.btnAccept.setOnClickListener {
                itemListListener.onItemClicked(position, ItemUserListLay, "accept")
            }
            binding.btnDecline.setOnClickListener {
                itemListListener.onItemClicked(position, ItemUserListLay, "decline")
            }

//            ImageUtil.loadImage(itemUserListBinding.cvProfile, )

        }
    }

    class MyChatRequestsViewHolder(val binding: ItemUserChatRequestsBinding) :
        ViewHolder(binding.root) {
        fun bindChatRequestsViews(
            position: Int,
            itemListListener: ItemListListener,
            any: Any
        ) {

//            ImageUtil.loadImage(itemUserListBinding.cvProfile, )
            binding.cvProfile.setOnClickListener {
                itemListListener.onItemClicked(position, ItemUserListLay, "goProfile")
            }

            binding.itemLays.setOnClickListener {
                itemListListener.onItemClicked(position, ItemUserListLay, "goChat")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        return when (viewType) {
            ItemUserListLay -> {
                val itemUserListBinding: ItemUserListBinding =
                    ItemUserListBinding.inflate(LayoutInflater.from(mContext), parent, false)
                MyUserListViewHolder(itemUserListBinding)
            }

            ItemUserChatPendingListLay -> {
                val itemUserChatPendingListBinding: ItemUserChatPendingListBinding =
                    ItemUserChatPendingListBinding.inflate(
                        LayoutInflater.from(mContext),
                        parent,
                        false
                    )
                return MyPendingRequestViewHolder(itemUserChatPendingListBinding)
            }

            ItemUserChatRequestsLay -> {
                val itemUserChatRequestsBinding: ItemUserChatRequestsBinding =
                    ItemUserChatRequestsBinding.inflate(
                        LayoutInflater.from(mContext),
                        parent,
                        false
                    )
                return MyChatRequestsViewHolder(itemUserChatRequestsBinding)
            }
            // Add more cases for other view types...
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item_position = position
        try {
            when (holder) {
                is MyUserListViewHolder -> {
                    holder.bindUserListViews(
                        chatList[item_position],
                        item_position,
                        itemListListener
                    )
                }

                is MyPendingRequestViewHolder -> {
                    holder.bindPendingRequestViews(
                        item_position,
                        itemListListener,
                        chatList[item_position]
                    )
                }

                is MyChatRequestsViewHolder -> {
                    holder.bindChatRequestsViews(
                        item_position,
                        itemListListener,
                        chatList[item_position]
                    )
                }

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
//        return 7
            return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (useType) {
            1 -> ItemUserListLay
            2 -> ItemUserChatPendingListLay
            else -> ItemUserChatRequestsLay
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newData: List<Any>, newUseType: Int) {
        chatList.clear()
        chatList.addAll(newData)
        this.useType = newUseType
        notifyDataSetChanged()
    }
}
