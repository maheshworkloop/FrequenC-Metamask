package com.dev.frequenc.ui_codes.screens.Profile

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.AudienceDataResponse
import com.dev.frequenc.ui_codes.data.CityResponse
import com.dev.frequenc.ui_codes.data.CountryResponse
import com.dev.frequenc.ui_codes.data.ProfileSuccessResponse
import com.dev.frequenc.ui_codes.data.StateResponse
import com.dev.frequenc.databinding.ActivityAudienceProfileBinding
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.AppCommonMethods
import com.dev.frequenc.ui_codes.util.Constants
import com.dev.frequenc.util.ImageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AudienceProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityAudienceProfileBinding
    private val GALLERY = 1
    private var CAMERA = 2
    var encoded_img_profile: String = ""
    var encoded_img_banner: String = ""
    var fromImageView = ""
    private val PERMISSION_REQUEST_CODE = 200

    var countryList = ArrayList<String>()
    var stateList = ArrayList<String>()
    var cityList = ArrayList<String>()
    lateinit var selectedCountry : String
    var selectedCountryId : String = ""
    lateinit var selectedState : String
    var selectedStateId : String = ""
    lateinit var selectedCity : String
    var selectedCityId : String =""
    lateinit var item : AudienceDataResponse
    var selectedCountryIdReq = ""
    var selectedStateReq = ""
    var selectedCityIdReq = ""
    lateinit var authorization : String
    lateinit var sharedPreferences : SharedPreferences
//    lateinit var buffer : ByteArray

    var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudienceProfileBinding.inflate(layoutInflater)


        setContentView(binding.root)

        item = intent.getSerializableExtra("item") as AudienceDataResponse

        init()

    }  //end of onCreate


    private fun init()
    {
        binding.ivBackBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        sharedPreferences = getSharedPreferences(Constants.SharedPreference ,Context.MODE_PRIVATE)

        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()


        if(!item.fullName.isNullOrEmpty())
        {
            binding.etFullName.editText!!.setText(item.fullName)
        }

        if(!item.description.isNullOrEmpty())
        {
            binding.etDescription.editText!!.setText(item.description)
        }

        if(!item.email.isNullOrEmpty())
        {
            binding.etEmail.editText!!.setText(item.email)
        }


        if(!item.mobile_no.isNullOrEmpty())
        {
            binding.etMobile.editText!!.setText(item.mobile_no)
        }


        if(!item.postalCode.isNullOrEmpty())
        {
            binding.etZipCode.editText!!.setText(item.postalCode)
        }


        if(!item.dob.isNullOrEmpty())
        {
            binding.etDobET!!.setText(item.dob)
        }


          if(item.profile_pic.isNullOrEmpty() || item.profile_pic.isNullOrBlank() || item.profile_pic.length<4)
            {

            }
            else
            {
//                    Glide.with(this).load(item.profile_pic).placeholder(R.drawable.user).into(binding.ivProfile)

                Log.e("Profile Url",item.profile_pic)
            }


        if(item.banner_image.isNullOrEmpty() || item.banner_image.isNullOrBlank() )
        {

        }
        else
        {
            Glide.with(this).load(item.banner_image).placeholder(R.drawable.user).into(binding.ivBanner)
        }





        if(!item.banner_image.isNullOrEmpty())
        {
            Glide.with(this).load(item.banner_image).into(binding.ivBanner)
        }

        if(!item.gender.isNullOrEmpty())
        {
            if(item.gender.equals("Male"))
            {
                binding.rbMale.isChecked = true
                gender ="Male"
            }
            else if(item.gender.equals("Female"))
            {
                binding.rbMale.isChecked = true
                gender = "Femaile"
            }
        }


        binding.rbMale.setOnClickListener {
            binding.rbMale.isChecked = true
            binding.rbFemale.isChecked = false
            gender = "Male"
        }

        binding.rbFemale.setOnClickListener {
            binding.rbFemale.isChecked = true
            binding.rbMale.isChecked = false
            gender = "Female"
        }


        binding.ivBanner.setOnClickListener {
            if(checkPermission())
            {
                fromImageView = "banner"
                showPictureDialog()
            }
            else
            {
                fromImageView = "profile"
                requestPermission()
            }
        }


        binding.ivProfile.setOnClickListener {
            if(checkPermission())
            {
                fromImageView = "profile"
                showPictureDialog()
            }
            else
            {
                requestPermission()
            }
        }

//        Log.e("countryId",item.country)

        getCountryApi()


        binding.btnUpdate.setOnClickListener {

            if(   !AppCommonMethods.checkForEmail(binding.etEmail.editText!!))
            {
                Toast.makeText(this,"Email Required",Toast.LENGTH_SHORT).show()
                binding.etEmail.editText!!.requestFocus()

            }
            else if(selectedStateReq.isNullOrEmpty() )
            {
                Toast.makeText(this,"State Required",Toast.LENGTH_SHORT).show()
            }
            else if(selectedCityIdReq.isNullOrEmpty() ){
                Toast.makeText(this,"City Required",Toast.LENGTH_SHORT).show()
            }

            else
            {
                updateProfileApi()
//                Toast.makeText(this,"Update Button",Toast.LENGTH_SHORT).show()

            }


        }


        binding.etDobET.setOnClickListener {
//            Toast.makeText(this,"Clicked2",Toast.LENGTH_SHORT).show()

            dateTimePickerDialog()

        }

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
                val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDay)
                    }.time)
                binding.etDobET!!.setText(selectedDate)

            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        // Show the date picker dialog
        datePickerDialog.show()
    }

    private fun getCountryApi()
    {

        binding.progressBar.visibility = View.VISIBLE

        ApiClient.getInstance()!!.getCountry().enqueue(object : retrofit2.Callback<List<CountryResponse>>{
            override fun onResponse(
                call: Call<List<CountryResponse>>,
                response: Response<List<CountryResponse>>
            ) {
                binding.progressBar.visibility = View.GONE

                if(response.isSuccessful && response.body()!=null)
                {
                    val res = response.body()
                    for(i in res!!.indices)
                    {
//                        Log.d(Constants.ApiResponse, " Body : ${res[i]!!.name}")
                        countryList.add(res[i]!!.name)

                    }

                    binding.spCountry.adapter =
                        ArrayAdapter(this@AudienceProfileActivity, R.layout.spinner, countryList)


                     var countryPos =  getCountryNamePos(res!!, item.country)

                    if(countryPos!=-1)
                    {
                        binding.spCountry.setSelection(countryPos)
                        Log.e("countryId",item.country)
                    }

//                    getStateListApi(res!![countryPos].isoCode )



                    binding.spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            selectedCountry = countryList[position]
                            selectedCountryId = res[position]!!.isoCode
                            selectedCountryIdReq = res[position]!!._id

                            getStateListApi(selectedCountryId)


                        }

                    }



                }

            }

            override fun onFailure(call: Call<List<CountryResponse>>, t: Throwable) {
                Log.d(Constants.ApiError,"On Failure",t)
                binding.progressBar.visibility = View.GONE

            }
        })

    }

    private fun getStateListApi(isoCode : String)
    {
        stateList.clear()
        cityList.clear()
        binding.spCity.adapter = null

        binding.progressBar.visibility = View.VISIBLE
       ApiClient.getInstance()!!.getState(isoCode).enqueue(object :  retrofit2.Callback<List<StateResponse>>{
           override fun onResponse(
               call: Call<List<StateResponse>>,
               response: Response<List<StateResponse>>
           ) {
               binding.progressBar.visibility = View.GONE

               if(response.isSuccessful && response.body()!=null)
               {
                   val res = response.body()

                   for(i in res!!.indices)
                   {
                       stateList.add(res[i]!!.name)

                   }


                   binding.spState.adapter = ArrayAdapter(this@AudienceProfileActivity, R.layout.spinner,stateList)

                   if(stateList.isEmpty())
                   {
                       Toast.makeText(this@AudienceProfileActivity,"No State Data",Toast.LENGTH_SHORT).show()

                       binding.tvStateTag.visibility =View.GONE
                       binding.spState.visibility =View.GONE
                       binding.tvCityTag.visibility =View.GONE
                       binding.spCity.visibility =View.GONE
                   }

                   else
                   {

                       var pos = getStateNamePos(res!!,item.state)
                       if(pos!=-1)
                           binding.spState.setSelection(pos)

                       binding.tvStateTag.visibility =View.VISIBLE
                       binding.spState.visibility =View.VISIBLE
                       binding.tvCityTag.visibility =View.VISIBLE
                       binding.spCity.visibility =View.VISIBLE

//                       getCityListApi(item.state)


                       binding.spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                           override fun onNothingSelected(parent: AdapterView<*>?) {
                           }

                           override fun onItemSelected(
                               parent: AdapterView<*>?,
                               view: View?,
                               position: Int,
                               id: Long
                           ) {

                               selectedState = stateList[position]
                               selectedStateId = res[position]!!.id
                               selectedStateReq = res[position]!!._id

                               getCityListApi(selectedStateId)


                           }

                       }

                   }

               }
           }

           override fun onFailure(call: Call<List<StateResponse>>, t: Throwable) {
               binding.progressBar.visibility = View.GONE
               Toast.makeText(this@AudienceProfileActivity,t.localizedMessage,Toast.LENGTH_SHORT).show()
           }
       })
    }

    private fun getCityListApi(stateId : String)
    {
        binding.progressBar.visibility = View.VISIBLE

        cityList.clear()

        binding.spCity.adapter = ArrayAdapter(this@AudienceProfileActivity,R.layout.spinner,cityList)


        ApiClient.getInstance()!!.getCity(stateId).enqueue(object :  retrofit2.Callback<List<CityResponse>>{
            override fun onResponse(
                call: Call<List<CityResponse>>,
                response: Response<List<CityResponse>>
            ) {

                binding.progressBar.visibility = View.GONE

                if(response.isSuccessful && response.body()!=null)
                {
                    val res = response.body()

                    for(i in res!!.indices)
                    {
                        cityList.add(res[i]!!.name)
                    }

                    binding.spCity.adapter = ArrayAdapter(this@AudienceProfileActivity,R.layout.spinner,cityList)
                    if(cityList.isEmpty())
                    {
                        Toast.makeText(this@AudienceProfileActivity,"No City Data",Toast.LENGTH_SHORT).show()
                        binding.tvCityTag.visibility =View.GONE
                        binding.spCity.visibility =View.GONE
                    }

                    else
                    {
                        var pos = getCityNamePos(res!!,item.city)
                        binding.spCity.setSelection(pos)
                        binding.tvCityTag.visibility =View.VISIBLE
                        binding.spCity.visibility =View.VISIBLE

                        binding.spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {

                                selectedCity = cityList[position]
                                selectedCityId = res[position]!!.id
                                selectedCityIdReq = res[position]!!._id

                            }

                        }

                    }

                }
            }

            override fun onFailure(call: Call<List<CityResponse>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE

            }
        })
    }

    private fun getCountryNamePos(countryList : List<CountryResponse>,id : String ) : Int
    {
        for (i in countryList.indices) {
            if (countryList[i]._id == id)
                return i
        }
        return -1
    }

    private fun getStateNamePos(stateList : List<StateResponse>,id : String ) : Int
    {
        for (i in stateList.indices) {
            if (stateList[i].id == id)
                return i
        }
        return -1
    }

    private fun getCityNamePos(cityList : List<CityResponse>,id : String ) : Int
    {
        for (i in cityList.indices) {
            if (cityList[i].id == id)
                return i
        }
        return -1
    }


   private fun updateProfileApi()
   {
       Log.d("user id",item.id)

//       val updateProfileReq = UpdateProfileReq(
//           binding.etFullName.editText!!.text.toString() ,
//           encoded_img_profile,
//           encoded_img_banner,
//           binding.etEmail.editText!!.text.toString(),
//           selectedCountryIdReq,
//           selectedStateReq,
//           selectedCityIdReq,
//           binding.etZipCode.editText!!.text.toString(),
//           gender,
//           binding.etDob.editText!!.text.toString(),
//           binding.etDescription.editText!!.text.toString()
//       )

//       authorization = authorization.substringAfter("Bearer ")
//       val formDataMap = mutableMapOf<String, String>()
//       formDataMap["fullName"] = binding.etFullName.editText!!.text.toString() + "aa"
//       formDataMap["profile_pic"] = encoded_img_profile
//       formDataMap["banner_image"] = encoded_img_banner
//       formDataMap["email"] = binding.etEmail.editText!!.text.toString()
//       formDataMap["country"] = selectedCountryId
//       formDataMap["state"] = selectedStateId
//       formDataMap["city"] = selectedCityId
//       formDataMap["postalCode"] = binding.etZipCode.editText!!.text.toString()
//       formDataMap["gender"] = gender
//       formDataMap["dob"] = binding.etDob.editText!!.text.toString()
//       formDataMap["description"] = binding.etDescription.editText!!.text.toString()
       binding.progressBar.visibility = View.VISIBLE

//       val profilePicReq = ProfilePicReq("profile_pic","profile_pic_original",buffer)


     ApiClient.getInstance()!!.updateProfile(authorization, item.id,
//                                                formDataMap
                                             encoded_img_profile,
                                             encoded_img_banner,
//                                             updateProfileReq,
//                                               profilePicReq,
                                             binding.etFullName.editText!!.text.toString(),
                                             binding.etEmail.editText!!.text.toString(),
                                             selectedCountryIdReq,
                                             selectedStateReq,
                                             selectedCityIdReq,
                                             binding.etZipCode.editText!!.text.toString(),
                                             gender,
                                             binding.etDob.editText!!.text!!.toString()
                                             ).enqueue(object : Callback<ProfileSuccessResponse> {
         override fun onResponse(call: Call<ProfileSuccessResponse>, response: Response<ProfileSuccessResponse>) {
             binding.progressBar.visibility = View.GONE

//             Toast.makeText(this@AudienceProfileActivity,"Profile Response !!!",Toast.LENGTH_SHORT).show()
             Log.d("response",response.body().toString())
//             Log.d("response",response.body().toString())

             if(response.isSuccessful && response.body()!=null)
             {
                 Toast.makeText(this@AudienceProfileActivity,"Profile Updated Successfully !!!",Toast.LENGTH_SHORT).show()
             }
             else
             {
                 Toast.makeText(this@AudienceProfileActivity,response.errorBody().toString(),Toast.LENGTH_SHORT).show()

             }

         }

         override fun onFailure(call: Call<ProfileSuccessResponse>, t: Throwable) {
             binding.progressBar.visibility = View.GONE
             Log.d(Constants.ApiError,t.localizedMessage)
             Toast.makeText(this@AudienceProfileActivity,t.localizedMessage,Toast.LENGTH_SHORT).show()
//             Log.d("response",response.body().toString())

         }
     } )
   }


    //********************CAMERA METHODS*************************//
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from Gallery",
            "Capture photo from Camera"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI)

                    if(fromImageView.equals("profile"))
                    {
                        binding.ivProfile.setImageBitmap(bitmap)
                        encoded_img_profile = ImageUtil.convert(bitmap!!)

                        val byteBuffer = ByteBuffer.allocate(bitmap.byteCount)
                        bitmap.copyPixelsToBuffer(byteBuffer)

                        val byteArray = byteBuffer.array()

//                        buffer = byteArray


                    }
                    else if(fromImageView.equals("banner") )
                    {
                        binding.ivBanner.setImageBitmap(bitmap)
                        encoded_img_banner = ImageUtil.convert(bitmap!!)

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //Toast.makeText(HomeActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!["data"] as Bitmap?

            if(fromImageView.equals("profile"))
            {
                binding.ivProfile.setImageBitmap(thumbnail)
                encoded_img_profile = ImageUtil.convert(thumbnail!!)


            }
            else if(fromImageView.equals("banner") )
            {
                binding.ivBanner.setImageBitmap(thumbnail)
                encoded_img_banner = ImageUtil.convert(thumbnail!!)


            }
        }
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)

         {
            // Permission is not granted
            return false
        }
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()

                // main logic
            } else {
                val PERMISSIONS = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) !=
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        showMessageOKCancel(
                            "You need to allow access permissions"
                        ) { dialog, which ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermission()
                            }
                        }
                    }
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) !=
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        showMessageOKCancel(
                            "You need to allow access permissions"
                        ) { dialog, which ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermission()
                            }
                        }
                    }
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        showMessageOKCancel(
                            "You need to allow access permissions"
                        ) { dialog, which ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermission()
                            }
                        }
                    }
                }
            }
        }
    }


    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }



}