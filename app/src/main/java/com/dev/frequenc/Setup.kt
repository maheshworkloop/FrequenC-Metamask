package com.dev.frequenc

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.frequenc.DappScreen.*
import com.dev.frequenc.util.Constants
import io.metamask.androidsdk.*

@Composable
fun Setup(
    ethereumViewModel: EthereumViewModel,
    screenViewModel: ScreenViewModel,
    sharedPreferences: SharedPreferences,
    onBackPressed: () -> Unit
) {
    val navController = rememberNavController()
    val ethereumState by ethereumViewModel.ethereumState.observeAsState(EthereumState("", "", ""))
    val sharedPreferencesEditor = sharedPreferences.edit()

    NavHost(navController = navController, startDestination = CONNECT.name) {
        composable(CONNECT.name) {
            ConnectScreen(
                ethereumState = ethereumState,
                onConnect = { dapp, onError ->
                    ethereumViewModel.connect(
                        dapp,
                        onSuccess = { if (it!= null && it.toString().length>6 ) {sharedPreferencesEditor.putString(Constants.MetaMaskWalletAddress,it.toString()).apply()}
                            screenViewModel.setScreen(ACTIONS)
                                    },
                        onError
                    )
                },
                onDisconnect = {
                    ethereumViewModel.disconnect()
                },
                onClearSession = {
                    ethereumViewModel.clearSession()
                },
                navController = navController
            )
            {
                if (Home.name.equals(it)) {
                    onBackPressed()
                }
//                if (navController.currentBackStackEntry.)
//                when (it) {
//                    ACTIONS.name -> {
////                        screenViewModel.setScreen(ACTIONS)
//                        navController.navigate(ACTIONS.name)
//                    }
//
//                    SIGN_MESSAGE.name -> {
////                        screenViewModel.setScreen(SIGN_MESSAGE)
//
//                        navController.navigate(SIGN_MESSAGE.name)
//                    }
//
//                    SEND_TRANSACTION.name -> {
////                        screenViewModel.setScreen(SEND_TRANSACTION)
//                        navController.navigate(SEND_TRANSACTION.name)
//                    }
//
//                    SWITCH_CHAIN.name -> {
////                        screenViewModel.setScreen(SWITCH_CHAIN)
//                        navController.navigate(SWITCH_CHAIN.name)
//                    }
//
//                    Home.name -> {
//                        screenViewModel.setScreen(Home)
//                        onBackPressed()
//                    }
//                }
            }
        }

        composable(ACTIONS.name) {
            DappActionsScreen(
                navController,
                onSignMessage = { screenViewModel.setScreen(SIGN_MESSAGE) },
                onSendTransaction = { screenViewModel.setScreen(SEND_TRANSACTION) },
                onSwitchChain = { screenViewModel.setScreen(SWITCH_CHAIN) }
            )
        }
        composable(SIGN_MESSAGE.name                                                     ) {
            SignMessageScreen(
                navController,
                ethereumState = ethereumState,
                signMessage = { message, address, onSuccess, onError ->
                    ethereumViewModel.signMessage(message, address, onSuccess, onError)
                })
        }
        composable(SEND_TRANSACTION.name) {
            SendTransactionScreen(
                navController,
                ethereumState = ethereumState,
                sendTransaction = { amount, from, to, onSuccess, onError ->
                    ethereumViewModel.sendTransaction(amount, from, to, onSuccess, onError)
                })
        }
        composable(SWITCH_CHAIN.name) {
            SwitchChainScreen(
                navController,
                ethereumState = ethereumState,
                switchChain = { chainId, onSuccess, onError ->
                    ethereumViewModel.switchChain(chainId, onSuccess, onError)
                }
            )
        }

    }

    when (screenViewModel.currentScreen.value) {
        CONNECT -> {
            navController.navigate(CONNECT.name)
            {
                navController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        ACTIONS -> {
            ethereumViewModel.getBalence { result ->
                if (result is RequestError) {
                    Log.d(TAG, "getBalence: ${result.message}")
                } else {
                    Log.d(TAG, "getBalence: ${result.toString()}")
                }
            }
            navController.navigate(ACTIONS.name)
//            {
//                navController.graph.startDestinationRoute?.let { route ->
//                    popUpTo(route) {
//                        saveState = true
//                    }
//                }
////                launchSingleTop = true
//                restoreState = true
//            }
        }

        SIGN_MESSAGE -> {
            navController.navigate(SIGN_MESSAGE.name)
//            {
//                navController.graph.startDestinationRoute?.let { route ->
//                    popUpTo(route) {
//                        saveState = true
//                    }
//                }
////                launchSingleTop = true
//                restoreState = true
//            }
        }

        SEND_TRANSACTION -> {
            navController.navigate(SEND_TRANSACTION.name)
//            {
//                navController.graph.startDestinationRoute?.let { route ->
//                    popUpTo(route) {
//                        saveState = true
//                    }
//                }
////                launchSingleTop = true
//                restoreState = true
//            }
        }

        SWITCH_CHAIN -> {
            navController.navigate(SWITCH_CHAIN.name)
//            {
//                navController.graph.startDestinationRoute?.let { route ->
//                    popUpTo(route) {
//                        saveState = true
//                    }
//                }
////                launchSingleTop = true
//                restoreState = true
//            }
        }

        Home -> {
            onBackPressed()
        }
    }

}