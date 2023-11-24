package com.dev.frequenc.ui_codes.screens.booking_process.tickets

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dev.frequenc.ui_codes.data.models.TicketDetailsModel
import com.dev.frequenc.databinding.FragmentShowTicketBinding
import com.dev.frequenc.ui_codes.util.CommonUtils

class ShowTicketFragment : Fragment() {

    private lateinit var binding: FragmentShowTicketBinding
    private lateinit var showTicketViewModel: ShowTicketViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentShowTicketBinding.inflate(inflater, container, false)
        showTicketViewModel = ViewModelProvider(this)[ShowTicketViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        activity?.runOnUiThread {
            showTicketViewModel.ticketDetailsModel.observe(viewLifecycleOwner) {
                it?.let {
                    binding.tvBookingId.text = "Booking ID: ${it.tokenId}"
                    binding.tvAdult.text = "${it.ticket_quantity} Only"
                    binding.headEventName.text = it.eventTitle
                    val daTeTime = CommonUtils.getDateAndTimeFromTimeStamp(it.eventStartDate)
                    binding.tvTime.text = daTeTime[1]
                    binding.tvTotalPrice.text = "${it.amount} FRQ"
                    binding.tvDate.text = daTeTime[0]
                    Glide.with(binding.ivTicket.context).load(Uri.parse(it.eventImage.toString()))
                }
            }
        }

        binding.btnShare.setOnClickListener {
            Toast.makeText(context, "sdfs", Toast.LENGTH_SHORT).show()
        }
        binding.btnDownload.setOnClickListener {
            Toast.makeText(
                context,
                "sdfssdfds",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.headerLays.btnBack.setOnClickListener {
            try {
                activity?.supportFragmentManager?.popBackStack()
            } catch (ex: Exception) {
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        showTicketViewModel.callT
        activity?.runOnUiThread {
            arguments?.getParcelable<TicketDetailsModel>("ticketDetailsModel")?.let {
                showTicketViewModel.setTicketDetails(it)
            }
        }

        binding.headerLays.tvHeader.text = "QR Code"
    }

}