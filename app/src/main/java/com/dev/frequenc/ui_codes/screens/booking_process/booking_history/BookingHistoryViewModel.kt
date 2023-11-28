package com.dev.frequenc.ui_codes.screens.booking_process.booking_history

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.frequenc.ui_codes.data.WalletBalenceData
import com.dev.frequenc.ui_codes.data.transactionlist.TransactionListRes
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingHistoryViewModel : ViewModel() {
    private val _walletBalence = MutableLiveData<WalletBalenceData>()
    private val _transactionListApi = MutableLiveData<TransactionListRes>()
    val transactionListAp: LiveData<TransactionListRes>
        get() = _transactionListApi

    val walletBalanceDataLists : LiveData<WalletBalenceData>
        get() = _walletBalence

    private val __isUpcomingTabSelected = MutableLiveData<Boolean>(false)
    val isUpcomingTabSelected: LiveData<Boolean>
        get() = __isUpcomingTabSelected

    private val _isApiCalled = MutableLiveData<Boolean>(false)
    val isApiCalled: LiveData<Boolean>
        get() = _isApiCalled

    fun setApiCall(isApiCalled: Boolean) {
        _isApiCalled.postValue(isApiCalled)
    }

    fun callTransactionslistApi(token: String) {
        viewModelScope.launch {
        _isApiCalled.postValue( true)
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
                            _isApiCalled.postValue(false )
                        } else {
                                Log.d(
                                Constants.Error,
                                "onResponse: ${
                                    response.errorBody()!!.charStream().read().toString()
                                }"
                            )
                            _isApiCalled.postValue(false)
                        }
                }

                override fun onFailure(call: Call<TransactionListRes>, t: Throwable) {
                    Log.e(Constants.Error, "onFailure: ", t)
                    _isApiCalled.postValue(false)
                }
            })
        }
    }


    fun setUpcomingTabValue(toSelectUpcomingTabValue: Boolean) {
        __isUpcomingTabSelected.postValue(toSelectUpcomingTabValue)
    }

    fun getWalletBalence(metamaskAddress: String) {
        _isApiCalled.postValue(true)
        viewModelScope.launch {
        ApiClient.getInstance()?.metamaskBalence(metamaskAddress)?.enqueue(object : Callback<WalletBalenceData>{
            override fun onResponse(call: Call<WalletBalenceData>, response: Response<WalletBalenceData>) {
                if (response.isSuccessful && response.body() != null ) {
                        _walletBalence.value = response.body()
                    _isApiCalled.postValue(false)
                }
                else {
                    Log.d(
                        Constants.Error,
                        "onResponse: ${
                            response.errorBody()!!.charStream().read().toString()
                        }"
                    )
                    _isApiCalled.postValue(false)
                }
            }

            override fun onFailure(call: Call<WalletBalenceData>, t: Throwable) {
                Log.e(Constants.Error, "onFailure: ", t)
                _isApiCalled.postValue(false)
            }
        }) }
    }

}
