package com.dev.frequenc.ui_codes.connect.VibesProfileList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.connect.Profile.ProfileFragment
import com.dev.frequenc.ui_codes.data.ConnectionResponse
import com.dev.frequenc.ui_codes.data.VibesProfileResponse

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

        val mList = listOf<VibesProfileResponse>(
            VibesProfileResponse(R.drawable.profile,"","Sofia","Angel,20"),
            VibesProfileResponse(R.drawable.profile,"","Kate","Pari,24"),
            VibesProfileResponse(R.drawable.profile,"","Nikky","Love,26"),
        )

        rvVibeUser.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = VibesProfileListAdapter(mList,this@VibesUserListFragment)
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

    override fun onClickAtProfile(item: VibesProfileResponse) {
        Toast.makeText(requireContext(),"Profile Clicked",Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager .beginTransaction().replace(R.id.flFragment,ProfileFragment()).
        addToBackStack("ProfileFragment")    .commit()
    }

    override fun onClickAtConnection(item: ConnectionResponse) {
        Toast.makeText(requireContext(),"Connection Clicked",Toast.LENGTH_SHORT).show()

    }
}