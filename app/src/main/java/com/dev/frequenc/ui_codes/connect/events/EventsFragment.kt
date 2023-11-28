package com.dev.frequenc.ui_codes.connect.events

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.Data
import com.dev.frequenc.ui_codes.data.GetVibeCategoryResponse
import com.dev.frequenc.ui_codes.data.VibeEventResponse
import com.dev.frequenc.ui_codes.screens.EventDetail.EventDetailActivity
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EventsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventsFragment : Fragment(),VibesEventAdapter.ListAdapterListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var root : View
    lateinit var recyclerView: RecyclerView
    lateinit var mlist : List<VibeEventResponse>

    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences
    var userRegistered : Boolean = false

    lateinit var loginBtn : TextView
    lateinit var ivHamburger : ImageView
    lateinit var ivNotification: ImageView
    lateinit var progressDialog: ProgressBar


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
        root = inflater.inflate(R.layout.fragment_events, container, false)

        recyclerView = root.findViewById(R.id.rvConnectEvents)
        progressDialog = root.findViewById(R.id.progress_bar)

        sharedPreferences = activity?.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!

        userRegistered = sharedPreferences.getBoolean(Constants.isUserTypeRegistered, false)


        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId,"-1").toString()


        getCategoryApi()

        if(userRegistered && !authorization.isNullOrEmpty() &&authorization!="-1" && !audience_id.isNullOrEmpty() )
        {

            Log.d("Audience Id",audience_id)
            Log.d("Bearer",authorization)
//            Toast.makeText(requireContext(),"Login Success", Toast.LENGTH_SHORT).show()
//            loginBtn.visibility = View.INVISIBLE
//            ivHamburger.visibility =View.VISIBLE
//            ivNotification.visibility =View.VISIBLE

            vibeEventsTokenApi(authorization)


        }
        else
        {


//            Toast.makeText(requireContext(),"Not Logged in Failure", Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment EventsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EventsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun vibeEventsTokenApi(authorization : String)
    {
        progressDialog.visibility = View.VISIBLE

        ApiClient.getInstance()!!.vibeEvent(authorization)!!.enqueue(object : retrofit2.Callback<VibeEventResponse>{
            override fun onResponse(
                call: Call<VibeEventResponse>,
                response: Response<VibeEventResponse>
            ) {

                progressDialog.visibility = View.GONE


                if (response.isSuccessful() && response.body() != null
                ) {
                    val res = response.body()!!.data

                    val vibeEventResponse : VibeEventResponse = response.body()!!


                    recyclerView.apply {
                        layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
                        adapter = VibesEventAdapter(vibeEventResponse,this@EventsFragment)
                    }

                }
            }

            override fun onFailure(call: Call<VibeEventResponse>, t: Throwable) {
                progressDialog.visibility = View.GONE

            }

        })
    }

    override fun onClickAtVibe(item: Data) {

        val intent = Intent(requireContext(),EventDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString("eventid",item._id)
        Log.d("eventid",item._id)

        intent.putExtras(bundle)
        startActivity(intent)

    }


    private fun getCategoryApi()
    {
        ApiClient.getInstance()!!.getVibeCategory()!!.enqueue(object : retrofit2.Callback<GetVibeCategoryResponse>{
            override fun onResponse(
                call: Call<GetVibeCategoryResponse>,
                response: Response<GetVibeCategoryResponse>
            ) {

            }

            override fun onFailure(call: Call<GetVibeCategoryResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }


}