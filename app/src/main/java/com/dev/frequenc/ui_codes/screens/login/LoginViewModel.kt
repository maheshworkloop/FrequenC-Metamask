package com.dev.frequenc.ui_codes.screens.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dev.frequenc.ui_codes.data.RegisterUserResponse
import com.dev.frequenc.ui_codes.data.confirmuserotp.ConfirmOtpResponse
import com.dev.frequenc.ui_codes.util.Constants
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Exception

class LoginViewModel : ViewModel() {
    var userId: String = ""
    private val _startOtpTimer = MutableLiveData<Boolean>(false)
    val startOtpTimer : LiveData<Boolean>
        get() = _startOtpTimer
    var _mob_no = MutableLiveData<String>(null)
    var _receivedToken = "--1"
    var isAgoraRegistered = false
    var isUserTypeRegistered = false
//    private val mobile_no: LiveData<String> get() =  _mob_no
//    private val otp : MutableLiveData<String> get() = _otp

    val _toastMessage = MutableLiveData<String>("")

    private val _currentFragmentTag = MutableLiveData<String>().apply { value = "0" }
    var audienceId: String = ""
    val currentFragmentTag: LiveData<String>
        get() = _currentFragmentTag

    fun setCurrentFragmentTag(currentFragment: String) {
        viewModelScope.launch {
            _currentFragmentTag.value = currentFragment
        }
    }

    private val __isApiCalled = MutableLiveData<Boolean>().apply { value = false }
    val isApiCalled: LiveData<Boolean>
        get() = __isApiCalled
//
//    var isRegisterApiCalled = MutableLiveData<Boolean>(false)
//    var isVerifyOtpApiHit = MutableLiveData<Boolean>(false)
//    var isUserTypeApiHit = MutableLiveData<Boolean>(false)
//
//    fun login() {
//        viewModelScope.launch {
//            moveToHome()
//        }
//    }
//
//    private suspend fun moveToHome() {
//        if (_loginResult.value == true) {
//
//        }
//    }

    fun callRegisterApi(phone_no: String) {
        viewModelScope.launch {
        __isApiCalled.value = true
        ApiClient.getInstance()!!.register(registerUserReq(phone_no))!!
            .enqueue(object : Callback<RegisterUserResponse> {
                override fun onResponse(
                    call: Call<RegisterUserResponse>,
                    response: Response<RegisterUserResponse>
                ) {
                        __isApiCalled.value = false
                        if (response.isSuccessful && response.body() != null) {
                            _toastMessage.value = response.body()?.message.toString()
                            moveToOtpVerification()
                        } else {
                            if (response.body() != null) Log.d(
                                Constants.Error,
                                "onResponse:callRegisterApi() " + response.body()
                            )
                            _toastMessage.value = response?.errorBody()?.string().toString()
                        }
                    }

                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                    _toastMessage.value = t.localizedMessage
                    __isApiCalled.value = false
                    Log.e(Constants.Error, "onFailure: ", t)
                }

            })
        }
    }

    data class registerUserReq(var phone_no: String)

    fun moveToOtpVerification() {
        _currentFragmentTag.value = "1"
    }

    fun moveToLogin() {
        _currentFragmentTag.value = "0"
    }

    fun moveToUserType() {
        _currentFragmentTag.value = "2"
    }

    fun moveToHome() {
        _currentFragmentTag.value = "-1"
    }

    fun moveToIntro() {
        _currentFragmentTag.value = "3"
    }

    fun callVerifyOtpApi(phone_no: String?, otp: String) {
        viewModelScope.launch {
            __isApiCalled.value = true
            otp.let {
                phone_no?.let { it1 ->
                    ApiClient.getInstance()!!
                        .confirmUserOtp(VerifyOtpReq(phone_no = it1, otp = it.toInt()))!!
                        .enqueue(object : Callback<ConfirmOtpResponse> {
                            override fun onResponse(
                                call: Call<ConfirmOtpResponse>,
                                response: Response<ConfirmOtpResponse>
                            ) =
                                run {
                                    __isApiCalled.value = false
                                    if (response.isSuccessful) {
                                        try {
                                            isAgoraRegistered = response.body()!!.data.isAgoraId
                                        }
                                        catch (ex: Exception ) {
                                            ex.printStackTrace()
                                        }
                                        try {
                                            _receivedToken =
                                                response.headers().get(Constants.Authorization)
                                                    .toString()
                                            userId = response.body()!!.data.user.id
                                        }
                                        catch (ex: Exception ) { ex.printStackTrace()}

                                        try {
                                            if (response.body()!!.data.user.user_type.equals("audience")) {
                                                isUserTypeRegistered = true
                                                if (!response.body()!!.data.user.audience_id.isNullOrEmpty()) {
                                                    audienceId =
                                                        response.body()!!.data.user.audience_id
                                                }
                                                moveToHome()
                                            } else if (response.body()!!.data.user.user_type.equals(
                                                    "venue"
                                                )
                                            ) {
                                                isUserTypeRegistered = true
                                                if (!response.body()!!.data.user.venue_id.isNullOrEmpty()) {
                                                    audienceId =
                                                        response.body()!!.data.user.venue_id[0].toString()
                                                }
                                                moveToHome()
                                            } else if (response.body()!!.data.user.user_type.equals(
                                                    "artist"
                                                )
                                            ) {
                                                isUserTypeRegistered = true
                                                if (!response.body()!!.data.user.audience_id.isNullOrEmpty()) {
                                                    audienceId =
                                                        response.body()!!.data.user.audience_id
                                                }
                                                moveToHome()
                                            } else if (response.body()!!.data.user.user_type.equals("vendor")
                                            ) {
                                                isUserTypeRegistered = true
                                                if (!response.body()!!.data.user.audience_id.isNullOrEmpty()) {
                                                    audienceId =
                                                        response.body()!!.data.user.audience_id
                                                }
                                                moveToHome()
                                            } else {
                                                isUserTypeRegistered = false
//                                                callUpdateUserTypeApi(
//                                                    _receivedToken,
//                                                    phone_no,
//                                                    "audience"
//                                                )
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            isUserTypeRegistered = false
//                                            moveToUserType()
//                                            callUpdateUserTypeApi(
//                                                _receivedToken,
//                                                phone_no,
//                                                "audience"
//                                            )
                                        }
                                    } else {
                                        Log.d(
                                            Constants.Error,
                                            "onResponse:callVerifyOtpApi() code:${response.code()} &&  message= ${
                                                response.errorBody()?.byteStream()?.read()
                                            }"
                                        )
                                        _toastMessage.value =
                                            response.errorBody()?.string().toString()
                                    }
                                    if (_toastMessage.value == null) {
                                        _toastMessage.value = response.message().toString()
                                    }
                                }

                            override fun onFailure(
                                call: Call<ConfirmOtpResponse>,
                                t: Throwable
                            ) {
                                _toastMessage.value = t.localizedMessage
                                __isApiCalled.value = false
                                Log.e(Constants.Error, "onFailure: ", t)
                            }
                        })
                }
            }
        }
    }

    data class VerifyOtpReq(val phone_no: String, val otp: Int)

//    fun callUpdateUserTypeApi(tokens: String, phone_no: String, userTypeKey: String) {
//        viewModelScope.launch {
//        __isApiCalled.value = true
//        tokens.let {
//            phone_no.let {
//                userTypeKey.let {
//                    ApiClient.getInstance()!!
//                        .updateUserType(
//                            tokens,
//                            UpdateUserReq(phone_no = phone_no, user_type = userTypeKey)
//                        )!!
//                        .enqueue(object : Callback<UpdateUserTypeResponse> {
//                            override fun onResponse(
//                                call: Call<UpdateUserTypeResponse>,
//                                response: Response<UpdateUserTypeResponse>
//                            ): Unit = run {
//                                    __isApiCalled.value = false
//                                    if (response.isSuccessful) {
//                                        try {
//                                            if (response.body()!!.data.user.user_type.equals("audience")) {
//                                                isUserTypeRegistered = true
//                                                if (!response.body()!!.data.user.audience_id.isNullOrEmpty()) {
//                                                    audienceId =
//                                                        response.body()!!.data.user.audience_id
//                                                }
//                                            } else if (response.body()!!.data.user.user_type.equals(
//                                                    "venue"
//                                                )
//                                            ) {
//                                                isUserTypeRegistered = true
//                                                if (!response.body()!!.data.user.venue_id.isNullOrEmpty()) {
//                                                    audienceId =
//                                                        response.body()!!.data.user.venue_id[0].toString()
//                                                }
//                                            } else if (response.body()!!.data.user.user_type.equals(
//                                                    "artist"
//                                                )
//                                            ) {
//                                                isUserTypeRegistered = true
//                                                if (!response.body()!!.data.user.artist_id.isNullOrEmpty()) {
//                                                    audienceId =
//                                                        response.body()!!.data.user.artist_id
//                                                }
//                                            } else if (response.body()!!.data.user.user_type.equals(
//                                                    "vendor"
//                                                )
//                                            ) {
//                                                isUserTypeRegistered = true
//                                                if (!response.body()!!.data.user.vendor_id.isNullOrEmpty()) {
//                                                    audienceId =
//                                                        response.body()!!.data.user.artist_id
//                                                }
//                                            }
//                                        } catch (e: Exception) {
//                                            e.printStackTrace()
//                                        }
//                                        //                    if (!response.body()!!.data.isUserType.toString().isNullOrEmpty()) {
//                                        //                    }
//                                        moveToHome()
//                                    } else {
//                                        Log.d(
//                                            Constants.Error,
//                                            "onResponse:callUpdateUserTypeApi() " + response.body()
//                                        )
//                                        _toastMessage.value =
//                                            response?.errorBody()?.string().toString()
//                                    }
//                                }
//
//                            override fun onFailure(
//                                call: Call<UpdateUserTypeResponse>,
//                                t: Throwable
//                            ) {
//                                __isApiCalled.value = false
//                                _toastMessage.value = t.localizedMessage
//                                Log.e(Constants.Error, "onFailure:callUpdateUserTypeApi ", t)
//                            }
//                        })
//                }
//                }
//            }
//        }
//    }

    fun callResendOtpApi(phone_no: String) {
        __isApiCalled.value = true
        _startOtpTimer.value = true
        viewModelScope.launch {
            ApiClient.getInstance()?.registerAttendee(JSONObject().put("phone_no", phone_no))
                ?.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        __isApiCalled.value = false
                        _startOtpTimer.value = false
                        if (response.isSuccessful) {
                            _toastMessage.value = response.body().toString()
                        } else {
                            Log.d(
                                Constants.Error,
                                "onResponse:callResendOtpApi() " + response.body()
                            )
                            _toastMessage.value =
                                response?.errorBody()?.string().toString()
                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        __isApiCalled.value = false
                        _startOtpTimer.value = false
                        _toastMessage.value = t.localizedMessage
                        Log.e(Constants.Error, "onFailure:callResendOtpApi ", t)
                    }
                })
        }
    }

    fun setStartOtpTimerValue(isResendOtpTimerStartd: Boolean) {
        _startOtpTimer.value = isResendOtpTimerStartd
    }
}

data class UpdateUserReq(val phone_no: String, val user_type: String)
class LoginViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")

            return LoginViewModel() as T

        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }

}

