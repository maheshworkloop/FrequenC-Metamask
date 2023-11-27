package com.dev.frequenc

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dev.frequenc.theme.MetaMaskAndroidSDKClientTheme
import com.dev.frequenc.ui_codes.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val ethereumViewModel: EthereumViewModel by viewModels()
    private val screenViewModel: ScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MetaMaskAndroidSDKClientTheme {
                var activityKey by rememberSaveable { mutableIntStateOf(0) }

//                if (activityKey == 0) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Setup(ethereumViewModel, screenViewModel) {
                            // Update the activity key when starting another activity
//                            activityKey++
                            startActivity(Intent(this@MainActivity, MainActivity::class.java))
                        }
//                    }
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
