package com.dev.frequenc.ui_codes.connect.yourvibes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.connect.VibesProfileList.VibesUserListFragment
import com.dev.frequenc.ui_codes.connect.home.YourVibeResponse
import com.dev.frequenc.ui_codes.data.CategoryDetail
import com.dev.frequenc.ui_codes.data.GetVibeCategoryResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import retrofit2.Call
import retrofit2.Response


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
    lateinit var progressDialog : ProgressBar
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

        progressDialog = root.findViewById(R.id.progress_bar)

        recyclerView = root.findViewById(R.id.rvYourVibe)



        getCategoryApi()
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
                    recyclerView.apply {
                        layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
                        adapter = YourVibesAdapter(response.body()!!,this@YourVibeFragment)
                    }

                }
            }

            override fun onFailure(call: Call<GetVibeCategoryResponse>, t: Throwable) {
                progressDialog.visibility = View.GONE
            }
        })
    }

    override fun onClickAtVibe(item: CategoryDetail)
    {
        Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()

        val bundle = Bundle()
        bundle.putString("category", item.name) // Put anything what you want


        val fragment2 = VibesUserListFragment()
        fragment2.setArguments(bundle)


        requireActivity().supportFragmentManager .beginTransaction().replace(R.id.flFragment,fragment2).
        addToBackStack("YourVibeFragment")    .commit()

    }

}