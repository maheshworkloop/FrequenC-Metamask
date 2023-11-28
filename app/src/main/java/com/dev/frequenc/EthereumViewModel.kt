package com.dev.frequenc

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.metamask.androidsdk.Dapp
import io.metamask.androidsdk.ErrorType
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.EthereumMethod
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.Logger
import io.metamask.androidsdk.Network
import io.metamask.androidsdk.RequestError
import io.metamask.androidsdk.TAG
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EthereumViewModel @Inject constructor(
    val ethereum: Ethereum
) : ViewModel() {

    var ethereumState = MediatorLiveData<EthereumState>().apply {
        addSource(ethereum.ethereumState) { newEthereumState ->
            value = newEthereumState
        }
    }

    fun connect(dapp: Dapp, onSuccess: (metamaskAddress: Any?) -> Unit, onError: (message: String) -> Unit) {
        ethereum.connect(dapp) { result ->
            if (result is RequestError) {
                Logger.log("Ethereum connection error: ${result.message}")
                onError(result.message)
            } else {
                Logger.log("Ethereum connection result: $result")
                try {
                    onSuccess(result.toString())
                }
                catch (ex: Exception ) {
                    onSuccess(null)
                }
            }
        }
    }

    fun disconnect() {
        ethereum.disconnect()
    }

    fun clearSession() {
        ethereum.clearSession()
    }

    fun signMessage(
        message: String,
        address: String,
        onSuccess: (Any?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val params: List<String> = listOf(address, message)

        val signRequest = EthereumRequest(
            UUID.randomUUID().toString(),
            EthereumMethod.ETH_SIGN_TYPED_DATA_V4.value,
            params
        )

        ethereum.sendRequest(signRequest) { result ->
            if (result is RequestError) {
                onError(result.message)
                Logger.log("Ethereum sign error: ${result.message}")
            } else {
                Logger.log("Ethereum sign result: $result")
                onSuccess(result)
            }
        }
    }

    fun sendTransaction(
        amount: String,
        from: String,
        to: String,
        onSuccess: (Any?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val params: MutableMap<String, Any> = mutableMapOf(
            "from" to from,
            "to" to to,
            "amount" to amount
        )

        val transactionRequest = EthereumRequest(
            UUID.randomUUID().toString(),
            EthereumMethod.ETH_SEND_TRANSACTION.value,
            listOf(params)
        )

        ethereum.sendRequest(transactionRequest) { result ->
            if (result is RequestError) {
                Logger.log("Ethereum transaction error: ${result.message}")
                onError(result.message)
            } else {
                Logger.log("Ethereum transaction result: $result")
                onSuccess(result)
            }
        }
    }

    fun getBalence(callback: ((Any?) -> Unit)?) {
        viewModelScope.launch {
//            val tokenContractAddress = "0x2f930D27e0502Ef690A418D03CF037d0509000c7"
            val ethereumAddress = ethereum.selectedAddress
//
//            val balanceOfFunctionSignature = calculateFunctionSignature("balanceOf(address)")
//            val data =
//                balanceOfFunctionSignature + ethereumAddress.removePrefix("0x").padStart(64, '0')

            Log.d(TAG, "getBalence==> ethereumAddress: $ethereumAddress")
// Params for ETH_CALL method
            val params: List<String> = listOf(
//                mapOf(
//                    "to" to tokenContractAddress,
//                    "data" to data
//                ),
                 ethereumAddress,
                "latest" // or "earliest" or "pending" (optional)
            )

// Create request using ETH_CALL method
            val getBalanceRequest = EthereumRequest(
                method = EthereumMethod.ETH_GET_BALANCE.value,
                params = params
            )

            ethereum.sendRequest(getBalanceRequest, callback)

//        {
//                result ->
//            if (result is RequestError) {
//                callback
//            } else {
//                balance = result.toString()
//            }
//        }
        }
    }


    fun switchChain(
        chainId: String,
        onSuccess: (message: String) -> Unit,
        onError: (message: String, action: (() -> Unit)?) -> Unit
    ) {
        val switchChainParams: Map<String, String> = mapOf("chainId" to chainId)
        val switchChainRequest = EthereumRequest(
            method = EthereumMethod.SWITCH_ETHEREUM_CHAIN.value,
            params = listOf(switchChainParams)
        )

        ethereum.sendRequest(switchChainRequest) { result ->
            if (result is RequestError) {
                if (result.code == ErrorType.UNRECOGNIZED_CHAIN_ID.code || result.code == ErrorType.SERVER_ERROR.code) {
                    val message =
                        "${Network.chainNameFor(chainId)} ($chainId) has not been added to your MetaMask wallet. Add chain?"

                    val action: () -> Unit = {
                        addEthereumChain(
                            chainId,
                            onSuccess = { result ->
                                onSuccess(result)
                            },
                            onError = { error ->
                                onError(error, null)
                            }
                        )
                    }
                    onError(message, action)
                } else {
                    onError("Switch chain error: ${result.message}", null)
                }
            } else {
                onSuccess("Successfully switched to ${Network.chainNameFor(chainId)} ($chainId)")
            }
        }
    }

    private fun addEthereumChain(
        chainId: String,
        onSuccess: (message: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        Logger.log("Adding chainId: $chainId")

        val addChainParams: Map<String, Any> = mapOf(
            "chainId" to chainId,
            "chainName" to Network.chainNameFor(chainId),
            "rpcUrls" to Network.rpcUrls(Network.fromChainId(chainId))
        )
        val addChainRequest = EthereumRequest(
            method = EthereumMethod.ADD_ETHEREUM_CHAIN.value,
            params = listOf(addChainParams)
        )

        ethereum.sendRequest(addChainRequest) { result ->
            if (result is RequestError) {
                onError("Add chain error: ${result.message}")
            } else {
                if (chainId == ethereum.chainId) {
                    onSuccess("Successfully switched to ${Network.chainNameFor(chainId)} ($chainId)")
                } else {
                    onSuccess("Successfully added ${Network.chainNameFor(chainId)} ($chainId)")
                }
            }
        }
    }
}