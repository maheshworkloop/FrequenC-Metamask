package com.dev.frequenc.ui_codes.screens.intro

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityIntroduceYourselfBinding
import com.dev.frequenc.util.AppCommonMethods
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IntroduceYourselfActivity : AppCompatActivity() {

    lateinit var binding : ActivityIntroduceYourselfBinding

    var flagNext = false

    var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroduceYourselfBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.ivNext.setOnClickListener {
            if(flagNext)
            {
                val intent = Intent(this,PresentationActivity::class.java)
                 startActivity(intent)
            }
            else
            {
                val intent = Intent(this,PresentationActivity::class.java)
                startActivity(intent)
                Toast.makeText(this,"Fill Up all details !",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMale.setOnClickListener {
            binding.btnMale.setBackgroundResource(R.drawable.bg_btn_gender_dark)
            binding.btnFemale.setBackgroundResource(R.drawable.bg_btn_gender)

            binding.btnFemale.setTextColor( resources.getColor(R.color.material_grey_800) )
            binding.btnMale.setTextColor( resources.getColor(R.color.white) )

            gender = "Male"

            checkCondition()

        }


        binding.tvDob.setOnClickListener {
            dateTimePickerDialog()
        }

        binding.btnFemale.setOnClickListener {
            binding.btnMale.setBackgroundResource(R.drawable.bg_btn_gender)
            binding.btnFemale.setBackgroundResource(R.drawable.bg_btn_gender_dark)
            binding.btnFemale.setTextColor( resources.getColor(R.color.white) )
            binding.btnMale.setTextColor( resources.getColor(R.color.material_grey_800) )

            gender = "Female"
            checkCondition()


        }


        binding.etName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {


            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

             checkCondition()

            }
        })

        binding.etEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){
               checkCondition()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        binding.etLocation.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               checkCondition()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


    }


    private fun checkCondition() : Boolean
    {
        if(binding.etName.text.isNullOrEmpty())
        {
            binding.ivNext.setBackgroundResource(R.drawable.bg_img_intro)
            flagNext = false
            return false
        }
        else if(binding.etEmail.text.isNullOrEmpty() && !AppCommonMethods.checkForEmail(binding.etEmail))
        {
            binding.ivNext.setBackgroundResource(R.drawable.bg_img_intro)
            flagNext = false
            return false
        }
        else if(binding.etLocation.text.isNullOrEmpty())
        {
            binding.ivNext.setBackgroundResource(R.drawable.bg_img_intro)
            flagNext = false
            return false
        }
        else if(binding.tvDob.text.isNullOrEmpty())
        {
            binding.ivNext.setBackgroundResource(R.drawable.bg_img_intro)
            flagNext = false
            return false
        }
        else if(gender.isNullOrEmpty())
        {
            binding.ivNext.setBackgroundResource(R.drawable.bg_img_intro)
            flagNext = false
            return false
        }

        binding.ivNext.setBackgroundResource(R.drawable.bg_img_intro_dark)
        flagNext = true
        return true
    }

    private fun dateTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a date picker dialog
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Handle the selected date
                binding.tvDob.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    .format(Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDay)
                    }.time)

                checkCondition()
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        // Show the date picker dialog
        datePickerDialog.show()

    }

}