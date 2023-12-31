package com.dev.frequenc.ui_codes.connect.VibesProfileList

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.connect.Profile.ProfileFragment
import com.dev.frequenc.ui_codes.data.ConnectionResponse
import com.dev.frequenc.ui_codes.data.MatchVibeData
import com.dev.frequenc.ui_codes.data.MatchVibeListResponse
import com.dev.frequenc.ui_codes.data.QuoteResponse
import com.dev.frequenc.ui_codes.data.SendInvitationResponse
import com.dev.frequenc.ui_codes.data.myconnection.Data
import com.dev.frequenc.ui_codes.data.myconnection.MyConnectionResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.ui_codes.util.Constants
import io.agora.chat.ChatClient
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Period

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VibesUserListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VibesUserListFragment : Fragment(), VibesProfileListAdapter.ListAdapterListener,
    ConnectionAdapter.ListAdapterListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var root: View
    lateinit var rvVibeUser: RecyclerView
    lateinit var rvConnection: RecyclerView
    lateinit var rvQuote: RecyclerView
    lateinit var category: String
    lateinit var tvConnectionTag: TextView

    lateinit var authorization: String
    lateinit var audience_id: String
    private lateinit var sharedPreferences: SharedPreferences
    var userRegistered: Boolean = false
    lateinit var progressDialog: ProgressBar

    private lateinit var _ivAnim: GifImageView
    var ivAnim : GifImageView
        get() = _ivAnim
        set(value) {
            _ivAnim = value
        }
    lateinit var tvVibeTag : TextView
    lateinit var ivHamburger: ImageView


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

        ivHamburger = root.findViewById(R.id.ivHamburger)
        rvVibeUser = root.findViewById(R.id.rvVibeUser)
        rvConnection = root.findViewById(R.id.rvConnection)
        rvQuote = root.findViewById(R.id.rvQuote)
        tvVibeTag = root.findViewById(R.id.tvVibeTag)
        ivAnim = root.findViewById(R.id.ivAnimSplashConnct)

        tvConnectionTag = root.findViewById<TextView>(R.id.tvConnectionTag)

        val bundle = this.arguments
        if (bundle != null) {
            category = bundle.getString("category").toString()
            Log.d("category", category)
            tvVibeTag.text = category

        }

        progressDialog = root.findViewById(R.id.progress_bar)
        sharedPreferences =
            activity?.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!
        userRegistered = sharedPreferences.getBoolean(Constants.isUserTypeRegistered, false)
        authorization = sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId, "-1").toString()


        callConnectionApi(authorization)


        Glide.with(requireContext()).asGif().load(R.drawable.frequenc_loader).into(ivAnim)


        if (userRegistered && !authorization.isNullOrEmpty() && authorization != "-1" && !audience_id.isNullOrEmpty()) {

            Log.d("Audience Id", audience_id)
            Log.d("Bearer", authorization)
//            Toast.makeText(requireContext(),"Login Success", Toast.LENGTH_SHORT).show()
//            loginBtn.visibility = View.INVISIBLE
//            ivHamburger.visibility =View.VISIBLE
//            ivNotification.visibility =View.VISIBLE

            getMatchUserList(category)
//            getMatchUserList("Party")

            ivHamburger.setOnClickListener {
                (activity as MainActivity).binding.drawerLayout.openDrawer(GravityCompat.END)
            }

        } else {

            Log.e("Audience Id", audience_id)
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
        try {
            ChatClient.getInstance().contactManager().addContact(item._id, "connect")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        ApiClient.getInstance()?.callInvitationApi(sharedPreferences.getString(Constants.Authorization,null).toString(), item._id)?.enqueue(object :
            Callback<SendInvitationResponse> {
            override fun onResponse(
                call: Call<SendInvitationResponse>,
                response: Response<SendInvitationResponse>
            ) {
                try {
                    Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    val profile = ProfileFragment()

                    Toast.makeText(requireContext(), "Profile Clicked", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, profile).addToBackStack("ProfileFragment").commit()

                    val bundle = Bundle()
                    bundle.putString("audience_id", item._id)
                    profile.arguments = bundle
                 }
                catch (ex: Exception)
                {
                    ex.printStackTrace()
                }
            }

            override fun onFailure(call: Call<SendInvitationResponse>, t: Throwable) {
                Log.e(Constants.ApiError, "onFailure: ", t)
            }
        })

    }

    override fun onClickAtConnection(item: ConnectionResponse) {
        Toast.makeText(requireContext(), "Connection Clicked", Toast.LENGTH_SHORT).show()

    }

    private fun getMatchUserList(category: String) {
        ApiClient.getInstance()!!.getMatchVibeList(authorization, category)!!
            .enqueue(object : retrofit2.Callback<MatchVibeListResponse> {
                override fun onResponse(
                    call: Call<MatchVibeListResponse>,
                    response: Response<MatchVibeListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val mlist = response.body()!!.data

                        rvVibeUser.apply {
                            layoutManager = GridLayoutManager(
                                requireContext(),
                                2,
                                GridLayoutManager.VERTICAL,
                                false
                            )
                            adapter = VibesProfileListAdapter(mlist, this@VibesUserListFragment)
                        }
                    }
                }

                override fun onFailure(call: Call<MatchVibeListResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun callConnectionApi(token: String) {

        Log.d("flow", "Connection Api Called")

        ApiClient.getInstance()?.connectionList(token)
            ?.enqueue(object : Callback<MyConnectionResponse> {
                override fun onResponse(
                    call: Call<MyConnectionResponse>,
                    response: Response<MyConnectionResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null && response.body()?.data != null) {

                            Log.d("flow", "Connection Api Success")


                            val count = response.body()!!.count

                            tvConnectionTag.text = "Connection ($count)"

                            if (count == 0) {
                                rvConnection.visibility = View.GONE
                                rvQuote.visibility = View.VISIBLE
                                Toast.makeText(requireContext(),"No Connection",Toast.LENGTH_SHORT).show()
                                getQuotes()


                            } else {
                                rvConnection.visibility = View.VISIBLE
                                rvQuote.visibility = View.GONE

                                val adapterLists = ArrayList<ConnectionResponse>()

                                for (data: Data in response.body()?.data!!) {
                                    var images: String = ""
                                    try {
                                        images = data.from_user_id.audience_id.profile_pic
                                    } catch (exs: Exception) {
                                        exs.printStackTrace()
                                    }

                                    var personAge: String = ""
                                    try {
                                        val dateParts: List<String> = data.from_user_id.audience_id.dob.split("-")
                                        val day:String = dateParts[2]
                                        val month = dateParts[1]
                                        val year = dateParts[0]
//        item.dob
                                        @RequiresApi(Build.VERSION_CODES.O)
                                        personAge = getAge(year.toInt(), month.toInt(),day.toInt()).toString()
                                    }
                                    catch (ex: Exception) {
                                        ex.printStackTrace()
                                    }
                                    adapterLists.add(ConnectionResponse(
                                        images,
                                        data.from_user_id.fullName,
                                        data.from_user_id._id,
                                        personAge,
                                        null,
                                        data.from_user_id.gender,
                                    ))

                                }


                                rvConnection.apply {
                                    adapter =
                                        ConnectionAdapter(adapterLists, ArrayList<Boolean>(adapterLists.size),this@VibesUserListFragment)
                                    layoutManager = LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )

                                }
                            }


//                            _userLists.postValue(adapterLists)
                        } else {
//                            _userLists.postValue(emptyList())
                        }
                    } else {
                        Log.d(Constants.Error, "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MyConnectionResponse>, t: Throwable) {
                    Log.e(Constants.Error, "onFailure: ", t)
                    Log.d("flow", "Connection Api Error")

                }
            })
    }

    private fun getQuotes() {
        Log.d("api", "calling get quotes api")
        ApiClient.getInstance()!!.getQuoteApi()!!
            .enqueue(object : retrofit2.Callback<QuoteResponse> {
                override fun onResponse(
                    call: Call<QuoteResponse>,
                    response: Response<QuoteResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null && response.body()!!.data != null) {

                            Log.d("api","response success get quotes")

                            val mData = response.body()!!

//                            Log.d("api",mData.data.get(0).name)


                            for(i in mData.data)
                             Log.d("api",i.name.toString())

                            rvQuote.apply {
                                adapter = QuoteAdapter(mData)
                                layoutManager = LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )

                            }
                        }
                    }
                }

                override fun onFailure(call: Call<QuoteResponse>, t: Throwable) {
                    Toast.makeText(requireContext(),t.localizedMessage,Toast.LENGTH_SHORT).show()
                }
            })


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }

}