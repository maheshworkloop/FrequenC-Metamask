package com.dev.frequenc

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DappActionsScreen(
    navController: NavController,
    onSignMessage: () -> Unit,
    onSendTransaction: () -> Unit,
    onSwitchChain: () -> Unit
) {
    Surface {
        AppTopBar(navController, DappScreen.ACTIONS) {
        }

        Row (modifier = Modifier
            .fillMaxWidth()
            ) {
//            Text(text =
//                ,modifier = Modifier
//                .weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Heading("Dapp Actions")

            Spacer(modifier = Modifier.weight(1f))

            // Sign message button
            DappButton(buttonText = stringResource(R.string.sign)) {
                onSignMessage()
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Send transaction button
            DappButton(buttonText = stringResource(R.string.send)) {
                onSendTransaction()
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Switch chain button
            DappButton(buttonText = stringResource(R.string.switch_chain)) {
                onSwitchChain()
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}



@Preview
@Composable
fun PreviewDappActions() {
    DappActionsScreen(rememberNavController(), {}, {}, {})
}