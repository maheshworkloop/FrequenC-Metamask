package com.dev.frequenc.ui_codes.screens.Dashboard.savedevent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.saved_event.SavedEventResponseItem
import com.dev.frequenc.databinding.FragmentSavedEventBinding
import com.dev.frequenc.ui_codes.util.Constants
import com.dev.frequenc.ui_codes.util.ItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog

class SavedEventFragment : Fragment(), ItemClickListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var bindings: FragmentSavedEventBinding
    private lateinit var savedEventViewModel: SavedEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindings =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_saved_event, container, false)
        savedEventViewModel = ViewModelProvider(this)[SavedEventViewModel::class.java]
        sharedPreferences =
            activity?.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedEventAdapter = SavedEventAdapter(ArrayList<SavedEventResponseItem>(), this,object :
            ItemClickListener {
            override fun onItemClicked(position: Int) {
                moveToEventDetailsPage(position)
            }
        })
        savedEventViewModel.savedEventResponse.observe(viewLifecycleOwner) {
            it.let {
                if (it.size > 0) {
                    showNoData(false)
                    savedEventAdapter.refreshList(it)
                } else {
                    showNoData(true)
                    savedEventAdapter.refreshList(it)
                }
            }
        }
        bindings.rvEvents.adapter = savedEventAdapter

        savedEventViewModel.toastMessage.observe(viewLifecycleOwner) {
            it.let {
                if (!it.isNullOrEmpty()) {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        bindings.headerLays.btnBack.setOnClickListener {
            activity?.runOnUiThread {
                try {
                    activity?.let { requireActivity().supportFragmentManager.popBackStack() }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        savedEventViewModel.isApiCalled.observe(viewLifecycleOwner) {
            if (it) {
                bindings.progressBar.visibility = View.VISIBLE
            }
            else { bindings.progressBar.visibility = View.GONE }
        }
    }

    private fun moveToEventDetailsPage(position: Int) {

    }

    private fun showNoData(toShow: Boolean) {
        if (toShow) {
            bindings.noDataLay.noDataLay.visibility = View.VISIBLE
        } else {
            bindings.noDataLay.noDataLay.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        bindings.headerLays.tvHeader.text = "Saved Events"
        bindings.headerLays.btnBack.setImageResource(R.drawable.icon_left_arrow)
        val tokens: String = sharedPreferences.getString(Constants.Authorization, "").toString()
        tokens.let {
            savedEventViewModel.callSavedEventApi(it)
        }

    }

    override fun onItemClicked(position: Int) {
        if (position != -1) {


            showCancelDialog(position)


        }
    }

    fun showCancelDialog(position : Int)
    {
        val dialog = BottomSheetDialog(requireContext())
        val bottomSheet = layoutInflater.inflate(R.layout.layout_bottom_bookmark, null)
//           .bs_tv_remove .setOnClickListener { dialog.dismiss() }

        var tvCancel =  bottomSheet.findViewById<TextView>(R.id.bs_tv_cancel)
        var tvRemove =  bottomSheet.findViewById<TextView>(R.id.bs_tv_remove)

        tvRemove.setOnClickListener {
            dialog.dismiss()
            savedEventViewModel.callBookmarkEventApi(
                position,
                sharedPreferences.getString(Constants.Authorization, "").toString()
            )
        }

        tvCancel.setOnClickListener {
            dialog.dismiss()

        }

        dialog.setContentView(bottomSheet)
        dialog.show()
    }

}