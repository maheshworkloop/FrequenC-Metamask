package com.dev.frequenc.ui_codes.connect.chat

import android.Manifest
import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.agorademo2.LogUtils
import com.dev.agorademo2.PermissionsManager
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityHomeBinding
import io.agora.CallBack
import io.agora.ConnectionListener
import io.agora.Error
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage
import io.agora.chat.ChatOptions
import io.agora.chat.uikit.EaseUIKit
import io.agora.chat.uikit.chat.EaseChatFragment
import io.agora.chat.uikit.chat.interfaces.OnChatExtendMenuItemClickListener
import io.agora.chat.uikit.chat.interfaces.OnMessageSendCallBack
import io.agora.chat.uikit.menu.EaseChatType
import io.agora.cloud.HttpClientManager
import org.json.JSONObject


class HomeActivity : AppCompatActivity() {

    private lateinit var connectionListener: ConnectionListener
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater, null , false)
        setContentView(binding.root)
        initView()
        requestPermissions()
        initSDK()
        addConnectionListener()

    }

    private fun initView() {
        binding.tvLog.setMovementMethod(ScrollingMovementMethod())
    }

    private fun requestPermissions() {
        checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, 110)
    }

    //=================== init SDK start ========================
    private fun initSDK() {
        val options = ChatOptions()
        // Set your appkey applied from Agora Console
        val sdkAppkey = getString(R.string.app_key)
        if (TextUtils.isEmpty(sdkAppkey)) {
            Toast.makeText(
                this@HomeActivity,
                "You should set your AppKey first!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        // Set your appkey to options
        options.appKey = sdkAppkey
        // Set whether confirmation of delivery is required by the recipient. Default: false
        options.requireDeliveryAck = true
        // Set not to log in automatically
        options.autoLogin = false
        // Use UI Samples to initialize Chat SDK
        EaseUIKit.getInstance().init(this, options)
        // Make Chat SDK debuggable
        ChatClient.getInstance().setDebugMode(true)
    }

    //=================== init SDK end ========================
    //================= SDK listener start ====================
    private fun addConnectionListener() {
        connectionListener = object : ConnectionListener {
            override fun onConnected() {}
            override fun onDisconnected(error: Int) {
                if (error == Error.USER_REMOVED) {
                    onUserException("account_removed")
                } else if (error == Error.USER_LOGIN_ANOTHER_DEVICE) {
                    onUserException("account_conflict")
                } else if (error == Error.SERVER_SERVICE_RESTRICTED) {
                    onUserException("account_forbidden")
                } else if (error == Error.USER_KICKED_BY_CHANGE_PASSWORD) {
                    onUserException("account_kicked_by_change_password")
                } else if (error == Error.USER_KICKED_BY_OTHER_DEVICE) {
                    onUserException("account_kicked_by_other_device")
                } else if (error == Error.USER_BIND_ANOTHER_DEVICE) {
                    onUserException("user_bind_another_device")
                } else if (error == Error.USER_DEVICE_CHANGED) {
                    onUserException("user_device_changed")
                } else if (error == Error.USER_LOGIN_TOO_MANY_DEVICES) {
                    onUserException("user_login_too_many_devices")
                }
            }

            override fun onTokenExpired() {
                //login again
                signInWithToken(null)
                LogUtils.showLog(binding.tvLog, "ConnectionListener onTokenExpired")
            }

            override fun onTokenWillExpire() {
                getTokenFromAppServer(RENEW_TOKEN)
                LogUtils.showLog(binding.tvLog, "ConnectionListener onTokenWillExpire")
            }
        }
        // Call removeConnectionListener(connectionListener) when the activity is destroyed
        ChatClient.getInstance().addConnectionListener(connectionListener)
    }
    //================= SDK listener end ====================
    //=================== click event start ========================
    /**
     * Sign up with username and password.
     */
    fun signUp(view: View?) {
        val username = binding.etUsername!!.text.toString().trim { it <= ' ' }
        val pwd = (binding.etPwd as EditText).text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
            LogUtils.showErrorToast(this, binding.tvLog, getString(R.string.username_or_pwd_miss))
            return
        }
        execute {
            try {
                val headers: MutableMap<String, String> =
                    HashMap()
                headers["Content-Type"] = "application/json"
                val request = JSONObject()
                request.putOpt("userAccount", username)
                request.putOpt("userPassword", pwd)
                LogUtils.showErrorLog(binding.tvLog, "begin to signUp...")
                val response = HttpClientManager.httpExecute(
                    REGISTER_URL,
                    headers,
                    request.toString(),
                    HttpClientManager.Method_POST
                )
                val code = response.code
                val responseInfo = response.content
                if (code == 200) {
                    if (responseInfo != null && responseInfo.length > 0) {
                        val `object` = JSONObject(responseInfo)
                        val resultCode = `object`.getString("code")
                        if (resultCode == "RES_OK") {
                            LogUtils.showToast(
                                this@HomeActivity,
                                binding.tvLog,
                                getString(R.string.sign_up_success)
                            )
                        } else {
                            val errorInfo = `object`.getString("errorInfo")
                            LogUtils.showErrorLog(binding.tvLog, errorInfo)
                        }
                    } else {
                        LogUtils.showErrorLog(binding.tvLog, responseInfo)
                    }
                } else {
                    LogUtils.showErrorLog(binding.tvLog, responseInfo)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.showErrorLog(binding.tvLog, e.message)
            }
        }
    }

    /**
     * Log in with token.
     */
    fun signInWithToken(view: View?) {
        getTokenFromAppServer(NEW_LOGIN)
    }

    /**
     * Sign out.
     */
    fun signOut(view: View?) {
        if (ChatClient.getInstance().isLoggedInBefore) {
            ChatClient.getInstance().logout(true, object : CallBack {
                override fun onSuccess() {
                    LogUtils.showToast(
                        this@HomeActivity,
                        binding.tvLog,
                        getString(R.string.sign_out_success)
                    )
                }

                override fun onError(code: Int, error: String) {
                    LogUtils.showErrorToast(
                        this@HomeActivity, binding.tvLog,
                        "Sign out failed! code: $code error: $error"
                    )
                }

                override fun onProgress(progress: Int, status: String) {}
            })
        }
    }

    fun startChat(view: View?) {
        val et_to_username = findViewById<EditText>(R.id.et_to_username)
        val toChatUsername = et_to_username.text.toString().trim { it <= ' ' }
        // check username
        if (TextUtils.isEmpty(toChatUsername)) {
            LogUtils.showErrorToast(this, binding.tvLog, getString(R.string.not_find_send_name))
            return
        }
        // 1: single chat; 2: group chat; 3: chat room
        val fragment = EaseChatFragment.Builder(toChatUsername, EaseChatType.SINGLE_CHAT)
            .useHeader(false)
            .setOnChatExtendMenuItemClickListener(OnChatExtendMenuItemClickListener { view, itemId ->
                if (itemId == io.agora.chat.uikit.R.id.extend_item_take_picture) {
                    return@OnChatExtendMenuItemClickListener !checkPermissions(
                        Manifest.permission.CAMERA,
                        111
                    )
                } else if (itemId == io.agora.chat.uikit.R.id.extend_item_picture || itemId == io.agora.chat.uikit.R.id.extend_item_file || itemId == io.agora.chat.uikit.R.id.extend_item_video) {
                    return@OnChatExtendMenuItemClickListener !checkPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        112
                    )
                }
                false
            })
            .setOnChatRecordTouchListener { v, event ->
                !checkPermissions(
                    Manifest.permission.RECORD_AUDIO,
                    113
                )
            }
            .setOnMessageSendCallBack(object : OnMessageSendCallBack {
                override fun onSuccess(message: ChatMessage) {
                    LogUtils.showLog(binding.tvLog, "Send success: message type: " + message.type.name)
                }

                override fun onError(code: Int, errorMsg: String) {
                    LogUtils.showErrorLog(
                        binding.tvLog,
                        "Send failed: error code: $code errorMsg: $errorMsg"
                    )
                }
            })
            .build()
        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment, fragment).commit()
    }

    //=================== click event end ========================
    //=================== get token from server start ========================
    private fun getTokenFromAppServer(requestType: String) {
        if (ChatClient.getInstance().options.autoLogin && ChatClient.getInstance().isLoggedInBefore) {
            LogUtils.showErrorLog(binding.tvLog, getString(R.string.has_login_before))
            return
        }
        val username = binding.etUsername.text.toString().trim { it <= ' ' }
        val pwd = (findViewById<View>(R.id.et_pwd) as EditText).text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
            LogUtils.showErrorToast(
                this@HomeActivity,
                binding.tvLog,
                getString(R.string.username_or_pwd_miss)
            )
            return
        }
        execute {
            try {
                val headers: MutableMap<String, String> =
                    HashMap()
                headers["Content-Type"] = "application/json"
                val request = JSONObject()
                request.putOpt("userAccount", username)
                request.putOpt("userPassword", pwd)
                LogUtils.showErrorLog(binding.tvLog, "begin to getTokenFromAppServer ...")
                val response = HttpClientManager.httpExecute(
                    LOGIN_URL,
                    headers,
                    request.toString(),
                    HttpClientManager.Method_POST
                )
                val code = response.code
                val responseInfo = response.content
                if (code == 200) {
                    if (responseInfo != null && responseInfo.length > 0) {
                        val `object` = JSONObject(responseInfo)
                        val token = `object`.getString("accessToken")
                        if (TextUtils.equals(
                                requestType,
                                NEW_LOGIN
                            )
                        ) {
                            ChatClient.getInstance()
                                .loginWithAgoraToken(username, token, object : CallBack {
                                    override fun onSuccess() {
                                        LogUtils.showToast(
                                            this@HomeActivity,
                                            binding.tvLog,
                                            getString(R.string.sign_in_success)
                                        )
                                    }

                                    override fun onError(code: Int, error: String) {
                                        LogUtils.showErrorToast(
                                            this@HomeActivity,
                                            binding.tvLog,
                                            "Login failed! code: $code error: $error"
                                        )
                                    }

                                    override fun onProgress(
                                        progress: Int,
                                        status: String
                                    ) {
                                    }
                                })
                        } else if (TextUtils.equals(
                                requestType,
                                RENEW_TOKEN
                            )
                        ) {
                            ChatClient.getInstance().renewToken(token)
                        }
                    } else {
                        LogUtils.showErrorToast(
                            this@HomeActivity,
                            binding.tvLog,
                            "getTokenFromAppServer failed! code: $code error: $responseInfo"
                        )
                    }
                } else {
                    LogUtils.showErrorToast(
                        this@HomeActivity,
                        binding.tvLog,
                        "getTokenFromAppServer failed! code: $code error: $responseInfo"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.showErrorToast(
                    this@HomeActivity,
                    binding.tvLog,
                    "getTokenFromAppServer failed! code: " + 0 + " error: " + e.message
                )
            }
        }
    }
    //=================== get token from server end ========================
    /**
     * Check and request permission
     * @param permission
     * @param requestCode
     * @return
     */
    private fun checkPermissions(permission: String, requestCode: Int): Boolean {
        PermissionsManager.instance?.let {
            if (!it.hasPermission(this, permission)) {
                it.requestPermissions(this, arrayOf<String>(permission), requestCode)
                return false
            }
            return true
        }
        return false
    }

    /**
     * user met some exception: conflict, removed or forbidden， goto login activity
     */
    protected fun onUserException(exception: String) {
        LogUtils.showLog(binding.tvLog, "onUserException: $exception")
        ChatClient.getInstance().logout(false, null)
    }

    fun execute(runnable: Runnable?) {
        Thread(runnable).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (connectionListener != null) {
            ChatClient.getInstance().removeConnectionListener(connectionListener)
        }
    }

    companion object {
        private const val NEW_LOGIN = "NEW_LOGIN"
        private const val RENEW_TOKEN = "RENEW_TOKEN"
        private const val LOGIN_URL = "https://a41.chat.agora.io/app/chat/user/login"
        private const val REGISTER_URL = "https://a41.chat.agora.io/app/chat/user/register"
    }
}
