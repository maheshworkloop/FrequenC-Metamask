package com.dev.frequenc.ui_codes.screens.Dashboard.wallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.dev.frequenc.databinding.FragmentWalletBinding
import com.dev.frequenc.ui_codes.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.metamask.androidsdk.Dapp
import io.metamask.androidsdk.ErrorType
import io.metamask.androidsdk.RequestError
import io.metamask.androidsdk.TAG


class WalletFragment : Fragment() {

    val dapp = Dapp("Droid Dapp", "https://droiddapp.com")

    private lateinit var walletViewModel: WalletViewModel
//    private val walletViewModel: WalletViewModel by activityViewModels()
//
//    companion object {
//        fun newInstance() = WalletFragment(walletViewModel)
//    }

    private lateinit var walletBinding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        walletBinding = FragmentWalletBinding.inflate(inflater, container, false)
        try {
            walletViewModel =
                ViewModelProvider(activity as MainActivity)[WalletViewModel::class.java]
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        activity?.runOnUiThread {
            walletViewModel.connectedVals.observe(viewLifecycleOwner) {
                if (it == true) {
                    walletBinding.btnConnect.text = "Disconnect"
                } else {
                    walletBinding.btnConnect.text = "Connect"
                }
            }
        }

        return walletBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        walletBinding.btnConnect.setOnClickListener {
            try {
                if (walletViewModel.connectedVals.value == false) {
                    walletViewModel.connect(dapp) { result ->
                        if (result is RequestError) {
                            Log.e(TAG, "Ethereum connection error: ${result.message}")
                        } else {
                            walletViewModel.setConnectedVals(true)
                            Log.d(TAG, "Ethereum connection result: $result")
                            showMessage(result.toString())
                        }
                    }
                } else {
                    walletViewModel.disconnect()
                    walletViewModel.setConnectedVals(false)
                }
            } catch (ex: Exception) {
                Log.e(TAG, "btnConnect: ", ex)
            }
        }

    }


    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }


    override fun onResume() {
        super.onResume()
        activity?.runOnUiThread {
            try {
                if (walletViewModel.connectedVals.value == true) {
                    walletViewModel.getBalence { result ->
                        if (result is RequestError) {
                            if (result.code.equals(ErrorType.UNAUTHORISED_REQUEST.code)) {
                                walletViewModel.setConnectedVals(false)
                            }
                        } else {
                            walletViewModel.setConnectedVals(true)
                            Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}

