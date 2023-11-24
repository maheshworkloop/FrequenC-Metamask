package com.dev.frequenc.ui_codes.screens.Dashboard.savedevent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.BookmarkEventResponse
import com.dev.frequenc.ui_codes.data.req.SavedEventsReq
import com.dev.frequenc.ui_codes.data.saved_event.SavedEventResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedEventViewModel : ViewModel() {
    private val _savedEventResponse = MutableLiveData<SavedEventResponse>()
    val savedEventResponse: LiveData<SavedEventResponse>
        get() = _savedEventResponse
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage
    private val _isApiCalled = MutableLiveData<Boolean>()
    val isApiCalled: LiveData<Boolean>
        get() = _isApiCalled

    fun callSavedEventApi(tokens: String) {
        _isApiCalled.value = true
        ApiClient.getInstance()!!.savedEvents(tokens)
            .enqueue(object : Callback<SavedEventResponse> {
                override fun onResponse(
                    call: Call<SavedEventResponse>,
                    response: Response<SavedEventResponse>
                ) {
                    _isApiCalled.value = false
                    if (response.isSuccessful) {
                        viewModelScope.launch {
                            _savedEventResponse.value = response.body()
                        }
                    } else {
                        Log.d(Constants.ApiError, "onResponse: " + response.message().toString())
                        _toastMessage.value =
                            "code: ${response.code()}, message =${response.errorBody()?.string()}"
                    }
                }

                override fun onFailure(call: Call<SavedEventResponse>, t: Throwable) {
                    _isApiCalled.value = false
                    _toastMessage.value = R.string.error_message.toString()
                    Log.e(Constants.ApiError, "onFailure: callSavedEventApi()", t)
                }
            })
    }


    fun callBookmarkEventApi(position: Int, token: String) {
        val itemBody = savedEventResponse.value?.get(position)
        token.let {
            if (itemBody != null) {
                ApiClient.getInstance()?.bookmarkEvent(
                    token,
                    savedEventsReq = SavedEventsReq(
                        event_id = itemBody._id,
                        is_bookmark = false
                    )
                )?.enqueue(object : Callback<BookmarkEventResponse> {
                    override fun onResponse(
                        call: Call<BookmarkEventResponse>,
                        response: Response<BookmarkEventResponse>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                _toastMessage.value = "Event removed from Saved Event"
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                            callSavedEventApi(token)
                        } else {
                            _toastMessage.value = "code: ${response.code()}, message =${response.errorBody()?.string()}"
                        }
                    }

                    override fun onFailure(call: Call<BookmarkEventResponse>, t: Throwable) {
                        _toastMessage.value = R.string.error_message.toString()
                        Log.e(Constants.ApiError, "onFailure: callBookmarkEventApi()", t)
                    }
                })
            }
        }
    }

}