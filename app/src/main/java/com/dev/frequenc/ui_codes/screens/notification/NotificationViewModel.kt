package com.dev.frequenc.ui_codes.screens.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.frequenc.ui_codes.data.notification.NotificationResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.ui_codes.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel : ViewModel() {
    private val __filterType = MutableLiveData<String>("all")
    val filterType: LiveData<String>
        get() = __filterType
    private val __isApiCalled = MutableLiveData<Boolean>(false)
    val isApiCalled: LiveData<Boolean>
        get() = __isApiCalled
    private val _notificationResponse = MutableLiveData<NotificationResponse>()
    private val _toastMessage = MutableLiveData<String>(null)
    val toastMessage: LiveData<String>
        get() = _toastMessage
    val NotificationResponse : LiveData<NotificationResponse>
        get() = _notificationResponse

    fun setApiCalled(toShow: Boolean) {
        __isApiCalled.value = toShow
    }

    fun callNotificationApi(tokens: String) {
        viewModelScope.launch {
            ApiClient.getInstance()!!.notification(tokens = tokens)
                .enqueue(object : Callback<NotificationResponse> {
                    override fun onResponse(
                        call: Call<NotificationResponse>,
                        response: Response<NotificationResponse>
                    ) {
                        if (response.isSuccessful) {
                            _notificationResponse.value = response.body()
                        } else {
                            __isApiCalled.value = false
                            _toastMessage.value = ("code: ${response.code()}, ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                        __isApiCalled.value = false
                        Log.e(Constants.ApiError, "onFailure: ", t)
                    }
                })
        }
    }

    fun setFilterTypeKey(filterKey: String) {
        __filterType.value = filterKey
    }

//    fun callNotificationApi
}