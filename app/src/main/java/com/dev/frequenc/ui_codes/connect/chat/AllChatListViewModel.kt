package com.dev.frequenc.ui_codes.connect.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AllChatListViewModel: ViewModel() {

    private val _userLists = MutableLiveData<List<Any>>(ArrayList(3))
    val userListsData: LiveData<List<Any>>
        get() = _userLists

    private val _isConnectionTabSelected = MutableLiveData<Boolean> (true)

    val isConnectionTabSelected: LiveData<Boolean>
        get() = _isConnectionTabSelected

    private val _isPendingSubTabSelected = MutableLiveData<Boolean> (true)

    val isPendingSubTabSelected: LiveData<Boolean>
        get() = _isPendingSubTabSelected

    private val __isApiCalled = MutableLiveData<Boolean> (false)
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

}
