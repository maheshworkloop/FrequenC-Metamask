package com.dev.frequenc.ui_codes.connect.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.frequenc.ui_codes.data.ConnectionResponse
import com.dev.frequenc.ui_codes.data.myconnection.ConnectionResponseData
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

    private val _connectionList = MutableLiveData<List<ConnectionResponse>>(ArrayList(3))
    val connectionList: LiveData<List<ConnectionResponse>>
        get() {
            return if (_connectionList.value?.isNullOrEmpty() == false) {
                setDataFound(true)
                _connectionList
            } else {
                setDataFound(false)
                MutableLiveData(emptyList())
            }
        }

    private val _userLists = MutableLiveData<List<Any>>(ArrayList(3))
    val userListsData: LiveData<List<Any>>
        get()
//        = _userLists
        {
        return if (_userLists.value?.isNullOrEmpty() == false) {
            setDataFound(true)
            _userLists
        } else {
            setDataFound(false)
            MutableLiveData(emptyList())
        }
        }

    private val _countNumber = MutableLiveData<HashMap<String,Int>>(HashMap())

    val CountNumber: LiveData<HashMap<String,Int>>
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
                            for (data: ConnectionResponseData in response.body()?.data!!) {
//                            adapterLists.add(ConnectionResponse(data.to_user_id,data.status))
//                                _countNumber.postValue(CountNumber.value.put("C"))
                                adapterLists.add(ConnectionResponse(0, true, ""))
                            }
                            _connectionList.postValue(adapterLists)
                        } else {
                            _connectionList.postValue(emptyList())
                        }
                    } else {
                        Log.d(Constants.Error, "onResponse: ${response.body()}")
                    }
                    __isApiCalled.postValue(false)
                }

                override fun onFailure(call: Call<MyConnectionResponse>, t: Throwable) {
                    Log.e(Constants.Error, "onFailure: ", t)
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
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                _userLists.postValue(response.body()?.data)
                            } else {
                                _userLists.postValue(emptyList())
                            }
                        } else {
                            Log.d(Constants.Error, "onResponse: ${response.body()}")
                        }
                        __isApiCalled.postValue(false)
                    }

                    override fun onFailure(call: Call<MyRequestsResponse>, t: Throwable) {
                        Log.e(Constants.Error, "onFailure: ", t)
                        __isApiCalled.postValue(false)
                    }
                })
        }
    }

    fun callPendingRequestApi(tokens: String) {

    }


}
