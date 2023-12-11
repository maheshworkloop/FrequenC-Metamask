package com.dev.frequenc.ui_codes.connect.chat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dev.frequenc.R
import com.dev.frequenc.databinding.FragmentAllChatUserBinding
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.connect.Profile.ProfileFragment
import com.dev.frequenc.ui_codes.connect.VibesProfileList.ConnectionAdapter
import com.dev.frequenc.ui_codes.data.ConnectionResponse
import com.dev.frequenc.ui_codes.screens.notification.NotificationActivity
import com.dev.frequenc.ui_codes.util.Constants
import io.agora.ContactListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AllChatUserFragment : Fragment(), ChatListAdapter.ItemListListener,
    ConnectionAdapter.ListAdapterListener {

    private var currentActivity: FragmentActivity? = null
    lateinit var binding: FragmentAllChatUserBinding
    lateinit var allChatListViewModel: AllChatListViewModel
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val ItemUserListLay = 1
        private const val ItemUserChatPendingListLay = 2
        private const val ItemUserChatRequestsLay: Int = 3

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllChatUserBinding.inflate(inflater, container, false)
        try {
            currentActivity = activity
        } catch (ex: Exception) {
            try {
                currentActivity = requireActivity()
            } catch (exss: Exception) {
                exss.printStackTrace()
            }
            ex.printStackTrace()
        }

        allChatListViewModel = ViewModelProvider(requireActivity())[AllChatListViewModel::class.java]
        return binding.root

    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences(
            Constants.SharedPreference,
            Context.MODE_PRIVATE
        ) as SharedPreferences
        val connectionAdapter =
            ConnectionAdapter(ArrayList(3), ArrayList(3), this@AllChatUserFragment)
        binding.rvConnection.apply {
            adapter = connectionAdapter
        }
        val chatListAdapter =
            ChatListAdapter(ArrayList(3), ArrayList(3), ItemUserListLay, this@AllChatUserFragment)
        binding.rvChatUser.apply {
            adapter = chatListAdapter
        }

        activity?.runOnUiThread {
            allChatListViewModel.isConnectionTabSelected.observe(viewLifecycleOwner) {
                sharedPreferences?.getString(Constants.Authorization, null)?.let { token ->
                    showConnectionTab(it, token)
                }
            }
        }

        val authorization = sharedPreferences.getString(Constants.Authorization,null)
        if(!authorization.isNullOrEmpty() && authorization!="-1" )
        {
            binding.rlSearchTop.ivHamburger.setOnClickListener {
                (activity as MainActivity).binding.drawerLayout.openDrawer(GravityCompat.END)
            }
        }
        else
        {
            Toast.makeText(context,"Not Logged in Failure", Toast.LENGTH_SHORT).show()
        }

        binding.rlSearchTop.ivNotification.setOnClickListener {
            val intent = Intent(activity,  NotificationActivity::class.java)
            startActivity(intent)
        }

        activity?.runOnUiThread {
            allChatListViewModel.isPendingSubTabSelected.observe(viewLifecycleOwner) {
                sharedPreferences?.getString(Constants.Authorization, null)?.let { token ->
                    showPendingRequestsSubTab(
                        it,
                        token
                    )
                }
            }
        }

        activity?.runOnUiThread {
            allChatListViewModel.isApiCalled.observe(viewLifecycleOwner) {
                if (it == true) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        activity?.runOnUiThread {
            allChatListViewModel.userListsData?.observe(viewLifecycleOwner) { it23 ->
                if (allChatListViewModel.isConnectionTabSelected.value == true) {
                    chatListAdapter.refreshData(
                        it23,
                        allChatListViewModel.connectionList.value,
                        ItemUserListLay
                    )
                    binding.tvChatTag.text = "Chats (${allChatListViewModel.chatCount.value})"

                } else {
                    if (allChatListViewModel.isPendingSubTabSelected.value == true) {
                        chatListAdapter.refreshData(
                            it23,
                            allChatListViewModel.connectionList.value,
                            ItemUserChatPendingListLay
                        )
                    } else {
                        chatListAdapter.refreshData(
                            it23,
                            allChatListViewModel.connectionList.value,
                            ItemUserChatRequestsLay
                        )
                    }
                }
            }
        }

        activity?.runOnUiThread {
            allChatListViewModel.myRequestCount.observe(viewLifecycleOwner) {
                binding.tvCountMyrequests.text =
                    allChatListViewModel.myRequestCount.value.toString()
            }

            allChatListViewModel.pendingRequestCount.observe(viewLifecycleOwner) {
                binding.tvCountPending.text =
                    allChatListViewModel.pendingRequestCount.value.toString()
            }

            try {
                allChatListViewModel.isOnlineList.observe(viewLifecycleOwner) {
                    connectionAdapter.updateOnlineStatus(it)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }


        activity?.runOnUiThread {
            allChatListViewModel.connectionList.observe(viewLifecycleOwner) {
                try {
//                    allChatListViewModel.getConnectionListWithPresence()
                    binding.tvConnectionTag.text =
                        "Connection (${allChatListViewModel.connectionList.value?.size})"
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                if (it.isNullOrEmpty()) {
                    binding.connectionNotFoundLay.visibility = View.VISIBLE
                    binding.rvConnection.visibility = View.INVISIBLE
                    binding.tvConnectionTag.text = "Connection"
                } else {
                    binding.rvConnection.visibility = View.VISIBLE
                    binding.connectionNotFoundLay.visibility = View.GONE
                    binding.tvConnectionTag.text = "Connection (${it.size})"
                }
                connectionAdapter.update(it)

            }
        }

        GlobalScope.launch {
            sharedPreferences?.getString(Constants.Authorization, null)?.let { token ->
                allChatListViewModel.callConnectionApi(token)
            }
        }

        activity?.runOnUiThread {
            allChatListViewModel.isDataFound.observe(viewLifecycleOwner) {
                if (it) {
                    binding.dataNotFoundLay.noDataLay.visibility = View.INVISIBLE
                    binding.rvChatUser.visibility = View.VISIBLE
                } else {
                    binding.rvChatUser.visibility = View.INVISIBLE
                    binding.dataNotFoundLay.noDataLay.visibility = View.VISIBLE
                }
            }
        }

        binding.tvRequestsTag.setOnClickListener {
            allChatListViewModel.setConnectionTab(false)
        }
        binding.tvConnectionTag.setOnClickListener {
            allChatListViewModel.setConnectionTab(true)
        }

        binding.pendingTab.setOnClickListener {
            allChatListViewModel.setPendingTab(true)
        }
        binding.myRequestsTab.setOnClickListener {
            allChatListViewModel.setPendingTab(false)
        }

        allChatListViewModel.setConnectionTab(true)

        GlobalScope.launch {
            allChatListViewModel.setOnPresenceChange()
        }

        activity?.runOnUiThread {
            allChatListViewModel.toastMessage.observe(viewLifecycleOwner)
            { observr ->
                run {
                    if (!observr.isNullOrEmpty()) {
                        Toast.makeText(context, observr, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        activity?.runOnUiThread {
            allChatListViewModel.setContactChangeListener(
                object : ContactListener {

                    override fun onFriendRequestAccepted(username: String) {
                        Log.d(Constants.TAG_CHAT, "onFriendRequestAccepted:username=> $username")
                    }

                    //contact request is rejected
                    override fun onFriendRequestDeclined(username: String) {
                        Log.d(Constants.TAG_CHAT, "onFriendRequestDeclined:username=> $username")
                    }

                    //Received contact invitation
                    override fun onContactInvited(username: String, reason: String) {
                        Log.d(
                            Constants.TAG_CHAT,
                            "onContactInvited:username=> $username  and reason =>> $reason"
                        )
                    }

                    //Call back this method when deleted
                    override fun onContactDeleted(username: String) {
                        Log.d(Constants.TAG_CHAT, "onContactDeleted:username=> $username")
                    }

                    //Call back this method when a contact is added
                    override fun onContactAdded(username: String) {
                        Log.d(Constants.TAG_CHAT, "onContactAdded:username=> $username")
                    }
                })
        }
    }

    private fun showPendingRequestsSubTab(
        toShowPendingRequestTab: Boolean,
        tokens: String
    ) {
        if (allChatListViewModel.isApiCalled.value != true) {
            if (allChatListViewModel.isConnectionTabSelected.value == false) {
                if (toShowPendingRequestTab) {
                    GlobalScope.launch {
                        allChatListViewModel.callPendingRequestApi(tokens)
                        binding.headPending.setTextColor(Color.parseColor("#8023EB"))
                        binding.selectedHeadPending.visibility = View.VISIBLE
                        binding.headMyrequests.setTextColor(Color.parseColor("#171A1F"))
                        binding.selectedHeadMyrequests.visibility = View.INVISIBLE
                    }
                } else {
                    GlobalScope.launch {
                        allChatListViewModel.callMyRequestApi(tokens)
                        binding.headMyrequests.setTextColor(Color.parseColor("#8023EB"))
                        binding.selectedHeadMyrequests.visibility = View.VISIBLE

                        binding.headPending.setTextColor(Color.parseColor("#171A1F"))
                        binding.selectedHeadPending.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun showConnectionTab(
        toShowConnectionTab: Boolean,
        token: String
    ) {
        if (allChatListViewModel.isApiCalled.value != true) {
            if (toShowConnectionTab) {
                GlobalScope.launch {
                    allChatListViewModel.getChatList()
                    binding.tvConnectionTag.setTextColor(Color.parseColor("#8023EB"))
                    binding.tvChatTag.visibility = View.VISIBLE

                    binding.tvRequestsTag.setTextColor(Color.parseColor("#171A1F"))
                    binding.requestLay.visibility = View.INVISIBLE
                }
            } else {
                GlobalScope.launch {
                    allChatListViewModel.callPendingRequestApi(token)
                    allChatListViewModel.setPendingTab(true)
                    binding.tvRequestsTag.setTextColor(Color.parseColor("#8023EB"))
                    binding.requestLay.visibility = View.VISIBLE

                    binding.tvConnectionTag.setTextColor(Color.parseColor("#171A1F"))
                    binding.tvChatTag.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onClickAtConnection(item: ConnectionResponse) {
        try {
            val bundle = Bundle()
            bundle.putString("Connection_id", item.id)
            val chatFragment = ChatFragment()
            chatFragment.arguments = bundle
            currentActivity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.flFragment, chatFragment, "ChatFragment")
                ?.commit()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onItemClicked(itemPosition: Int, useType: Int, action: String) {
        val bundle: Bundle = Bundle()

        when (useType) {
            ItemUserListLay -> {
//                val chatItem = allChatListViewModel.userListsData.value?.get(itemPosition) as ChatUserModel
                performClickAction(action, bundle)
            }

            ItemUserChatPendingListLay -> {
                try {
                    val dataItem =
                        (allChatListViewModel.userListsData.value?.get(itemPosition) as com.dev.frequenc.ui_codes.data.pending_request.Data)
                    bundle.putString("Connection_id", dataItem.from_user_id._id)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                performClickAction(action, bundle)
            }

            ItemUserChatRequestsLay -> {
                performClickAction(action, bundle)
            }
        }
    }

    private fun performClickAction(action: String, bundle: Bundle?) {
        when (action) {
            "goChat" -> {
                try {
                    val chatFragment = ChatFragment()
                    chatFragment.arguments = bundle
                    currentActivity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.flFragment, chatFragment, "ChatFragment")
                        ?.commit()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            "goProfile" -> {
                try {
                    val profileFragment = ProfileFragment()
                    profileFragment.arguments = bundle
                    currentActivity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.flFragment, profileFragment, "ProfileFragment")
                        ?.commit()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            "showMenu" -> {
                Toast.makeText(context, "Menu is not designed. ", Toast.LENGTH_SHORT).show()
            }

            "accept" -> {
                sharedPreferences.getString(Constants.Authorization, null)
                    ?.let {
                        GlobalScope.launch {
                            allChatListViewModel.callAcceptApi(
                                token = it,
                                bundle?.getString("Connection_id", null).toString()
                            )
                        }
                    }
            }

            "decline" -> {
                sharedPreferences.getString(Constants.Authorization, null)
                    ?.let {
                        GlobalScope.launch {
                            allChatListViewModel.callRejectApi(
                                token = it,
                                bundle?.getString("Connection_id", null).toString()
                            )
                        }
                    }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        allChatListViewModel.removePresenceListener( object : CallBack {
//            override fun onSuccess() {
//
//            }
//            override fun onError(errorCode: Int, errorMsg: String) {
//
//            }}
//        )
    }
}
