package com.dev.frequenc.ui_codes.connect.home

import android.R.attr.bitmap
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.connect.events.EventsFragment
import com.dev.frequenc.ui_codes.connect.yourvibes.ShareVibesAdapter
import com.dev.frequenc.ui_codes.connect.yourvibes.YourVibeFragment
import com.dev.frequenc.ui_codes.connect.yourvibes.YourVibesAdapter
import com.dev.frequenc.ui_codes.data.AudienceDataResponse
import com.dev.frequenc.ui_codes.data.CategoryDetail
import com.dev.frequenc.ui_codes.data.GetVibeCategoryResponse
import com.dev.frequenc.ui_codes.screens.Profile.AudienceProfileActivity
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Response
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConnectHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConnectHomeFragment : Fragment(),ShareVibesAdapter.ListAdapterListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var root : View

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager
    lateinit var dialog : Dialog
    lateinit var dialog2 : Dialog
    lateinit var dialogProfileMatch : Dialog
    lateinit var gifImageView : GifImageView
    lateinit var progressDialog : ProgressBar
    lateinit var authorization : String

    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences
    var userRegistered : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_connect_home, container, false)

        progressDialog = root.findViewById(R.id.progress_bar)

        val ivConnectSplash = root.findViewById<GifImageView>(R.id.ivAnimSplashConnct)

        Glide.with(requireContext()).asGif().load(R.drawable.frequenc_loader).into(ivConnectSplash)

        val rl_splash = root.findViewById<RelativeLayout>(R.id.rl_splash)




        Handler().postDelayed({
            rl_splash.visibility =View.GONE
        }, 3000)

        tabLayout = root.findViewById(R.id.tabLayout)
        viewPager = root.findViewById(R.id.viewPager)

        val adapter = VibesTabAdapter(childFragmentManager)
        adapter.addFragment(YourVibeFragment(),"Your Vibe")
        adapter.addFragment(EventsFragment(),"Events")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(
            TabLayoutOnPageChangeListener(tabLayout)
        )

        gifImageView = root.findViewById<GifImageView>(R.id.ivAnimSplashConnct)


//        showPopUpConnectionRequest()


//        showPopUpCongratulation()

        Glide.with(this)
            .asGif()
            .load(R.drawable.frequenc_loader) // Replace with the actual resource ID of your GIF
            .into(gifImageView)

//        tabLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })


        sharedPreferences = activity?.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!

        userRegistered = sharedPreferences.getBoolean(Constants.isUserTypeRegistered, false)


        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId,"-1").toString()

        if(userRegistered && !authorization.isNullOrEmpty() &&authorization!="-1" && !audience_id.isNullOrEmpty() )
        {

            getProfileApi()
        }
        else
        {


//            Toast.makeText(requireContext(),"Not Logged in Failure", Toast.LENGTH_SHORT).show()
            Log.e("Audience Id",audience_id)
        }




        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConnectHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConnectHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
    }



    private fun showPopUp(mData : GetVibeCategoryResponse)
    {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.layout_dialog_share_your_vibes)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)

        dialog.setCancelable(false)

        var gifImage = dialog.findViewById<GifImageView>(R.id.ivAnimSplash)

        Glide.with(requireContext()).load(R.drawable.frequenc_loader).into(gifImage)

        var rvShareVibe = dialog.findViewById<RecyclerView>(R.id.rvShareVibes)

        var item1 = YourVibeResponse(R.drawable.lookingforlove,"Looking For Love")
        var item2 = YourVibeResponse(R.drawable.lookingforlove,"Free Tonight")
        var item3 = YourVibeResponse(R.drawable.lookingforlove,"Let's be friend")
        val mlist = listOf(item1,item2,item3)

        rvShareVibe.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter = ShareVibesAdapter(mData,this@ConnectHomeFragment)
        }

//        dialog.getWindow()!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }


    private fun showPopUpConnectionRequest()
    {
        dialog2 = Dialog(requireContext())
        dialog2.setContentView(R.layout.layout_dialog_new_connection_request)
        dialog2.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog2.setCancelable(false)


        var btnViewProfile = dialog2.findViewById<TextView>(R.id.btnViewProfile)

       btnViewProfile.setOnClickListener {
           dialog2.cancel()
       }

//        dialog.getWindow()!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog2.show()
    }

    private fun showPopUpCongratulation()
    {
        dialogProfileMatch = Dialog(requireContext())
        dialogProfileMatch.setContentView(R.layout.layout_dialog_profile_match)
        dialogProfileMatch.window?.setBackgroundDrawableResource(R.color.transparent)
//        dialog2.setCancelable(false)

        val ivProfile1 = dialogProfileMatch.findViewById<ImageView>(R.id.ivProfile1)
        val ivProfile2 = dialogProfileMatch.findViewById<ImageView>(R.id.ivProfile2)



//        var btnViewProfile = dialog2.findViewById<TextView>(R.id.btnViewProfile)
//
//        btnViewProfile.setOnClickListener {
//            dialog2.cancel()
//        }

//        dialog.getWindow()!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogProfileMatch.show()
    }

    override fun onClickAtShareVibe(item: CategoryDetail) {
        dialog.cancel()
        updateVibe(item._id)
    }


    private fun getCategoryApi()
    {
        progressDialog.visibility =View.VISIBLE
        ApiClient.getInstance()!!.getVibeCategory()!!.enqueue(object : retrofit2.Callback<GetVibeCategoryResponse>{
            override fun onResponse(
                call: Call<GetVibeCategoryResponse>,
                response: Response<GetVibeCategoryResponse>
            ) {

                progressDialog.visibility = View.GONE

                if(response.isSuccessful && response.body()!=null)
                {


                    Handler().postDelayed({
                        showPopUp(response.body()!!)
                    }, 2000)

                }
            }

            override fun onFailure(call: Call<GetVibeCategoryResponse>, t: Throwable) {
                progressDialog.visibility = View.GONE
            }
        })
    }


    private fun getProfileApi() {
        progressDialog.visibility = View.VISIBLE
        try {
            ApiClient.getInstance()!!.getProfile(authorization, audience_id)
                .enqueue(object : retrofit2.Callback<AudienceDataResponse> {
                    override fun onResponse(
                        call: Call<AudienceDataResponse>,
                        response: Response<AudienceDataResponse>
                    ) {
                        progressDialog.visibility = View.GONE
                        if (response.isSuccessful && response.body() != null) {
                            Log.d("Profile Api", "onResponse Retrofit Profile Data: " + response.body())
                            val res = response.body()

                            var item: AudienceDataResponse = res!!

                            val vibe_date = item.vibesDate.substringBefore("T")



                            val sdf = SimpleDateFormat("yyyy-MM-dd")

                            val date = Calendar.getInstance().time
                            val currentDate = sdf.format(Date())
                            Log.e("date-today",currentDate)
                            Log.e("date-vibe",vibe_date)

                            if(!currentDate.equals(vibe_date))
                            {
                                getCategoryApi()

                            }

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


    private fun updateVibe(id : String)
    {

        progressDialog.visibility = View.VISIBLE

        ApiClient.getInstance()!!.updateVibe(authorization,id)!!.enqueue(object : retrofit2.Callback<AudienceDataResponse>{
            override fun onResponse(
                call: Call<AudienceDataResponse>,
                response: Response<AudienceDataResponse>
            ) {
                progressDialog.visibility = View.GONE

                if(response.isSuccessful && response.body()!=null)
                {
                    var item: AudienceDataResponse = response.body()!!

                    val vibe_date = item.vibesDate.substringBefore("T")



                    val sdf = SimpleDateFormat("yyyy-MM-dd")

                    val date = Calendar.getInstance().time
                    val currentDate = sdf.format(Date())
                    Log.e("date-today",currentDate)
                    Log.e("date-vibe",vibe_date)

                    if(currentDate.equals(vibe_date))
                    {
//                        getCategoryApi()
                      Toast.makeText(requireContext(),"Vibes Shared",Toast.LENGTH_SHORT).show()

                    }

                }
            }

            override fun onFailure(call: Call<AudienceDataResponse>, t: Throwable) {
                progressDialog.visibility = View.GONE

            }
        })
    }



}