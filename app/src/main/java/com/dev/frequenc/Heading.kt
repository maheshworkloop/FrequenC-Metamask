package com.dev.frequenc

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Heading(title: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        ){

    Image(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "",
        modifier = Modifier
            .size(27.dp)
            .padding(16.dp)
    )

    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
    }
}

@Preview
@Composable
fun PreviewHeading() {
    Heading(title = "Connect Dapp"
    )
}