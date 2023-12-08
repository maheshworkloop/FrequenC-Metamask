package com.dev.frequenc.ui_codes.screens.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.dev.frequenc.databinding.FragmentVerifyOtpBinding
import com.dev.frequenc.ui_codes.util.Constants
import com.dev.frequenc.util.DataChangeListener

class VerifyOtpFragment : Fragment() {
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var verifyOtpBinding: FragmentVerifyOtpBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dataChangeListener: DataChangeListener
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences =
            activity?.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!
        verifyOtpBinding = FragmentVerifyOtpBinding.inflate(layoutInflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        activity?.runOnUiThread {
            loginViewModel.currentFragmentTag.observe(viewLifecycleOwner) {
                Log.d("token", "onCreateView: ${loginViewModel._receivedToken}")
                it.let {
                    if (!loginViewModel._receivedToken.isNullOrEmpty() && loginViewModel._receivedToken != "--1"
                        && loginViewModel._receivedToken != "-1"
                    ) {
                        closeKeyboard()
                        sharedPreferences.edit().putBoolean(
                            Constants.isUserTypeRegistered,
                            loginViewModel.isUserTypeRegistered
                        )
                            .putBoolean(Constants.Is_AgoraRegistered, loginViewModel.isAgoraRegistered)
                            .putString(Constants.User_Id, loginViewModel.userId)
                            .putString(Constants.Authorization, loginViewModel._receivedToken)
                            .putString(Constants.AudienceId, loginViewModel.audienceId)
                            .apply()

//                    Toast.makeText(requireContext(),loginViewModel.audienceId, Toast.LENGTH_SHORT).show()

                        dataChangeListener.onDataChange(it, "move")

//                    AppPrefs.putBooleanPref(IS_LOGIN,true,requireContext())
                    }
                }
            }
        }
        activity?.runOnUiThread {
            loginViewModel.isApiCalled.observe(viewLifecycleOwner) {
                it.let { dataChangeListener.onDataChange(it, "api") }
            }
        }
        countDownTimer = object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = millisUntilFinished / 1000
                verifyOtpBinding.btnResendOtp.text =
                    "${secondsUntilFinished.toString()} sec remaining"
                if (loginViewModel.startOtpTimer.value == true) {
                    verifyOtpBinding.dividerOtpLays.visibility = View.GONE
                }
            }

            override fun onFinish() {
                verifyOtpBinding.btnResendOtp.text = "Resend Otp"
                verifyOtpBinding.dividerOtpLays.visibility = View.VISIBLE

                loginViewModel.setStartOtpTimerValue(false)
            }
        }

        loginViewModel.setStartOtpTimerValue(true)

        activity?.runOnUiThread {
            loginViewModel._toastMessage.observe(viewLifecycleOwner) {
                it.let { dataChangeListener.onDataChange(it, "message") }
            }
        }
        activity?.runOnUiThread {
            loginViewModel.startOtpTimer.observe(viewLifecycleOwner) {

                if (loginViewModel.startOtpTimer.value == true) {
                    countDownTimer.start()
                } else {
                    verifyOtpBinding.btnResendOtp.text = "Resend Otp"
                    verifyOtpBinding.dividerOtpLays.visibility = View.VISIBLE
                    loginViewModel.setStartOtpTimerValue(false)
                }
            }
        }

        verifyOtpBinding.btnResendOtp.setOnClickListener {
            if (loginViewModel.startOtpTimer.value == false) {
                activity?.runOnUiThread {
                    sharedPreferences.getString(Constants.PhoneNo, null)?.let { it1 ->
                        loginViewModel.callResendOtpApi(it1)
                    }
                }
            }
        }

        return verifyOtpBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DataChangeListener) {
            dataChangeListener = context
        } else {
            throw IllegalArgumentException("Activity must implement  DataChangeListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        verifyOtpBinding.otp1.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
//            run {
//                verifyOtpBinding.otp2.text = null
//                verifyOtpBinding.otp3.text = null
//                verifyOtpBinding.otp4.text = null
//                verifyOtpBinding.otp5.text = null
//                verifyOtpBinding.otp6.text = null
//                if (requireNotNull(charSequence).length == 1) {
//                    loginViewModel._otp.value = verifyOtpBinding.otp1.text.toString()
//                    verifyOtpBinding.otp2.requestFocus()
//                }
//            }
//        },
//            beforeTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> },
//            afterTextChanged = {})
//        verifyOtpBinding.otp2.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
//            run {
//                verifyOtpBinding.otp3.text = null
//                verifyOtpBinding.otp4.text = null
//                verifyOtpBinding.otp5.text = null
//                verifyOtpBinding.otp6.text = null
//                if (requireNotNull(charSequence).length == 1) {
//                    loginViewModel._otp.value = verifyOtpBinding.otp1.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp2.text.toString()
//                    verifyOtpBinding.otp3.requestFocus()
//                }
//                else { verifyOtpBinding.otp1.requestFocus() }
//            }
//        },
//            beforeTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> },
//            afterTextChanged = {})
//        verifyOtpBinding.otp3.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
//            run {
//                verifyOtpBinding.otp4.text = null
//                verifyOtpBinding.otp5.text = null
//                verifyOtpBinding.otp6.text = null
//                if (requireNotNull(charSequence).length == 1) {
//                    loginViewModel._otp.value = verifyOtpBinding.otp1.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp2.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp3.text.toString()
//                    verifyOtpBinding.otp4.requestFocus()
//                }
//                else { verifyOtpBinding.otp2.requestFocus() }
//            }
//        },
//            beforeTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> },
//            afterTextChanged = {})
//        verifyOtpBinding.otp4.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
//            run {
//                verifyOtpBinding.otp5.text = null
//                verifyOtpBinding.otp6.text = null
//                if (requireNotNull(charSequence).length == 1) {
//                    loginViewModel._otp.value = verifyOtpBinding.otp1.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp2.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp3.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp4.text.toString()
//                    verifyOtpBinding.otp5.requestFocus()
//                }
//                else { verifyOtpBinding.otp3.requestFocus() }
//            }
//        },
//            beforeTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> },
//            afterTextChanged = {})
//        verifyOtpBinding.otp5.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
//            run {
//                verifyOtpBinding.otp6.text = null
//                if (requireNotNull(charSequence).length == 1) {
//                    loginViewModel._otp.value = verifyOtpBinding.otp1.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp2.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp3.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp4.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp5.text.toString()
//                    verifyOtpBinding.otp6.requestFocus()
//                }
//                else { verifyOtpBinding.otp4.requestFocus() }
//            }
//        },
//            beforeTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> },
//            afterTextChanged = {})
//        verifyOtpBinding.otp6.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
//            run {
//                verifyOtpBinding.otp6.text = null
//                if (requireNotNull(charSequence).length == 1) {
//                    loginViewModel._otp.value = verifyOtpBinding.otp1.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp2.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp3.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp4.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp5.text.toString()
//                    loginViewModel._otp.value += verifyOtpBinding.otp6.text.toString()
//                    verifyOtpBinding.btnSubmit.requestFocus()
//                }
//                else { verifyOtpBinding.otp5.requestFocus() }
//            }
//        },
//            beforeTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> },
//            afterTextChanged = {})

        verifyOtpBinding.btnSubmit.setOnClickListener {
                val otps = verifyOtpBinding.otpLays.text.toString()
            if (otps?.isEmpty() == true || otps?.length != 6) {
                verifyOtpBinding.otpLays.requestFocus()
            } else {
                loginViewModel.callVerifyOtpApi(
                    sharedPreferences?.getString(
                        Constants.PhoneNo,
                        null
                    ), otps
                )
            }
        }

        verifyOtpBinding.btnBack.setOnClickListener {
            dataChangeListener.onDataChange("0", "move")
        }

    }

    fun closeKeyboard() {
        try {
            val view = activity?.currentFocus

            if (view != null) {

                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            }
            view?.clearFocus()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}