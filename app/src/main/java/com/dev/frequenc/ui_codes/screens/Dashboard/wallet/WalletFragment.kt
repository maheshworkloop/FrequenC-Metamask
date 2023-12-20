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


    private lateinit var walletBinding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        walletBinding = FragmentWalletBinding.inflate(inflater, container, false)


        return walletBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
    }

}

