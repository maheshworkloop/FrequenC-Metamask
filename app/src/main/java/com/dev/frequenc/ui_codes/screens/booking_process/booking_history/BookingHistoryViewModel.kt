package com.dev.frequenc.ui_codes.screens.booking_process.booking_history

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.frequenc.ui_codes.data.transactionlist.TransactionListRes
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingHistoryViewModel : ViewModel() {
    private val _transactionListApi = MutableLiveData<TransactionListRes>()
    val transactionListAp: LiveData<TransactionListRes>
        get() = _transactionListApi

    private val __isUpcomingTabSelected = MutableLiveData<Boolean>(false)
    val isUpcomingTabSelected: LiveData<Boolean>
        get() = __isUpcomingTabSelected

    private val _isApiCalled = MutableLiveData<Boolean>(false)
    val isApiCalled: LiveData<Boolean>
        get() = _isApiCalled

    fun setApiCall(isApiCalled: Boolean) {
        _isApiCalled.value = isApiCalled
    }

    fun callTransactionslistApi(token: String) {
        viewModelScope.launch {
        _isApiCalled.value = true
        ApiClient.getInstance()!!.getTransactionlist(token)
            .enqueue(object : Callback<TransactionListRes> {
                override fun onResponse(
                    call: Call<TransactionListRes>,
                    response: Response<TransactionListRes>
                ) {
                        if (response.isSuccessful) {
                            response.body().let {
                                _transactionListApi.value = response.body()
                            }
                            _isApiCalled.value = false
                        } else {
                            Log.d(
                                Constants.Error,
                                "onResponse: ${
                                    response.errorBody()!!.charStream().read().toString()
                                }"
                            )
                            _isApiCalled.value = false
                        }
                }

                override fun onFailure(call: Call<TransactionListRes>, t: Throwable) {
                    Log.e(Constants.Error, "onFailure: ", t)
                    _isApiCalled.value = false
                }
            })
        }
    }


    fun setUpcomingTabValue(toSelectUpcomingTabValue: Boolean) {
        __isUpcomingTabSelected.value = toSelectUpcomingTabValue
    }

}
