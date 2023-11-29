package com.dev.frequenc.ui_codes.connect.VibesProfileList

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.connect.Profile.ProfileFragment
import com.dev.frequenc.ui_codes.data.ConnectionResponse
import com.dev.frequenc.ui_codes.data.MatchVibeData
import com.dev.frequenc.ui_codes.data.MatchVibeListResponse
import com.dev.frequenc.ui_codes.data.VibesProfileResponse
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
 * Use the [VibesUserListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VibesUserListFragment : Fragment(), VibesProfileListAdapter.ListAdapterListener, ConnectionAdapter.ListAdapterListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var root : View
    lateinit var rvVibeUser : RecyclerView
    lateinit var rvConnection : RecyclerView
    lateinit var category : String

    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences
    var userRegistered : Boolean = false
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
        root = inflater.inflate(R.layout.fragment_vibes_user_list, container, false)


        rvVibeUser = root.findViewById(R.id.rvVibeUser)
        rvConnection = root.findViewById(R.id.rvConnection)

        val bundle = this.arguments
        if (bundle != null) {
            category = bundle.getString("category").toString()
            Log.d("category",category)
        }

        progressDialog = root.findViewById(R.id.progress_bar)
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

            getMatchUserList(category)
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



        val mlist2 = listOf<ConnectionResponse>(
            ConnectionResponse(R.drawable.profile,true,""),
            ConnectionResponse(R.drawable.profile,true,""),
            ConnectionResponse(R.drawable.profile,false,""),
            ConnectionResponse(R.drawable.profile,true,""),
            ConnectionResponse(R.drawable.profile,false,"")
        )

        rvConnection.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = ConnectionAdapter(mlist2,this@VibesUserListFragment)
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
         * @return A new instance of fragment VibesUserListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VibesUserListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClickAtProfile(item: MatchVibeData) {

        val profile = ProfileFragment()

        Toast.makeText(requireContext(),"Profile Clicked",Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager .beginTransaction().replace(R.id.flFragment,profile).
        addToBackStack("ProfileFragment")    .commit()

        val bundle = Bundle()
        bundle.putString("audience_id",item._id)
        profile.arguments = bundle
    }

    override fun onClickAtConnection(item: ConnectionResponse) {
        Toast.makeText(requireContext(),"Connection Clicked",Toast.LENGTH_SHORT).show()

    }

    private fun getMatchUserList(category : String)
    {
        ApiClient.getInstance()!!.getMatchVibeList(authorization,category)!!.enqueue(object : retrofit2.Callback<MatchVibeListResponse>{
            override fun onResponse(
                call: Call<MatchVibeListResponse>,
                response: Response<MatchVibeListResponse>
            ) {
                if(response.isSuccessful && response.body()!=null)
                {
                    val mlist = response.body()!!.data

                    rvVibeUser.apply {
                        layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
                        adapter = VibesProfileListAdapter(mlist,this@VibesUserListFragment)
                    }
                }
            }

            override fun onFailure(call: Call<MatchVibeListResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }


}