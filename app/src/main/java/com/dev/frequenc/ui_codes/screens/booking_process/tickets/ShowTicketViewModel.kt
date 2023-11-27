package com.dev.frequenc.ui_codes.screens.booking_process.tickets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.frequenc.ui_codes.data.models.TicketDetailsModel
import kotlinx.coroutines.launch

class ShowTicketViewModel : ViewModel() {
    private val __ticketDetailsModel = MutableLiveData<TicketDetailsModel>()
    val ticketDetailsModel: LiveData<TicketDetailsModel>
        get() = __ticketDetailsModel

    fun setTicketDetails(it: TicketDetailsModel) {
        viewModelScope.launch {
            __ticketDetailsModel.value = it
        }
    }


}
