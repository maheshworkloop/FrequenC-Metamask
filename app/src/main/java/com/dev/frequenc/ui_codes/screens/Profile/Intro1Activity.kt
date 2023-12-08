package com.dev.frequenc.ui_codes.screens.Profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityIntro1Binding
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.util.Constants

class Intro1Activity : AppCompatActivity() {

    lateinit var binding : ActivityIntro1Binding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntro1Binding.inflate(layoutInflater)

        setContentView(binding.root)

        Glide.with(this).load(R.drawable.frequenc_loader).into(binding.ivAnimSplash)

        sharedPreferences = getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!



        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            sharedPreferences.edit().putBoolean(
                Constants.isFirst_Time_User,
                false
            ).apply()
        }
    }
}