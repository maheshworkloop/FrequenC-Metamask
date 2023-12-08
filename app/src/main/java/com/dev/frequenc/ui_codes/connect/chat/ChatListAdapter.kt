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
import com.dev.frequenc.ui_codes.data.ConnectionResponse
import com.dev.frequenc.ui_codes.data.pending_request.Data
import com.dev.frequenc.util.ImageUtil


class ChatListAdapter(
    private val chatList: ArrayList<Any>,
    private val connectionList: ArrayList<ConnectionResponse>,
    private var useType: Int,
    private val itemListListener: ItemListListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val ItemUserListLay: Int = 1
        private const val ItemUserChatPendingListLay: Int = 2
        private const val ItemUserChatRequestsLay: Int = 3

    }

    var conectLst = connectionList

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
                val time = it.chatTime.toInt() / 60000
                val user_image = it.chatPersonImage

                try {
                    binding.tvLastMsg.text = "You: ${it.lastMessage.substring(4)}"
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                binding.tvTime.text = "$time min ago"
                binding.tvProfileName.text = it.profileName

                ImageUtil.loadImage(binding.cvProfile, user_image)
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
            pendingItem: Any,
            itemListListener: ItemListListener,
            position: Int
        ) {
            (pendingItem as Data)?.let {
                var item_image: String = ""
                try {
                    item_image = pendingItem.from_user_id.audience_id.profile_pic.toString()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

                ImageUtil.loadImage(binding.cvProfile, item_image)

                try {
                    binding.tvProfileName.text =
                        pendingItem.from_user_id.audience_id.fullName.toString()
//                    binding.tvTime.text = pendingItem.from_user_id.
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                binding.cvProfile.setOnClickListener {
                    itemListListener.onItemClicked(
                        position,
                        ItemUserChatPendingListLay,
                        "goProfile"
                    )
                }
                binding.btnAccept.setOnClickListener {
                    itemListListener.onItemClicked(position, ItemUserChatPendingListLay, "accept")
                }
                binding.btnDecline.setOnClickListener {
                    itemListListener.onItemClicked(position, ItemUserChatPendingListLay, "decline")
                }
            }


        }
    }

    class MyChatRequestsViewHolder(val binding: ItemUserChatRequestsBinding) :
        ViewHolder(binding.root) {
        fun bindChatRequestsViews(
            MyRequestDataItem: Any,
            itemListListener: ItemListListener,
            position: Int
        ) {
            (MyRequestDataItem as com.dev.frequenc.ui_codes.data.myrequests.Data)?.let {
                try {
                    binding.tvProfileName.text = it.to_user_id.audience_id.fullName.toString()
                    var item_img = it.to_user_id.audience_id.profile_images[0].toString()
//                    if (item_img.isNullOrEmpty() || item_img.isNullOrBlank()) {
//                        for (connectonItm: ConnectionResponse in )
//                    }
                    ImageUtil.loadImage( binding.cvProfile, item_img)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

                binding.cvProfile.setOnClickListener {
                    itemListListener.onItemClicked(position, ItemUserChatRequestsLay, "goProfile")
                }

                binding.itemLays.setOnClickListener {
                    itemListListener.onItemClicked(position, ItemUserChatRequestsLay, "goChat")
                }
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
                        chatList[item_position],
                        itemListListener,
                        item_position
                    )
                }

                is MyChatRequestsViewHolder -> {
                    holder.bindChatRequestsViews(
                        chatList[item_position],
                        itemListListener,
                        item_position
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
    fun refreshData(
        newData: List<Any>,
        connectionLists: List<ConnectionResponse>,
        newUseType: Int
    ) {
        chatList.clear()
        chatList.addAll(newData)
        this.connectionList.clear()
        this.connectionList.addAll(connectionLists)
        this.useType = newUseType
        notifyDataSetChanged()
    }
}
