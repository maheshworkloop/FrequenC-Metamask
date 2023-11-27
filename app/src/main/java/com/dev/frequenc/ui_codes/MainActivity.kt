package com.dev.frequenc.ui_codes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityMainBinding
import com.dev.frequenc.ui_codes.connect.home.ConnectHomeFragment
import com.dev.frequenc.ui_codes.data.AudienceDataResponse
import com.dev.frequenc.ui_codes.screens.Dashboard.CreateFragment
import com.dev.frequenc.ui_codes.screens.Dashboard.MarketPlaceFragment
import com.dev.frequenc.ui_codes.screens.Dashboard.savedevent.SavedEventFragment
import com.dev.frequenc.ui_codes.screens.Dashboard.wallet.WalletFragment
import com.dev.frequenc.ui_codes.screens.Profile.AudienceProfileActivity
import com.dev.frequenc.ui_codes.screens.booking_process.booking_history.BookingHistoryFragment
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import java.io.Serializable


@AndroidEntryPoint
class MainActivity  : AppCompatActivity() {

    val requestcode = 101
    var latitude = ""
    var longitude = ""
    var exitCount = 0
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var authorization: String
    lateinit var audience_id: String
    lateinit var binding: ActivityMainBinding
//    private lateinit var walletViewModel: WalletViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS)

        val createFragment = CreateFragment()
        val connectFragment = ConnectHomeFragment()
        val marketPlace = MarketPlaceFragment()
        val walletFragment = WalletFragment()
        val savedEventFragment = SavedEventFragment()
        val bookingHistoryFragment = BookingHistoryFragment()

        setCurrentFragment(marketPlace, "MarketPlaceFragment")


        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_marketplace ->
                {
                    setCurrentFragment(marketPlace, "MarketPlaceFragment")
//                    binding.bottomNavigationView.getMenu().clear(); //clear old inflated items.
//                    binding.bottomNavigationView.inflateMenu(R.menu.menu_bottom_connect)
                }
                R.id.bottom_create      -> setCurrentFragment(createFragment, "CreateFragment")
                R.id.bottom_connect     ->
//                      startActivity(Intent(this,ConnectHomeActivity::class.java))
                {
                    setCurrentFragment(connectFragment, "ConnectFragment")
//                    binding.bottomNavigationView.getMenu().clear(); //clear old inflated items.
//                    binding.bottomNavigationView.inflateMenu(R.menu.menu_bottom_connect)
                }
                R.id.bottom_wallet      -> setCurrentFragment(walletFragment, "WalletFragment")
            }

            true
        }


        sharedPreferences = getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!

        val userRegistered = sharedPreferences.getBoolean(Constants.isUserTypeRegistered, false)

        authorization = sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId, "-1").toString()

        if (userRegistered && !(authorization == "-1") && !(audience_id == "-1")) {

            Log.d("Audience Id", audience_id)
            Log.d("Bearer", authorization)
            Log.d("token", "onCreate: ${sharedPreferences.getString(Constants.Authorization, " ")}")
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            binding.navbar.rlLogout.visibility = View.VISIBLE
            binding.navbar.viewLogout.visibility = View.VISIBLE
            binding.navbar.rlLogout.setOnClickListener {
                closeDrawer()
                showLogout()

            }
            binding.navbar.rlSavedEvents.setOnClickListener {
                setCurrentFragment(savedEventFragment, "SavedEventFragment")
                closeDrawer()
            }
            binding.navbar.rlBookingHistory.setOnClickListener {
                setCurrentFragment(bookingHistoryFragment, "BookingHistoryFragment")
                closeDrawer()
            }


        } else {
            Toast.makeText(this, "User Not Logged in", Toast.LENGTH_SHORT).show()
            Log.e("Audience Id", audience_id)
            Log.e("Bearer", authorization)
            binding.navbar.rlLogout.visibility = View.INVISIBLE
            binding.navbar.viewLogout.visibility = View.INVISIBLE

        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val count = supportFragmentManager.backStackEntryCount

                if (count == 0) {
                    val builder1 = AlertDialog.Builder(this@MainActivity)
                        .setMessage("Do you want to exit ?")
                        .setTitle("Alert !")
                        .setPositiveButton("Yes") { dialog, id ->
                            System.exit(0)
                        }
                        .setNegativeButton("No") { dialog, id ->
                            dialog.cancel()
                        }

                    builder1.create().show()
                    //additional code
                } else {
                    supportFragmentManager.popBackStack()
                }

            }
        })

//        this.supportFragmentManager.addOnBackStackChangedListener {
//            if (supportFragmentManager.findFragmentByTag("MarketPlaceFragment")?.isVisible == true) {
//
//            } else if (supportFragmentManager.findFragmentByTag("CreateFragment")?.isVisible == true) {
//
//            } else if (supportFragmentManager.findFragmentByTag("ConnectFragment")?.isVisible == true) {
//
//            } else if (supportFragmentMan ager.findFragmentByTag("WalletFragment")?.isVisible == true) {
//
//            } else {
//
//            }
//        }

    }


    private fun setCurrentFragment(fragment: Fragment, fragmetsTag: String) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, fragmetsTag)
            addToBackStack(fragmetsTag)
            commit()
        }

    private fun closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawers()
        }
    }


    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestcode
            )
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE)
//        {
//            if( grantResults.isNotEmpty() )
//                if(  grantResults[0].equals(PackageManager.PERMISSION_GRANTED) ||
//                    grantResults[1].equals(PackageManager.PERMISSION_GRANTED)   )
//                {
//                    GetLocation()
//                }
//        }
    }


    private fun getProfileApi() {
//        binding.progressDialog.visibility = View.VISIBLE
        try {
        ApiClient.getInstance()!!.getProfile(authorization, audience_id)
            .enqueue(object : retrofit2.Callback<AudienceDataResponse> {
                override fun onResponse(
                    call: Call<AudienceDataResponse>,
                    response: Response<AudienceDataResponse>
                ) {
//                binding.progressDialog.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("Profile Api", "onResponse Retrofit Profile Data: " + response.body())
                        val res = response.body()

                        var item: AudienceDataResponse = res!!

                        binding.navbar.tvCustName.text = res!!.fullName

                        binding.navbar.llNavDetails.setOnClickListener {
                            closeDrawer()
                            val intent =
                                Intent(this@MainActivity, AudienceProfileActivity::class.java)
                            intent.putExtra("item", item as Serializable)
                            startActivity(intent)
                        }

                        if (!res.profile_pic.isNullOrEmpty())
                            Glide.with(this@MainActivity).load(res.profile_pic)
                                .into(binding.navbar.ivProfileImage)
                    }
                }

                override fun onFailure(call: Call<AudienceDataResponse>, t: Throwable) {
//                binding.progressDialog.visibility = View.GONE
                    Log.d("Profile Api", "onFailure Retrofit: " + t.localizedMessage)


                }

            })
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showLogout() {
        val builder1 = AlertDialog.Builder(this)
            .setMessage("Do you want to Logout from your account ?")
            .setTitle("Alert !")
            .setPositiveButton("Yes") { dialog, id ->
                sharedPreferences.edit().putBoolean(Constants.isUserTypeRegistered, false).apply()
                sharedPreferences.edit().putString(Constants.Authorization, "").apply()
                sharedPreferences.edit().putString(Constants.AudienceId, "").apply()
                binding.navbar.rlLogout.visibility = View.GONE
                dialog.cancel()
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }

        builder1.create().show()

    }


    override fun onResume() {
        super.onResume()
        exitCount = 0
        getProfileApi()
    }


    //    override fun onBackPressed() {
////        this.supportFragmentManager.findFragmentByTag("MarketPlaceFragment")?.id?.let {
////        supportFragmentManager.inTransaction {
////
////            popBackStack(
////                it, FragmentManager.POP_BACK_STACK_INCLUSIVE)
////
////            replace(R.id.container, newFragment, "NewFragment")
////        }
//        val builder1 = AlertDialog.Builder(this)
//            .setMessage("Do you want to exit ?")
//            .setTitle("Alert !")
//            .setPositiveButton("Yes") { dialog, id ->
//                super.onBackPressed()
//                System.exit(0)
//            }
//            .setNegativeButton("No") { dialog, id ->
//                dialog.cancel()
//            }
//
//        builder1.create().show()
//    }
    override fun onBackPressed() {
//        this.supportFragmentManager.findFragmentByTag("MarketPlaceFragment")?.id?.let {
//        supportFragmentManager.inTransaction {
//
//            popBackStack(
//                it, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//
//            replace(R.id.container, newFragment, "NewFragment")
//        }

        try {
            if (this.supportFragmentManager.fragments.size > 1 && (this.supportFragmentManager?.findFragmentByTag(
                    "MarketPlaceFragment"
                )?.isVisible == false)
            ) {

                val fragmentManager = this.supportFragmentManager
                val backStackCount = fragmentManager.backStackEntryCount
                for (i in 0 until backStackCount) {
                    val backStackEntry: FragmentManager.BackStackEntry =
                        fragmentManager.getBackStackEntryAt(i)
                    if (!backStackEntry.getName().equals("MarketPlaceFragment")) {
                        fragmentManager.popBackStackImmediate(
                            backStackEntry.getId(),
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }
                }
//                var fragmentToKeep =
//                    this.supportFragmentManager.findFragmentByTag("MarketPlaceFragment")!! as MarketPlaceFragment
//                val transaction: FragmentTransaction =
//                    this.supportFragmentManager.beginTransaction()
//
//                if (fragmentToKeep != null) {
//                    transaction.detach(fragmentToKeep)
//                } else {
//                    fragmentToKeep = MarketPlaceFragment()
//                }
//
//                for (fragment in supportFragmentManager.fragments) {
//                    if (fragment != null && fragment !== fragmentToKeep) {
//                        transaction.remove(fragment)
//                    }
//                }
//
//                transaction.addToBackStack(null)
//                transaction.commit()
            } else {
                val builder1 = AlertDialog.Builder(this)
                    .setMessage("Do you want to exit ?")
                    .setTitle("Alert !")
                    .setPositiveButton("Yes") { dialog, id ->
                        super.onBackPressed()
                        System.exit(0)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.cancel()
                    }


                builder1.create().show()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            this.supportFragmentManager.beginTransaction()
                .add(R.id.flFragment, MarketPlaceFragment(), "MarketPlaceFragment").commit()
        }
    }
}


