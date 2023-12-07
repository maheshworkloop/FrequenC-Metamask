package com.dev.frequenc.ui_codes.screens.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityIntroduceYourselfBinding

class IntroduceYourselfActivity : AppCompatActivity() {

    lateinit var binding : ActivityIntroduceYourselfBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroduceYourselfBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnMale.setOnClickListener {
            binding.btnMale.setBackgroundResource(R.drawable.bg_btn_gender_dark)
            binding.btnFemale.setBackgroundResource(R.drawable.bg_btn_gender)
        }

        binding.btnFemale.setOnClickListener {
            binding.btnMale.setBackgroundResource(R.drawable.bg_btn_gender)
            binding.btnFemale.setBackgroundResource(R.drawable.bg_btn_gender_dark)
        }

    }
}