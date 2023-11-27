package com.dev.frequenc.ui_codes.screens.booking_process.booking_history

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.dev.frequenc.R
import com.dev.frequenc.databinding.FragmentBookingHistoryBinding
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.data.models.TicketDetailsModel
import com.dev.frequenc.ui_codes.screens.Dashboard.wallet.WalletFragment
import com.dev.frequenc.ui_codes.screens.Dashboard.wallet.WalletViewModel
import com.dev.frequenc.ui_codes.screens.booking_process.tickets.ShowTicketFragment
import com.dev.frequenc.util.Constants
import com.dev.frequenc.util.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import io.metamask.androidsdk.ErrorType
import io.metamask.androidsdk.RequestError
import io.metamask.androidsdk.TAG

@AndroidEntryPoint
class BookingHistoryFragment : Fragment(), ItemClickListener {
    private lateinit var currentActivity: MainActivity
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var bookinghistoryViewModel: BookingHistoryViewModel
    private lateinit var binding: FragmentBookingHistoryBinding
    private lateinit var walletViewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBookingHistoryBinding.inflate(layoutInflater, container, false)
        bookinghistoryViewModel = ViewModelProvider(this)[BookingHistoryViewModel::class.java]
        try {
            walletViewModel = ViewModelProvider(activity as MainActivity)[WalletViewModel::class.java]
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            currentActivity = context as MainActivity
        } else {
            try {
                currentActivity = this.requireActivity() as MainActivity
            }
            catch (e: Exception) { e.printStackTrace() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
                currentActivity.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)

        binding.headerLays.btnBack.setOnClickListener {
            try {
                currentActivity.let { requireActivity().supportFragmentManager.popBackStack() }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        currentActivity.runOnUiThread {
            try {
                if (currentActivity is MainActivity) {
                    walletViewModel.connectedVals.observe(viewLifecycleOwner) {
                        if (!it) {
                    var fragment: Fragment? =
                        currentActivity.supportFragmentManager?.findFragmentByTag("WalletFragment")
                    var walletFragment: WalletFragment
                    if (fragment != null) {
                        walletFragment = fragment as WalletFragment
                    } else {
                        walletFragment = WalletFragment()
                    }
//                    currentActivity?.supportFragmentManager?.beginTransaction()
//                        ?.replace(R.id.flFragment, walletFragment, "WalletFragment")
//                        ?.commit()
                            try {
                                startActivity(Intent(activity, com.dev.frequenc.MainActivity::class.java))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                        else {
                            walletViewModel.getBalence {
                                    result ->
                                if (result is RequestError) {
                                    if (result.code.equals(ErrorType.UNAUTHORISED_REQUEST.code)) {
//                                        var fragment: Fragment? =
//                                            currentActivity.supportFragmentManager?.findFragmentByTag("WalletFragment")
//                                        var walletFragment: WalletFragment
//                                        if (fragment != null) {
//                                            walletFragment = fragment as WalletFragment
//                                        } else {
//                                            walletFragment = WalletFragment()
//                                        }
//                                        currentActivity?.supportFragmentManager?.beginTransaction()
//                                            ?.replace(R.id.flFragment, walletFragment, "WalletFragment")
//                                            ?.commit()
                                        try {
                                            startActivity(
                                                Intent(
                                                    activity,
                                                    com.dev.frequenc.MainActivity::class.java
                                                )
                                            )
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()

                                        }
                                    }
                                    Log.e(TAG, "Ethereum connection error: ${result.message}")
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()

                                } else {
                                    walletViewModel.setConnectedVals(true)
                                    Log.d(TAG, "Ethereum connection result: $result")
                                    Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "connectedVals: ", ex)
            }
        }

        val bookingHistoryAdapter =
            BookingHistoryAdapter(ArrayList(), isUpcomingTabSelected = true, this)
        binding.rvBookingHistory.adapter = bookingHistoryAdapter

        currentActivity.runOnUiThread {
            bookinghistoryViewModel.isUpcomingTabSelected.observe(viewLifecycleOwner) {
                try {
                    if (it) {
                        setUpcomingTab()
                    } else {
                        setCompletedTab()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    try {
                        if (bookinghistoryViewModel.transactionListAp.value?.pastBooking.isNullOrEmpty() && bookinghistoryViewModel.transactionListAp.value?.upcomingBooking.isNullOrEmpty() && bookinghistoryViewModel.isApiCalled.value == false) {
                            binding.noDataLay.noDataLay.visibility = View.VISIBLE
                        } else {
                            binding.noDataLay.noDataLay.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.noDataLay.noDataLay.visibility = View.VISIBLE
                    }
                }
            }
        }

        currentActivity.runOnUiThread {
            bookinghistoryViewModel.isApiCalled.observe(viewLifecycleOwner) {
                if (!it) {
                    bookinghistoryViewModel.transactionListAp.value?.let { it1 ->
                        try {
                            if (bookinghistoryViewModel.isUpcomingTabSelected.value == true && !it1.upcomingBooking.isNullOrEmpty()) {
                                bookingHistoryAdapter.refreshLists(
                                    it1.upcomingBooking,
                                    bookinghistoryViewModel.isUpcomingTabSelected.value!!
                                )
                                binding.noDataLay.noDataLay.visibility = View.GONE
                            } else if (!it1.pastBooking.isNullOrEmpty()) {
                                bookingHistoryAdapter.refreshLists(
                                    it1.pastBooking,
                                    bookinghistoryViewModel.isUpcomingTabSelected.value!!
                                )
                                binding.noDataLay.noDataLay.visibility = View.GONE
                            } else {
                                binding.noDataLay.noDataLay.visibility = View.VISIBLE
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        binding.upcomingTab.setOnClickListener {
            if (bookinghistoryViewModel.isApiCalled.value == false) {
                bookinghistoryViewModel.setUpcomingTabValue(true)
            }
        }
        binding.completedTab.setOnClickListener {
            if (bookinghistoryViewModel.isApiCalled.value == false) {
                bookinghistoryViewModel.setUpcomingTabValue(false)
            }
        }
    }

    private fun setUpcomingTab() {
        binding.upcomingTab.setTextColor(Color.WHITE)
        binding.upcomingTab.background = resources.getDrawable(R.drawable.purple_corner_cut_bg)
        binding.headUpcomingBookings.text = "Upcoming Bookings"

        binding.completedTab.setTextColor(Color.BLACK)
        binding.completedTab.background = resources.getDrawable(R.drawable.transparent_bg)
    }

    private fun setCompletedTab() {
        binding.completedTab.setTextColor(Color.WHITE)
        binding.completedTab.background = resources.getDrawable(R.drawable.purple_corner_cut_bg)
        binding.headUpcomingBookings.text = "Completed Bookings"

        binding.upcomingTab.setTextColor(Color.BLACK)
        binding.upcomingTab.background = resources.getDrawable(R.drawable.transparent_bg)
    }


    override fun onResume() {
        super.onResume()
        binding.headerLays.tvHeader.text = "Booking History"
        bookinghistoryViewModel.callTransactionslistApi(
            sharedPreferences?.getString(
                Constants.Authorization,
                null
            ).toString()
        )

        currentActivity.runOnUiThread {
            try {
                if (currentActivity is MainActivity && walletViewModel.connectedVals.value == true) {
                    walletViewModel.getBalence { result ->
                        if (result is RequestError) {
                            if (result.code.equals(ErrorType.UNAUTHORISED_REQUEST.code)) {
                                walletViewModel.setConnectedVals(false)
                            }
                            Log.e(TAG, "Ethereum connection error: ${result.message}")
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()

                        } else {
                            walletViewModel.setConnectedVals(true)
                            Log.d(TAG, "Ethereum connection result: $result")
                            Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "WalletViewModel: ", ex)
                walletViewModel.setConnectedVals(false)
            }
        }

    }

    override fun onItemClicked(itemPositon: Int) {
        currentActivity.runOnUiThread {
            if (bookinghistoryViewModel.isUpcomingTabSelected.value == true) {
                bookinghistoryViewModel.transactionListAp.value?.upcomingBooking?.get(itemPositon)
                    ?.let {
                        moveToShowTicket(
                            TicketDetailsModel(
                                tokenId = it.tokenId,
                                amount = it.amount.toString(),
                                ownerAddress = it.ownerAddress,
                                contractAddress = it.contractAddress,
                                eventImage = it.eventImage,
                                eventTitle = it.eventTitle,
                                eventStartDate = it.eventStartDate,
                                ticket_quantity = it.ticket_quantity,
                                ticket_type = it.ticket_type,
                                payment_status = it.payment_status,
                                transactionHash = it.transactionHash
                            )
                        )
                    }
            } else {
                bookinghistoryViewModel.transactionListAp.value?.pastBooking?.get(itemPositon)
                    ?.let {
                        moveToShowTicket(
                            TicketDetailsModel(
                                tokenId = it.tokenId,
                                amount = it.amount.toString(),
                                ownerAddress = it.ownerAddress,
                                contractAddress = it.contractAddress,
                                eventImage = it.eventImage,
                                eventTitle = it.eventTitle,
                                eventStartDate = it.eventStartDate,
                                ticket_quantity = it.ticket_quantity,
                                ticket_type = it.ticket_type,
                                payment_status = it.payment_status,
                                transactionHash = it.transactionHash
                            )
                        )
                    }
            }
        }
    }

    private fun moveToShowTicket(ticketDetailsModel: TicketDetailsModel) {
        val myArguments = Bundle()
        myArguments.putParcelable("ticketDetailsModel", ticketDetailsModel)
        try {
            val fragmentss =
                activity?.supportFragmentManager?.findFragmentByTag("ShowTicketFragment")
            if (fragmentss != null) {
                val showTicketFragment = fragmentss as ShowTicketFragment
                showTicketFragment.arguments = myArguments
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.flFragment, showTicketFragment, "ShowTicketFragment")
                    ?.commit()
            } else {
                val showTicketFragment = ShowTicketFragment()
                showTicketFragment.arguments = myArguments
                activity?.supportFragmentManager?.beginTransaction()
                    ?.add(R.id.flFragment, showTicketFragment, "ShowTicketFragment")
                    ?.commit()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}