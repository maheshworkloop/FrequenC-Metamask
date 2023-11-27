package com.dev.frequenc

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dev.frequenc.theme.MetaMaskAndroidSDKClientTheme
import com.dev.frequenc.ui_codes.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(){

    private val ethereumViewModel: EthereumViewModel by viewModels()
    private val screenViewModel: ScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MetaMaskAndroidSDKClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                    ) {
                        Setup(ethereumViewModel, screenViewModel) {
                            startActivity(Intent( this@MainActivity, MainActivity:: class.java))
                        }
                }
            }
        }
    }

}
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MetaMaskAndroidSDKClientTheme {
        Greeting("Android")
    }

}
