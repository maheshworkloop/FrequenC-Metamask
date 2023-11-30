package com.dev.frequenc.ui_codes.connect.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.frequenc.ui_codes.data.ConnectionResponse
import com.dev.frequenc.ui_codes.data.myconnection.Data
import com.dev.frequenc.ui_codes.data.myconnection.MyConnectionResponse
import com.dev.frequenc.ui_codes.data.myrequests.MyRequestsResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllChatListViewModel : ViewModel() {

    private val _myRequestCount = MutableLiveData<Int>(0)
    val myRequestCount: LiveData<Int>
        get () = _myRequestCount

    private val _pendingRequestCount = MutableLiveData<Int>(0)
    val pendingRequestCount: LiveData<Int>
        get () = _pendingRequestCount

    private val _chatCount = MutableLiveData<Int>(0)
    val chatCount: LiveData<Int>
        get () = _chatCount

    private val _requestCount = MutableLiveData<Int>(0)
    val requestCount: LiveData<Int>
        get () = _requestCount

    private val _connectionList = MutableLiveData<List<ConnectionResponse>>(ArrayList(3))
    val connectionList: LiveData<List<ConnectionResponse>>
        get() = _connectionList

    private val _userLists = MutableLiveData<List<Any>>(ArrayList(3))
    val userListsData: LiveData<List<Any>>
        get()
        = _userLists

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

    fun callConnectionApi(token: String) {
        __isApiCalled.postValue(true)
        ApiClient.getInstance()?.connectionList(token)
            ?.enqueue(object : Callback<MyConnectionResponse> {
                override fun onResponse(
                    call: Call<MyConnectionResponse>,
                    response: Response<MyConnectionResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null && response.body()?.data != null) {
                            val adapterLists = ArrayList<ConnectionResponse>()
                            for (data: Data in response.body()?.data!!) {
//                            adapterLists.add(ConnectionResponse(data.to_user_id,data.status))
//                                _countNumber.postValue(CountNumber.value.put("C"))
                                var images: String = ""
                                try {
                                    images = data.to_user_id.audience_id.profile_pic
                                } catch (exs: Exception) {
                                    exs.printStackTrace()
                                }
                                adapterLists.add(ConnectionResponse(images, true, ""))
                            }
                            try {
                                _connectionList.postValue(adapterLists)
                                setDataFound(true)
                            } catch (ex: Exception) {
                                setDataFound(false)
                                _connectionList.postValue(emptyList())
                            }
                        } else {
                            _connectionList.postValue(emptyList())
                            setDataFound(false)
                        }
                    } else {
                        Log.d(Constants.Error, "onResponse:callConnectionApi() ${response.body()}")
                    }
                    __isApiCalled.postValue(false)
                }

                override fun onFailure(call: Call<MyConnectionResponse>, t: Throwable) {
                    Log.e(Constants.Error, "onFailure:callConnectionApi() ", t)
                    __isApiCalled.postValue(false)
                }
            })
    }

    fun callMyRequestApi(token: String) {
        viewModelScope.launch {
            __isApiCalled.postValue(true)
            ApiClient.getInstance()?.myRequestApi(token)
                ?.enqueue(object : Callback<MyRequestsResponse> {
                    override fun onResponse(
                        call: Call<MyRequestsResponse>,
                        response: Response<MyRequestsResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null && response.body()?.data.isNullOrEmpty() == false) {
                            if (response.body() != null) {
                                try {
                                    _requestCount.postValue(response.body()?.count)
                                    _myRequestCount.postValue(response.body()?.count)
                                    _pendingRequestCount.postValue(response.body()?.count)
                                    _chatCount.postValue(response.body()?.count)
                                }
                                catch (ex: Exception) {}
                                try {
                                    _userLists.postValue(response.body()?.data)
                                    setDataFound(true)
                                    _userLists
                                } catch (ex: Exception) {
                                    setDataFound(false)
                                    MutableLiveData(emptyList())
                                }
                            } else {
                                _userLists.postValue(emptyList())
                                setDataFound(false)
                                MutableLiveData(emptyList())
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

    fun callPendingRequestApi(tokens: String) {

    }


}
