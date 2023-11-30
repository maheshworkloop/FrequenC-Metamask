package com.dev.frequenc.ui_codes.connect.Profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.AudienceDataResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var root : View
    lateinit var viewPagerProfileAdapter : ViewPager2
    lateinit var tabLayoutProfile : TabLayout
    lateinit var audience_id : String
    lateinit var progressDialog : ProgressBar

    lateinit var authorization : String
    private lateinit var sharedPreferences: SharedPreferences
    var userRegistered : Boolean = false

    lateinit var tvAboutMe : TextView
    lateinit var tvAddress : TextView
    var cityList = ArrayList<String>()


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
        root = inflater.inflate(R.layout.fragment_profile, container, false)

        progressDialog = root.findViewById(R.id.progressDialog)

        tvAboutMe = root.findViewById(R.id.tvAboutMe)
        tvAddress = root.findViewById(R.id.tvAddress)

        val bundle = this.arguments
        if(bundle!=null)
        {
            audience_id = bundle.getString("audience_id")!!
        }

        viewPagerProfileAdapter = root.findViewById(R.id.viewPagerProfile)

        tabLayoutProfile = root.findViewById(R.id.tabLayoutProfile)



        sharedPreferences = activity?.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!
        userRegistered = sharedPreferences.getBoolean(Constants.isUserTypeRegistered, false)
        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId,"-1").toString()

        if(userRegistered && !authorization.isNullOrEmpty() &&authorization!="-1" && !audience_id.isNullOrEmpty() )
        {

            Log.d("Audience Id",audience_id)
            Log.d("Bearer",authorization)
//            Toast.makeText(requireContext(),"Login Success", Toast.LENGTH_SHORT).show()
//            loginBtn.visibility = View.INVISIBLE
//            ivHamburger.visibility =View.VISIBLE
//            ivNotification.visibility =View.VISIBLE
            getProfileApi()
//            getMatchUserList("Party")
        }
        else
        {

            Log.e("Audience Id",audience_id)
//            loginBtn.visibility = View.VISIBLE
//            ivHamburger.visibility =View.INVISIBLE
//            ivNotification.visibility =View.INVISIBLE


//            Log.e("Bearer",authorization)
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun setupTabs(mlist : List<String>) {


        val adapter = ViewPager2ProfileAdapter(requireContext(),mlist)
        viewPagerProfileAdapter.adapter = adapter

        TabLayoutMediator(tabLayoutProfile,viewPagerProfileAdapter){ tab, position ->{
            tab.text = position.toString()
        }
//            if(!courseModelArrayList[position].name.contains("Others", ignoreCase = true)) {
//                tab.text = courseModelArrayList[position].name
//            }
        }.attach()

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
                            Log.d("profile",item.name)

                            val mlist = listOf<String>(item.banner_image)

                            tvAboutMe.text = item.description

                            tvAddress.text = item.city



                            setupTabs(mlist)


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






}