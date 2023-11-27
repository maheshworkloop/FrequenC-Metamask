package com.dev.frequenc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Heading("Dapp Actions")

            Spacer(modifier = Modifier.weight(1f))

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

@Composable
fun RewardLayer() {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxSize(),
        elevation = 4.dp,
        shape = RoundedCornerShape(7.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(7.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rewards_booking),
                    contentDescription = "Reward",
                    modifier = Modifier.size(56.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Rewards",
                    fontSize = 13.sp,
                    maxLines = 1
                )

                Text(
                    text = "\u20B9 0",
                    fontSize = 17.sp,
                    maxLines = 1
                )

                Text(
                    text = "Show Text",
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier
//                    .visibility(Visibility.Gone)
                )
            }
        }
    }
}


@Composable
fun WalletCard() {
    val walletIcon = painterResource(R.drawable.wallet_booking)

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        elevation = 4.dp,
        shape = RoundedCornerShape(7.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = walletIcon,
                contentDescription = "Wallet",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.FillBounds
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "My Wallet",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "\u20B9 0",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.Black
            )

            Text(
                text = "show_text",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier
//                    .visibility(Visibility.Gone)
            )
        }
    }
}

@Preview
@Composable
fun WalletCardPreview() {
    WalletCard()
    Spacer(modifier = Modifier
        .padding(20.dp))
    RewardLayer()
}
@Preview
@Composable
fun PreviewDappActions() {
    DappActionsScreen(rememberNavController(), {}, {}, {})
}