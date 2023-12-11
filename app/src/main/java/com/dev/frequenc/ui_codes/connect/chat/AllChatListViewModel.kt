package com.dev.frequenc.ui_codes.connect.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev.frequenc.ui_codes.data.ChatUserModel
import com.dev.frequenc.ui_codes.data.ConnectionResponse
import com.dev.frequenc.ui_codes.data.accept_invitation.RequestAcceptResponse
import com.dev.frequenc.ui_codes.data.myconnection.Data
import com.dev.frequenc.ui_codes.data.myconnection.MyConnectionResponse
import com.dev.frequenc.ui_codes.data.myrequests.MyRequestsResponse
import com.dev.frequenc.ui_codes.data.pending_request.PendingRequestResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.ui_codes.util.Constants
import io.agora.CallBack
import io.agora.ContactListener
import io.agora.PresenceListener
import io.agora.ValueCallBack
import io.agora.chat.ChatClient
import io.agora.chat.Conversation
import io.agora.chat.Presence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AllChatListViewModel : ViewModel() {

    private var userLists = ArrayList<String>()
    private val _myRequestCount = MutableLiveData<Int>(0)
    val myRequestCount: LiveData<Int>
        get() = _myRequestCount

    private val _pendingRequestCount = MutableLiveData<Int>(0)
    val pendingRequestCount: LiveData<Int>
        get() = _pendingRequestCount

    private val _toastMessage = MutableLiveData<String>(null)
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private val _isOnlineList = MutableLiveData<List<Boolean>>(ArrayList())
    val isOnlineList: LiveData<List<Boolean>>
        get() = _isOnlineList

    private val _chatCount = MutableLiveData<Int>(0)
    val chatCount: LiveData<Int>
        get() = _chatCount

    private val _requestCount = MutableLiveData<Int>(0)
    val requestCount: LiveData<Int>
        get() = _requestCount

    private val _connectionList = MutableLiveData<List<ConnectionResponse>>(ArrayList(3))
    val connectionList: LiveData<List<ConnectionResponse>>
        get() = _connectionList

    private val _userListsData = MutableLiveData<List<Any>>(ArrayList(3))
    val userListsData: LiveData<List<Any>>
        get()
        = _userListsData

    private val _countNumber = MutableLiveData<HashMap<String, Int>>(HashMap())

    val CountNumber: LiveData<HashMap<String, Int>>
        get() = _countNumber

    private val _isConnectionTabSelected = MutableLiveData<Boolean>(true)

    val isConnectionTabSelected: LiveData<Boolean>
        get() = _isConnectionTabSelected

    private val _isPendingSubTabSelected = MutableLiveData<Boolean>()

    val isPendingSubTabSelected: LiveData<Boolean>
        get() = _isPendingSubTabSelected


    private val _isDataFound = MutableLiveData<Boolean>(true)

    val isDataFound: LiveData<Boolean>
        get() = _isDataFound

    fun setDataFound(toNotShowFound: Boolean) {
        _isDataFound.postValue(toNotShowFound)
    }

    private val __isApiCalled = MutableLiveData<Boolean>(false)
    val isApiCalled: LiveData<Boolean>
        get() = __isApiCalled


    fun setConnectionTab(newValue: Boolean) {
        _isConnectionTabSelected.postValue(newValue)
    }

    fun setPendingTab(newValue: Boolean) {
        if (isConnectionTabSelected.value == false) {
            _isPendingSubTabSelected.postValue(newValue)
        }
    }

    suspend fun callConnectionApi(token: String) {
        coroutineScope {
            __isApiCalled.postValue(true)
            ApiClient.getInstance()?.connectionList(token)
                ?.enqueue(object : Callback<MyConnectionResponse> {
                    override fun onResponse(
                        call: Call<MyConnectionResponse>,
                        response: Response<MyConnectionResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null && response.body()?.data != null && response.body()?.data?.size!! > 0) {
                                val adapterLists = ArrayList<ConnectionResponse>()
                                val userIdsLst = ArrayList<String>()
                                for (data: Data in response.body()?.data!!) {
//                            adapterLists.add(ConnectionResponse(data.to_user_id,data.status))
//                                _countNumber.postValue(CountNumber.value.put("C"))
                                    var images: String = ""
                                    try {
                                        images = data.from_user_id.audience_id.profile_pic
                                    } catch (exs: Exception) {
                                        exs.printStackTrace()
                                    }
                                    adapterLists.add(
                                        ConnectionResponse(
                                            images,
                                            data.from_user_id.fullName,
                                            data.from_user_id._id
                                        )
                                    )
                                    userIdsLst.add(data.id)
                                }
                                userLists = userIdsLst
                                try {
                                    _connectionList.postValue(adapterLists)
                                    _requestCount.postValue(response.body()!!.requestCount)
                                } catch (ex: Exception) {
                                }

                            } else {
                                _connectionList.postValue(ArrayList())
                            }
                        } else {
                            Log.d(
                                Constants.Error,
                                "onResponse:callConnectionApi() ${response.body()}"
                            )
                        }
                        __isApiCalled.postValue(false)
                    }

                    override fun onFailure(call: Call<MyConnectionResponse>, t: Throwable) {
                        Log.e(Constants.Error, "onFailure:callConnectionApi() ", t)
                        __isApiCalled.postValue(false)
                    }
                })
        }
    }


    suspend fun callMyRequestApi(token: String) {
        coroutineScope {
            __isApiCalled.postValue(true)
            ApiClient.getInstance()?.myRequestApi(token)
                ?.enqueue(object : Callback<MyRequestsResponse> {
                    override fun onResponse(
                        call: Call<MyRequestsResponse>,
                        response: Response<MyRequestsResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null && response.body()?.data.isNullOrEmpty() == false && response.body()?.data!!.size > 0) {
                            if (response.body() != null) {
                                try {
                                    _pendingRequestCount.postValue(response.body()!!.pendingCount)
                                    _myRequestCount.postValue(response.body()!!.count)
                                } catch (ex: Exception) {
                                }
                                try {
                                    _userListsData.postValue(response.body()?.data)
                                    setDataFound(true)
                                } catch (ex: Exception) {
                                    setDataFound(false)
                                    _userListsData.postValue(ArrayList())
                                }
                            } else {
                                _userListsData.postValue(ArrayList())
                                setDataFound(false)
                                _pendingRequestCount.postValue(0)
                                _myRequestCount.postValue(0)
                            }
                        } else {
                            Log.d(
                                Constants.Error,
                                "onResponse: callMyRequestApi() ${response.body()}"
                            )
                        }
                        __isApiCalled.postValue(false)
                    }

                    override fun onFailure(call: Call<MyRequestsResponse>, t: Throwable) {
                        Log.e(Constants.Error, "onFailure: callMyRequestApi() ", t)
                        __isApiCalled.postValue(false)
                    }
                })
        }
    }

    suspend fun getChatList() {
        coroutineScope {
            __isApiCalled.postValue(true)
            try {
                Log.d(Constants.TAG_CHAT, "begin to getChatlists ...")
//                Log.d("chats", "getContactList:  $usernames")
                val contactLists: MutableMap<String, Conversation>? =
                    ChatClient.getInstance().chatManager().allConversations

                if (contactLists != null) {
                    val chatListVals = ArrayList<ChatUserModel>()
                    userLists.clear()

                    for (contact: Conversation in contactLists.values) {
                        contact.let {
                            val lastMessage: String = it.lastMessage.body.toString()
                            val userName: String = it.lastMessage.from
                            val toUserName: String = it.lastMessage.to
                            val unreadMessagesCount: String = it.unreadMsgCount.toString()
                            val time = it.lastMessage.localTime() / 6000000
                            val user_image = it.lastMessage.localTime().toInt().toString()
                            chatListVals.add(
                                ChatUserModel(
                                    userName,
                                    toUserName,
                                    time.toInt().toString(),
                                    user_image,
                                    lastMessage
                                )
                            )
                            userLists.add(userName)
                        }
//
//                        ChatClient.getInstance().userInfoManager().fetchUserInfoByUserId(userLists.toArray() as Array<out String>?, object : ValueCallBack<Map<String, UserInfo>> {
//                            override fun onSuccess(value: Map<String, UserInfo>?) {
//                                value?.values?.forEachIndexed{ profileIndex, profileInfos ->
//                                    chatListVals[profileIndex].chatPersonImage = profileInfos.avatarUrl.toString()
//                                }
//                            }
//
//                            override fun onError(error: Int, errorMsg: String?) {
//                                Log.d("ds", "onError: ")
//                            }
//                        })
                    }
                    _chatCount.postValue(chatListVals.size)

                    _userListsData.postValue(chatListVals)
                    __isApiCalled.postValue(false)
                    setDataFound(true)
                } else {
                    _userListsData.postValue(ArrayList())
                    _chatCount.postValue(0)
                    __isApiCalled.postValue(false)
                    setDataFound(false)
                }

            } catch (exs: Exception) {
                exs.printStackTrace()
            }
        }
    }


    suspend fun callPendingRequestApi(tokens: String) {
        coroutineScope {
            ApiClient.getInstance()?.pendingRequests(tokens)?.enqueue(object :
                Callback<PendingRequestResponse> {
                override fun onResponse(
                    call: Call<PendingRequestResponse>,
                    response: Response<PendingRequestResponse>
                ) {
                    if (response.body() != null && response.body()?.data.isNullOrEmpty() == false && response.body()?.data!!.size > 0) {
                        try {
                            _requestCount.postValue(response.body()?.count)
                            _pendingRequestCount.postValue(response.body()?.requestCount)
                            _userListsData.postValue(response.body()?.data)
                            setDataFound(true)
                        } catch (ex: Exception) {
                            setDataFound(false)
                            _userListsData.postValue(ArrayList())
                            _pendingRequestCount.postValue(0)
                        }
                    } else {
                        _userListsData.postValue(ArrayList())
                        _pendingRequestCount.postValue(0)
                        setDataFound(false)
                    }
                }

                override fun onFailure(call: Call<PendingRequestResponse>, t: Throwable) {
                    _userListsData.postValue(ArrayList())
                    _pendingRequestCount.postValue(0)
                    setDataFound(false)
                    Log.e(Constants.ApiError, "onFailure:callPendingRequestApi ", t)
                }
            })

            try {
                var usernamesList = ChatClient.getInstance().contactManager().allContactsFromServer
                Log.d(Constants.TAG_CHAT, "callPendingRequestApi:  " + usernamesList)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    suspend fun getConnectionListWithPresence() {
        coroutineScope {
            ChatClient.getInstance().presenceManager().subscribePresences(
                userLists,
                (1 * 24 * 3600).toLong(),
                object : ValueCallBack<List<Presence?>?> {
                    override fun onSuccess(presences: List<Presence?>?) {
                        val oldConnectionLists = ArrayList<Boolean>()
                        presences?.forEachIndexed { index, presenceItem ->
                            _connectionList.value?.get(index)?.let {
                                var isOnline = false
                                presenceItem?.statusList?.let { its ->
                                    for (presenceItm in its) {
                                        if (presenceItm.value.equals("1")) {
                                            isOnline = true
                                            return
                                        }
                                    }
                                }
                                oldConnectionLists.add(isOnline)
                            }
                            _isOnlineList.postValue(oldConnectionLists)
                        }
                    }

                    override fun onError(errorCode: Int, errorMsg: String) {
                        Log.e(
                            Constants.ApiError,
                            "onFailure:getConnectionListWithPresence ",
                            Throwable(errorMsg)
                        )
                    }
                })
        }

    }

    fun setContactChangeListener(contactListener: ContactListener) {
        ChatClient.getInstance().contactManager().setContactListener(contactListener)
    }

    suspend fun callAcceptApi(token: String, connect_id: String) {
        coroutineScope {
            __isApiCalled.postValue(true)
            ApiClient.getInstance()?.acceptInvitation(token = token, connect_id)
                ?.enqueue(object : Callback<RequestAcceptResponse> {
                    override fun onResponse(
                        call: Call<RequestAcceptResponse>,
                        response: Response<RequestAcceptResponse>
                    ) {
                        if (response.body() != null && response.body()?.data != null && response.body()?.data?.request_status?.equals(
                                "approved"
                            ) == true
                        ) {
                            GlobalScope.launch {
                            try {
                                _toastMessage.postValue("Accepted")
                                    callPendingRequestApi(token)
                            } catch (ex: Exception) {
                            }
                        }
                        }
                        __isApiCalled.postValue(false)
                    }

                    override fun onFailure(call: Call<RequestAcceptResponse>, t: Throwable) {
                        Log.e(Constants.ApiError, "onFailure:callAcceptApi ", t)
                        __isApiCalled.postValue(false)
                    }
                })
            ChatClient.getInstance().contactManager().acceptInvitation(connect_id)
        }
    }

    suspend fun callRejectApi(token: String, connect_id: String) {
        coroutineScope {
            __isApiCalled.postValue(true)
            ApiClient.getInstance()?.rejectInvitation(token = token, connect_id)
                ?.enqueue(object : Callback<RequestAcceptResponse> {
                    override fun onResponse(
                        call: Call<RequestAcceptResponse>,
                        response: Response<RequestAcceptResponse>
                    ) {
                        if (response.body() != null && response.body()?.data != null && response.body()?.data?.request_status?.equals(
                                "approved"
                            ) == false
                        ) {
                            GlobalScope.launch {
                                try {
                                    _toastMessage.postValue("Rejected.")
                                    callPendingRequestApi(token)
                                } catch (ex: Exception) {
                                    Log.d(
                                        Constants.ApiError,
                                        "onResponse:callAcceptApi $response.body()"
                                    )
                                }
                            }
                        }
                        __isApiCalled.postValue(false)
                    }

                    override fun onFailure(call: Call<RequestAcceptResponse>, t: Throwable) {
                        Log.e(Constants.ApiError, "onFailure:callAcceptApi ", t)
                        __isApiCalled.postValue(false)
                    }
                })
            ChatClient.getInstance().contactManager().declineInvitation(connect_id)
        }
    }

    fun removePresenceListener(callbacks: CallBack) {
        ChatClient.getInstance().presenceManager().unsubscribePresences(userLists, callbacks)
    }

    suspend fun setOnPresenceChange() {
        coroutineScope {
            ChatClient.getInstance().presenceManager().addListener(object : PresenceListener {
                override fun onPresenceUpdated(presences: MutableList<Presence>?) {
                    val oldConnectionLists = ArrayList<Boolean>()
                    presences?.forEachIndexed { index, presenceItem ->
                        _connectionList.value?.get(index)?.let {
                            var isOnline = false
                            presenceItem?.statusList?.let { its ->
                                for (presenceItm in its) {
                                    if (presenceItm.value.equals("1")) {
                                        isOnline = true
                                        return
                                    }
                                }
                            }
                            oldConnectionLists.add(isOnline)
                        }
                        _isOnlineList.postValue(oldConnectionLists)
                    }
                }
            })
        }
    }

    suspend fun fetchPresenceStateLists() {
        coroutineScope {
            ChatClient.getInstance().presenceManager()
                .fetchPresenceStatus(userLists, object : ValueCallBack<List<Presence?>?> {
                    override fun onSuccess(presences: List<Presence?>?) {
                        val oldConnectionLists = ArrayList<Boolean>()
                        presences?.forEachIndexed { index, presenceItem ->
                            _connectionList.value?.get(index)?.let {
                                var isOnline = false
                                presenceItem?.statusList?.let { its ->
                                    for (presenceItm in its) {
                                        if (presenceItm.value.equals("1")) {
                                            isOnline = true
                                            return
                                        }
                                    }
                                }
                                oldConnectionLists.add(isOnline)
                            }
                        }
                        _isOnlineList.postValue(oldConnectionLists)
                    }

                    override fun onError(errorCode: Int, errorMsg: String) {}
                })
        }
    }


}
