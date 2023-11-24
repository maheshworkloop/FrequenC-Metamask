package com.dev.frequenc.ui_codes.screens.notification

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityNotificationBinding
import com.dev.frequenc.util.Constants
import com.dev.frequenc.util.ItemClickListener

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var sharedPreference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@NotificationActivity , R.layout.activity_notification)

        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        binding.headerLays.tvHeader.text = "Notifications"
        sharedPreference = this.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)

        val notificationAdapterForShowAllToday =
            NotificationAdapter(ArrayList(), object : ItemClickListener {
                override fun onItemClicked(item_pos: Int) {

                }
            })
        binding.rvToday.adapter = notificationAdapterForShowAllToday

        val notificationAdapterForShowAllThisWeek =
            NotificationAdapter(ArrayList(), object : ItemClickListener {
                override fun onItemClicked(item_pos: Int) {

                }
            })
        binding.rvLastWeek.adapter = notificationAdapterForShowAllThisWeek

        val notificationAdapterForItems =
            NotificationAdapter(ArrayList(), object : ItemClickListener {
                override fun onItemClicked(item_pos: Int) {

                }
            })
        binding.rvItems.adapter = notificationAdapterForItems

        this.runOnUiThread {
            notificationViewModel.filterType.observe(this@NotificationActivity) {
                it?.let {
                    notificationViewModel.setApiCalled(true)
                    when (it) {
                        "today" -> {
                            setButton(2)
                        }

                        "last_week" -> {
                            setButton(3)
                        }

                        else -> {
                            setButton(1)
                        }
                    }
                }
            }
        }

        this.runOnUiThread {
            notificationViewModel.isApiCalled.observe(this@NotificationActivity) {
                if (it) {
                    binding.noDataInTodayLast.noDataLay.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        this.runOnUiThread {
            notificationViewModel.toastMessage.observe(this){  if (!it.isNullOrEmpty()) {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            }}
        }

        this.runOnUiThread {
            notificationViewModel.NotificationResponse.observe(this@NotificationActivity, Observer {
                it?.notification?.let {
                when (notificationViewModel.filterType.value) {
                    "today" -> {
                        if (it.isNullOrEmpty()) {
                            binding.noDataInTodayLast.noDataLay.visibility = View.VISIBLE
                        } else {
                            binding.noDataInTodayLast.noDataLay.visibility = View.GONE
                        }
                        notificationAdapterForItems.refreshList(it)
                        notificationViewModel.setApiCalled(false)
                    }

                    "last_week" -> {
                        if (it.isNullOrEmpty()) {
                            binding.noDataInTodayLast.noDataLay.visibility = View.VISIBLE
                        } else {
                            binding.noDataInTodayLast.noDataLay.visibility = View.GONE
                        }
                        notificationAdapterForItems.refreshList(it)
                        notificationViewModel.setApiCalled(false)
                    }

                    else -> {
//                        if (it.isNullOrEmpty()) {
//                            binding.noDataInTodayLast.noDataLay.visibility = View.VISIBLE
//                        } else {
                            if (it.isNotEmpty()) {
                                binding.noThisWeekDataInAll.noDataLay.visibility = View.GONE
                            } else {
                                binding.noThisWeekDataInAll.noDataLay.visibility = View.VISIBLE
                            }
                            notificationAdapterForShowAllThisWeek.refreshList(it)
                            if (it.isNotEmpty()) {
                                binding.noTodayDataInAll.noDataLay.visibility = View.GONE
                            } else {
                                binding.noTodayDataInAll.noDataLay.visibility = View.VISIBLE
                            }
                            notificationAdapterForShowAllToday.refreshList(it)
//                        }
                        notificationViewModel.setApiCalled(false)
                    }
                }
                }
            })
        }

        binding.headerLays.btnBack.setOnClickListener { onBackPressed() }

        binding.btnShowAll.setOnClickListener {
            val token = sharedPreference.getString(Constants.Authorization, null).toString()
            token?.let {
                notificationViewModel.setFilterTypeKey("all")
                notificationViewModel.callNotificationApi(tokens = token)
            }
        }

        binding.btnToday.setOnClickListener {
            val token = sharedPreference.getString(Constants.Authorization, null).toString()
            token?.let {
                notificationViewModel.setFilterTypeKey("today")
                notificationViewModel.callNotificationApi(tokens = token)
            }
        }

        binding.btnLastWeek.setOnClickListener {
            val token = sharedPreference.getString(Constants.Authorization, null).toString()
            token?.let {
                notificationViewModel.setFilterTypeKey("last_week")
                notificationViewModel.callNotificationApi(tokens = token)
            }
        }

    }

    private fun setButton(i: Int) {
        when (i) {
            2 -> {
                binding.btnToday.background = resources.getDrawable(R.drawable.voilet_corner_bg)
                binding.btnToday.setTextColor(Color.WHITE)

                binding.btnShowAll.background = resources.getDrawable(R.drawable.gray_corner_cut_white_bg)
                binding.btnShowAll.setTextColor(Color.BLACK)
                binding.btnLastWeek.background = resources.getDrawable(R.drawable.gray_corner_cut_white_bg)
                binding.btnLastWeek.setTextColor(Color.BLACK)

                binding.bothLays.visibility = View.GONE
                binding.singleLays.visibility = View.VISIBLE
            }

            3 -> {
                binding.btnLastWeek.background = resources.getDrawable(R.drawable.voilet_corner_bg)
                binding.btnLastWeek.setTextColor(Color.WHITE)

                binding.btnToday.background = resources.getDrawable(R.drawable.gray_corner_cut_white_bg)
                binding.btnToday.setTextColor(Color.BLACK)
                binding.btnShowAll.background = resources.getDrawable(R.drawable.gray_corner_cut_white_bg)
                binding.btnShowAll.setTextColor(Color.BLACK)

                binding.bothLays.visibility = View.GONE
                binding.singleLays.visibility = View.VISIBLE
            }

            else -> {
                binding.btnShowAll.background = resources.getDrawable(R.drawable.voilet_corner_bg)
                binding.btnShowAll.setTextColor(Color.WHITE)

                binding.btnToday.background = resources.getDrawable(R.drawable.gray_corner_cut_white_bg)
                binding.btnToday.setTextColor(Color.BLACK)
                binding.btnLastWeek.background = resources.getDrawable(R.drawable.gray_corner_cut_white_bg)
                binding.btnLastWeek.setTextColor(Color.BLACK)

                binding.singleLays.visibility = View.GONE
                binding.bothLays.visibility = View.VISIBLE
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onResume() {
        super.onResume()
        val token = sharedPreference.getString(Constants.Authorization, null).toString()
        token?.let {
            notificationViewModel.setFilterTypeKey("all")
            notificationViewModel.callNotificationApi(tokens = token)
        }

    }
}