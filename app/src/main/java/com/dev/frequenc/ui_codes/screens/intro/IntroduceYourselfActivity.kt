package com.dev.frequenc.ui_codes.screens.intro

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityIntroduceYourselfBinding
import com.dev.frequenc.ui_codes.data.ProfileRes
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.AppCommonMethods
import com.dev.frequenc.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IntroduceYourselfActivity : AppCompatActivity() {

    lateinit var binding : ActivityIntroduceYourselfBinding

    var flagNext = false

    var gender = ""
    var dob = ""

    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroduceYourselfBinding.inflate(layoutInflater)

        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!

        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId,"-1").toString()

        binding.ivNext.setOnClickListener {
            if(flagNext)
            {

                Log.d("audience_id4",audience_id)

                binding.progressBar.visibility = View.VISIBLE

                calProfileUpdateApi()

            }
            else
            {
//                val intent = Intent(this,PresentationActivity::class.java)
//                startActivity(intent)
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


   private fun calProfileUpdateApi()
    {
        ApiClient.getInstance()!!.updateConnectProfile(authorization,audience_id,
            binding.etName.text.toString(),
            binding.etEmail.text.toString(),
            binding.etLocation.text.toString(),
            dob,
            gender
        )!!.enqueue(object : Callback<ProfileRes>{
            override fun onResponse(
                call: Call<ProfileRes>,
                response: Response<ProfileRes>
            ) {

                binding.progressBar.visibility = View.GONE

                Log.d("api","connect profile update response")

                if(response.isSuccessful)
                    if(response.body()!=null)
                    {
                        Toast.makeText(this@IntroduceYourselfActivity,"Profile Data Updated",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@IntroduceYourselfActivity,PresentationActivity::class.java)
                        startActivity(intent)

                        Log.d("api","connect profile update success")

                    }
            }

            override fun onFailure(call: Call<ProfileRes>, t: Throwable) {
                Toast.makeText(this@IntroduceYourselfActivity,"Some error occurred",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE

                Log.d("api","connect profile update failure")

            }
        }  )
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

                dob = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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