package com.dev.frequenc.ui_codes.connect.home

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.connect.events.EventsFragment
import com.dev.frequenc.ui_codes.connect.yourvibes.ShareVibesAdapter
import com.dev.frequenc.ui_codes.connect.yourvibes.YourVibeFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import pl.droidsonroids.gif.GifImageView

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
    lateinit var gifImageView : GifImageView


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


        showPopUpConnectionRequest()

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
        Handler().postDelayed({
            showPopUp()
        }, 10000)


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



    private fun showPopUp()
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
            adapter = ShareVibesAdapter(mlist,this@ConnectHomeFragment)
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

    override fun onClickAtShareVibe(item: YourVibeResponse) {
        dialog.cancel()
    }

}