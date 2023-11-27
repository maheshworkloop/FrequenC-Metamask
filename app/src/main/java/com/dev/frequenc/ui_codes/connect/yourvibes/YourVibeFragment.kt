package com.dev.frequenc.ui_codes.connect.yourvibes

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.connect.VibesProfileList.VibesUserListFragment
import com.dev.frequenc.ui_codes.connect.home.YourVibeResponse
import pl.droidsonroids.gif.GifImageView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [YourVibeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YourVibeFragment : Fragment(), YourVibesAdapter.ListAdapterListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var root : View
    lateinit var recyclerView: RecyclerView
    lateinit var mlist : List<YourVibeResponse>
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
        root = inflater.inflate(R.layout.fragment_your_vibe, container, false)

        recyclerView = root.findViewById(R.id.rvYourVibe)
        var item1 = YourVibeResponse(R.drawable.lookingforlove,"Looking For Love")
        var item2 = YourVibeResponse(R.drawable.lookingforlove,"Free Tonight")
        var item3 = YourVibeResponse(R.drawable.lookingforlove,"Let's be friend")
         mlist = listOf(item1,item2,item3)




        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = YourVibesAdapter(mlist,this@YourVibeFragment)
        }


//        showPopUp()

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment YourVibeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YourVibeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }




    override fun onClickAtVibe(item: YourVibeResponse) {

      Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()

       requireActivity().supportFragmentManager .beginTransaction().replace(R.id.flFragment,VibesUserListFragment()).
       addToBackStack("YourVibeFragment")    .commit()
    }





}