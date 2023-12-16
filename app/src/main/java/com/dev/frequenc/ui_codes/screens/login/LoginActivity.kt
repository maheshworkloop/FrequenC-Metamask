package com.dev.frequenc.ui_codes.screens.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.screens.intro.IntroduceYourselfActivity
import com.dev.frequenc.ui_codes.util.Constants
import com.dev.frequenc.util.DataChangeListener

class LoginActivity : AppCompatActivity(), DataChangeListener {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences

    private val loginFragment = LoginFragment()
    private val verifyOtpFragment = VerifyOtpFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences =
            this.getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)
        sharedPreferencesEditor =
            sharedPreferences.edit()

        val loginViewModelFactory: LoginViewModelFactory = LoginViewModelFactory()
        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)

        addFragment("LoginFragment", loginFragment)
//        loginViewModel.currentFragmentTag.observe( this) { result ->
//            if (result.equals("0")) {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.containerLays, loginFragment, "LoginFragment")
//                    .commit()
//            } else if (result.equals("1")) {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.containerLays, verifyOtpFragment, "VerifyOtpFragment")
//                    .commit()
//            } else if (result.equals("2")) {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.containerLays, userTypeFragment, "UserTypeFragment")
//                    .commit()
//            } else {
//                backToHome()
//            }
//        }

        progressBar = findViewById(R.id.progressBar)
//        setContent {
//            createLoginView()
//        }
//
//        loginViewModel.toastMessage.observe(this) { it ->
//            run {
//                if (!it.isNullOrBlank() && !it.isNullOrEmpty())
//                    Toast.makeText(this, it, Toast.LENGTH_LONG)
//            }
//        }


    }


//
//
//    //    @Preview(showBackground = true)
//    @Composable
//    fun createLoginView() {
//        Box( modifier = Modifier
//            .fillMaxSize(),
//                ) {
//            Image(
//                painter = painterResource(id = R . drawable . background),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
//            Box ( modifier = Modifier) {
////                Image(painter = painterResource(id = R.drawable.blurbackground) ,
////                    contentDescription = null,
////                    modifier = Modifier.fillMaxSize(),
////                    contentScale = ContentScale.FillBounds)
//            Column (
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(19.dp)
//                    .background(color = Color.Black)
//            ) {
//                Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
//                Row(
//                    modifier  = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.Top,
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Box(modifier = Modifier
//                        .size(27.dp)
//                        .background(color = Color.Blue)
//                        .aspectRatio(1f)
//                        .clickable {
//                            backToHome()
//                        },
//                        contentAlignment = Alignment.TopStart
//                    ) {
//                        Icon(imageVector = Icons.Default.ArrowBack,
//                            contentDescription = null,
//                            tint = Color.White,
//                            modifier = Modifier.fillMaxSize())
//                    }
//                }
////                gifImageShown()
////
//
//                Spacer(modifier = Modifier
//                    .height(10.dp)
//                    .fillMaxWidth())
//                Text(
//                    text = "Sign In To frequenC",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f),
//                    color = Color.White,
//                    style = TextStyle(
//                        fontFamily = FontFamily(Font(R.font.robotomedium)),
//                        fontWeight = FontWeight.W500, // Adjust this weight as needed
//                        fontSize = 23.sp
//                    ),
//                    textAlign = TextAlign.Center
//                )
//
//
//                Spacer(modifier = Modifier
//                    .height(10.dp)
//                    .fillMaxWidth())
//
//            }
//            }
//
//        }
//
//
//    }


    private fun backToHome() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    override fun onDataChange(data: Any, use_type: String) {
        when (use_type) {
            "move" -> {

                if (data.equals("0")) {
                    addORReplaceFragment("LoginFragment", loginFragment)
                } else if (data.equals("1")) {
                    addORReplaceFragment("VerifyOtpFragment", verifyOtpFragment)
                } else if (data.equals("2") && !sharedPreferences.getBoolean( Constants.isUserTypeRegistered, false)) {
                    addFragment("UserTypeFragment", UserTypeFragment())
                }
                else if (data.equals("-2") && !sharedPreferences.getBoolean( Constants.isUserTypeRegistered, false)) {
                    val intent = Intent(this,IntroduceYourselfActivity::class.java)
                    startActivity(intent)
                }
                else {
                    if (!data.toString().isNullOrEmpty() && data.equals("-1")) {
                        backToHome()
                    }
                }
            }

            "api" -> {
                when (data) {
                    true -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    else -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            "message" -> {
                if (!data?.toString().isNullOrEmpty()) Toast.makeText(
                    this@LoginActivity,
                    data.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun addORReplaceFragment(fragmentTag: String, fragment: Fragment) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(
            supportFragmentManager.fragments.last().requireView().windowToken, 0
        )
            if (supportFragmentManager.findFragmentByTag(fragmentTag) == null) {
                addFragment(fragmentTag, fragment)
            } else {
                replaceFragment(fragmentTag, fragment)
            }
    }

    private fun addFragment(fragmentTag: String, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.containerLays, fragment, fragmentTag)
            .commit()
    }

    private fun replaceFragment(fragmentTag: String, fragment: Fragment) {

        if (supportFragmentManager.findFragmentByTag(fragmentTag)?.isVisible == false) {
//        if (supportFragmentManager.findFragmentByTag(fragmentTag)?.isVisible == false) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerLays, fragment, fragmentTag)
                .commit()
//        }
        }
    }
//
//
//    //    @Preview(showBackground = true )
//    @Composable
//    fun gifImageShown() {
//        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            val context = LocalView.current.context
//
//            val imageView = ImageView(context)
//            Glide.with(context)
//                .asGif()
//                .load(R.drawable.frequenc_logo) // Replace with the resource ID of your GIF
//                .apply(requestOptions)
//                .into(imageView)
//
//            var density  = LocalDensity.current.density
//            AndroidView(
//                factory = {
//                    imageView.layoutParams = ViewGroup.LayoutParams(
//                        (200f * density).toInt(),
//                        (200f * density).toInt()
//                    )
//                    imageView
//                },
//                modifier = Modifier.fillMaxSize(),
//                update = { view ->
//                    // You can update the view here if needed
//                })
//        }
//    }

}
