package com.dev.frequenc

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.metamask.androidsdk.Dapp
import io.metamask.androidsdk.EthereumState

@Composable
fun ConnectScreen(
    ethereumState: EthereumState,
    onConnect: (Dapp, onError: (message: String) -> Unit) -> Unit,
    onDisconnect: () -> Unit,
    onClearSession: () -> Unit,
    navController: NavController,
    moveToActivity : (moveTo: String) -> Unit
) {

    val bottomMargin = 24.dp
    val connected = ethereumState.selectedAddress.isNotEmpty()

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface {
        AppTopBar(navController, DappScreen.CONNECT) {
            moveToActivity(DappScreen.Home.name)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Heading("MetaMask SDK Dapp")

            Spacer(modifier = Modifier.weight(1f))

            // Connect button
            if (connected) {
                moveToActivity(DappScreen.ACTIONS.name)
                DappButton(buttonText = stringResource(R.string.disconnect)) {
                    onDisconnect()
                }
            } else {
                DappButton(buttonText = stringResource(R.string.connect)) {
                    onConnect(Dapp("Droiddapp", "https://droiddapp.io")) { error ->
                        errorMessage = error
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            DappLabel(
                text = errorMessage ?: ethereumState.selectedAddress,
                color = if (errorMessage != null) {
                    Color.Red
                } else {
                    Color.Unspecified
                },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Clear session button
            DappButton(buttonText = stringResource(R.string.clear_session)) {
                onClearSession()
                errorMessage = null
            }

            Spacer(modifier = Modifier.height(4.dp))

            DappLabel(
                text = ethereumState.sessionId,
                modifier = Modifier.padding(bottom = bottomMargin)
            )
        }
    }
}

@Preview
@Composable
fun PreviewConnectClearButtons() {
    ConnectScreen(
        ethereumState = EthereumState("", "", ""),
        onConnect = { _, _ -> },
        onDisconnect = {},
        onClearSession = {},
        navController = rememberNavController(),
        moveToActivity = {}
    )
}