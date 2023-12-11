package com.dev.frequenc.ui_codes.connect.chat

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask.execute
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dev.agorademo2.PermissionsManager
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.util.ApiChatClient
import com.dev.frequenc.ui_codes.util.CommonUtils
import com.dev.frequenc.ui_codes.util.Constants
import com.dev.frequenc.ui_codes.util.KeysConstant
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage
import io.agora.chat.Conversation
import io.agora.chat.uikit.chat.EaseChatFragment
import io.agora.chat.uikit.chat.interfaces.OnChatExtendMenuItemClickListener
import io.agora.chat.uikit.chat.interfaces.OnMessageSendCallBack
import io.agora.chat.uikit.menu.EaseChatType
import io.agora.cloud.HttpClientManager
import retrofit2.Call


class ChatFragment : Fragment() {

    private lateinit var shearedPreference: SharedPreferences
    private var toChatUsername: String = "skkfjdsd"
//    private var toChatUsername: String? = "7777444422"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)
        try {
            shearedPreference = this.activity?.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE) as SharedPreferences
//            toChatUsername = shearedPreference.getString(Constants.User_Id, null).toString()
//            toChatUsername = activity?.intent?.getStringExtra(Constants.Messaged_user).toString()
        }
        catch (ex: Exception) {

        }

        startChat(view)
        return view
    }


    private fun checkPermissions(permission: String, requestCode: Int): Boolean {
        PermissionsManager.instance?.let {
            if (!it.hasPermission(activity, permission)) {
                it.requestPermissions(activity, arrayOf<String>(permission), requestCode)
                return false
            }
            return true
        }
        return false
    }

    private fun requestPermissions() {
        checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, 110)
    }


    private fun startChat(view: View) {

        // check username
        if (TextUtils.isEmpty(toChatUsername)) {
            Log.d(Constants.TAG_CHAT, getString(R.string.not_find_send_name))
//            LogUtils.showErrorToast(this, binding.tvLog, getString(R.string.not_find_send_name))
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
                    Log.d(Constants.TAG_CHAT, "Send success: message type: " + message.type.name)
//                    LogUtils.showLog(binding.tvLog, "Send success: message type: " + message.type.name)
                }

                override fun onError(code: Int, errorMsg: String) {
                    Log.d(Constants.TAG_CHAT, "Send failed: error code: $code errorMsg: $errorMsg")
//                    LogUtils.showErrorLog(
//                        binding.tvLog,
//                        "Send failed: error code: $code errorMsg: $errorMsg"
//                    )
                }
            })
            .build()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.chat_flfragment, fragment)?.commit()
    }


}