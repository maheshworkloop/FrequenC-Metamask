package com.dev.frequenc.ui_codes.screens.booking_process.booking_history

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.frequenc.databinding.ItemBookingHistoryBinding
import com.dev.frequenc.ui_codes.data.models.ItemBHistoryModel
import com.dev.frequenc.ui_codes.data.transactionlist.PastBooking
import com.dev.frequenc.ui_codes.data.transactionlist.UpcomingBooking
import com.dev.frequenc.ui_codes.util.CommonUtils
import com.dev.frequenc.util.ImageUtil
import com.dev.frequenc.ui_codes.util.ItemClickListener

class BookingHistoryAdapter(
    var bhistoryList: List<Any>,
    var isUpcomingTabSelected: Boolean,
    var itemClickListener: ItemClickListener
) : Adapter<BookingHistoryAdapter.mViewHolder>() {
    class mViewHolder(val itemBookingHistoryBinding: ItemBookingHistoryBinding) :
        ViewHolder(itemBookingHistoryBinding.root) {

        fun bindCompletedBookingViews(pastBooking: PastBooking) {
            val bookingTimeDate =
                CommonUtils.getDateAndTimeFromTimeStamp(pastBooking.eventStartDate)
            val bookingStatus = SpannableString("Ticket Status : ${pastBooking.payment_status}")
            bookingStatus.setSpan(
                ForegroundColorSpan(Color.parseColor("#FFBB86FC")),
                15,
                bookingStatus.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            itemBookingHistoryBinding.tvTicketStatus.text = bookingStatus
            val paymentStatus =
                SpannableString("Payment Status: ${pastBooking.payment_status} | FRQ 10")
            itemBookingHistoryBinding.tvCashAmount.text = pastBooking.amount.toString()
            bookingStatus.setSpan(
                ForegroundColorSpan(Color.parseColor("#32D414")),
                16,
                bookingStatus.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            itemBookingHistoryBinding.tvPaymentStatus.text = paymentStatus
            ImageUtil.loadImage(itemBookingHistoryBinding.ivItem, pastBooking.eventImage[0])
            itemBookingHistoryBinding.itemBHistory = ItemBHistoryModel(
                itemTitle = pastBooking.eventTitle,
                ticketType = pastBooking.ticket_type,
                bookingDate = bookingTimeDate[0],
                bookingTime = bookingTimeDate[1]
            )
            itemBookingHistoryBinding.executePendingBindings()
        }

        fun bindUpcomingBookingViews(upcomingBooking: UpcomingBooking) {
            val bookingTimeDate =
                CommonUtils.getDateAndTimeFromTimeStamp(upcomingBooking.eventStartDate)
            val bookingStatus = SpannableString("Ticket Status : ${upcomingBooking.payment_status}")
            bookingStatus.setSpan(
                ForegroundColorSpan(Color.parseColor("#FFBB86FC")),
                15,
                bookingStatus.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            itemBookingHistoryBinding.tvTicketStatus.text = bookingStatus
            val paymentStatus =
                SpannableString("Payment Status: ${upcomingBooking.payment_status} | FRQ 10")
            bookingStatus.setSpan(
                ForegroundColorSpan(Color.parseColor("#32D414")),
                16,
                bookingStatus.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            itemBookingHistoryBinding.tvCashAmount.text = upcomingBooking.amount.toString()
            ImageUtil.loadImage(itemBookingHistoryBinding.ivItem, upcomingBooking.eventImage[0])

            itemBookingHistoryBinding.tvPaymentStatus.text = paymentStatus
            itemBookingHistoryBinding.itemBHistory = ItemBHistoryModel(
                itemTitle = upcomingBooking.eventTitle,
                ticketType = upcomingBooking.ticket_type,
                bookingDate = bookingTimeDate[0],
                bookingTime = bookingTimeDate[1]
            )
            itemBookingHistoryBinding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        val itemBookingHistoryBinding =
            ItemBookingHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return mViewHolder(itemBookingHistoryBinding)
    }

    override fun getItemCount(): Int {
        return bhistoryList.size
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        val item_pos = holder.bindingAdapterPosition
        if (item_pos != -1) {
            val itemObj = bhistoryList[item_pos]
            if (itemObj != null) {
//                if (itemObj is UpcomingBooking) {
                if (isUpcomingTabSelected) {
                    holder.bindUpcomingBookingViews(bhistoryList[item_pos] as UpcomingBooking)
                } else {
                    holder.bindCompletedBookingViews(bhistoryList[item_pos] as PastBooking)
                }
                holder.itemBookingHistoryBinding.itemLay.setOnClickListener {
                    itemClickListener.onItemClicked(item_pos)
                }
            } else {
                holder.itemBookingHistoryBinding.itemLay.alpha = 0.3f
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshLists(it: List<Any>, isUpcomingTabSelected: Boolean) {
        this.bhistoryList = it
        this.isUpcomingTabSelected = isUpcomingTabSelected
        notifyDataSetChanged()
    }

}

