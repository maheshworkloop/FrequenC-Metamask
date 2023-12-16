package com.dev.frequenc.ui_codes.connect.Profile

import android.Manifest
import android.app.Dialog
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
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityEditProfileBinding
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.data.AudienceDataResponse
import com.dev.frequenc.ui_codes.data.ProfileSuccessResponse
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.ui_codes.util.CommonInterface
import com.dev.frequenc.ui_codes.util.CommonUtils
import com.dev.frequenc.ui_codes.util.Constants
import com.dev.frequenc.util.ImageUtil
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.time.LocalDate
import java.time.Period

class EditProfileActivity : AppCompatActivity(), CommonInterface {

    lateinit var binding: ActivityEditProfileBinding
    private var mImagePath = ""
    private var mImagePath2 = ""
    private var mImagePath3 = ""
    private var mImagePath4 = ""
    lateinit var dialog: Dialog
    lateinit var authorization: String
    lateinit var id: String
    var profile_images = ArrayList<MultipartBody.Part>()


    private val GALLERY = 1
    private var CAMERA = 2
    var encoded_img_profile1: String = ""
    var encoded_img_profile2: String = ""
    var encoded_img_profile3: String = ""
    var encoded_img_profile4: String = ""
    var fromImageView = ""
    private val PERMISSION_REQUEST_CODE = 200

    lateinit var item : AudienceDataResponse

    var langEng = false
    var langHin = false
    var langPunjabi = false

    var flagImg1 = false
    var flagImg2 = false
    var flagImg3 = false
    var flagImg4 = false

    lateinit var sharedPreferences : SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val bundle = intent.extras

        if (bundle != null) {
            item = intent.getSerializableExtra("item") as AudienceDataResponse
            id = item._id
        }

        init()



        binding.ivProfile1.setOnClickListener {

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


        binding.ivProfile2.setOnClickListener {

            if(checkPermission())
            {
                fromImageView = "profile2"
                showPictureDialog()
            }
            else
            {
                requestPermission()
            }
        }

        binding.ivProfile3.setOnClickListener {

            if(checkPermission())
            {
                fromImageView = "profile3"
                showPictureDialog()
            }
            else
            {
                requestPermission()
            }
        }

        binding.ivProfile4.setOnClickListener {

            if(checkPermission())
            {
                fromImageView = "profile4"
                showPictureDialog()
            }
            else
            {
                requestPermission()
            }
        }


        binding.tvHeightAdd.setOnClickListener {

            showHeight()
        }


        binding.tvSmoke.setOnClickListener {

            showSmoke()
        }


        var mlistPets = listOf<String>("Dogs", "Cats", "Birds", "Fish", "Turtles")
        binding.tvPetsAdd.setOnClickListener {
            showDialogAdd("Do you Have Pet ?", mlistPets, R.drawable.pawprint, binding.tvPetsAdd)
        }


        var mlistOccupation = listOf<String>("Businessmen", "Salaried", "HouseWife")
        binding.tvOccupationAdd.setOnClickListener {
            showDialogAdd(
                "What's your Occupation ?",
                mlistOccupation,
                R.drawable.briefcase,
                binding.tvOccupationAdd
            )
        }


        var mlistZodiac = listOf<String>(
            "Capricorn", "Gemini", "Libra", "Pisces",
            "Taurus", "Aries", "Cancer", "Leo",
            "Sagittarius", "Virgo", "Aquarius", "Scorpio"
        )
        binding.tvZodiacAdd.setOnClickListener {
            showDialogAdd(
                "What's your Zodiac Sign ?",
                mlistZodiac,
                R.drawable.zodiac,
                binding.tvZodiacAdd
            )
        }


        var mlistDrinking = listOf<String>("Frequently", "Socially", "Rarely", "Never", "Sober")

        binding.tvDrinkAdd.setOnClickListener {
            showDialogAdd("Do you Drink ?", mlistDrinking, R.drawable.cheers, binding.tvDrinkAdd)
        }


        var mlistReligion = listOf<String>(
            "Hindu", "Muslim", "Christian", "Jain", "Jewish",
            "Buddhist"
        )
        binding.tvReligionAdd.setOnClickListener {
            showDialogAdd(
                "What's your Religion ?",
                mlistReligion,
                R.drawable.zodiac,
                binding.tvReligionAdd
            )
        }


        var mlistChildren = listOf<String>("0", "1", "2", "3", "4")
        binding.tvChildrenAdd.setOnClickListener {
            showDialogAdd(
                "How Many Children do you have ?",
                mlistChildren,
                R.drawable.children,
                binding.tvChildrenAdd
            )
        }


        var mlistEducation = listOf<String>("10th", "12th", "MBA", "College", "Graduate")
        binding.tvEducationAdd.setOnClickListener {
            showDialogAdd(
                "Enter your Education ?",
                mlistEducation,
                R.drawable.education,
                binding.tvEducationAdd
            )
        }


        var mlistLocation = listOf<String>("Noida", "Delhi", "Mumbai", "Chandigarh", "Amritsar")
        binding.tvLocationAdd.setOnClickListener {
            showDialogAdd(
                "Enter your City ?",
                mlistLocation,
                R.drawable.cityscape,
                binding.tvLocationAdd
            )
        }

        var mlistGender = listOf<String>("Male", "Female")
        binding.tvGenderAdd.setOnClickListener {
            showDialogAdd("What's your Gender", mlistGender, R.drawable.gender, binding.tvGenderAdd)
        }


        binding.tvEnglish.setOnClickListener {

            langEng = !langEng

            if (langEng) {
                binding.tvEnglish.setBackgroundResource(R.drawable.bg_tv_mydetail_blue_dark)
            } else {
                binding.tvEnglish.setBackgroundResource(R.drawable.bg_tv_mydetail_blue)

            }

        }

        binding.tvHindi.setOnClickListener {

            langHin = !langHin

            if (langHin) {
                binding.tvHindi.setBackgroundResource(R.drawable.bg_tv_mydetail_blue_dark)
            } else {
                binding.tvHindi.setBackgroundResource(R.drawable.bg_tv_mydetail_blue)

            }

        }

        binding.tvPunjabi.setOnClickListener {

            langPunjabi = !langPunjabi

            if (langPunjabi) {
                binding.tvPunjabi.setBackgroundResource(R.drawable.bg_tv_mydetail_blue_dark)
            } else {
                binding.tvPunjabi.setBackgroundResource(R.drawable.bg_tv_mydetail_blue)

            }

        }


        binding.btnUpdate.setOnClickListener {

            Log.d("api", "Calling update api")
            updateProfileApi()


        }

    }


    private fun showHeight() {
        var heightlist = listOf<String>(
            "151 cm", "152 cm", "153 cm", "154 cm", "155 cm",
            "156 cm", "157 cm", "158 cm", "159 cm", "160 cm"
        )





        dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_showheight)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)

        var gifImage = dialog.findViewById<GifImageView>(R.id.ivAnimSplash)
        Glide.with(this).load(R.drawable.frequenc_loader).into(gifImage)

        var tvOk = dialog.findViewById<TextView>(R.id.btnOk)
        var tvCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        var spHeight = dialog.findViewById<Spinner>(R.id.spData)



        tvCancel.setOnClickListener {
            dialog.cancel()
        }

        tvOk.setOnClickListener {

//            Log.d("height",spHeight.selectedItem.toString())

            binding.tvHeightAdd.text = spHeight.selectedItem.toString()
            dialog.cancel()

        }


        spHeight.adapter = ArrayAdapter(this@EditProfileActivity, R.layout.spinner, heightlist)


        val layoutParams: WindowManager.LayoutParams = dialog.window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT


//        dialog.getWindow()!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }


    private fun showSmoke() {
        var heightlist = listOf<String>(
            "Socially", "Never", "Regularly"
        )





        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.layout_dialog_smoking)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)

        var gifImage = dialog.findViewById<GifImageView>(R.id.ivAnimSplash)
        Glide.with(this).load(R.drawable.frequenc_loader).into(gifImage)

        var tvOk = dialog.findViewById<TextView>(R.id.btnOk)
        var tvCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        var spData = dialog.findViewById<Spinner>(R.id.spData)



        tvCancel.setOnClickListener {
            dialog.cancel()
        }

        tvOk.setOnClickListener {

//            Log.d("height",spHeight.selectedItem.toString())

            binding.tvSmoke.text = spData.selectedItem.toString()
            dialog.cancel()

        }


        spData.adapter = ArrayAdapter(this@EditProfileActivity, R.layout.spinner2, heightlist)


        val layoutParams: WindowManager.LayoutParams = dialog.window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT


//        dialog.getWindow()!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }


    private fun showDialogAdd(msg: String, mlist: List<String>, pic: Int, tv: TextView) {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_dialog_smoking)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)

        var gifImage = dialog.findViewById<GifImageView>(R.id.ivAnimSplash)
        Glide.with(this).load(R.drawable.frequenc_loader).into(gifImage)

        var tvOk = dialog.findViewById<TextView>(R.id.btnOk)
        var tvCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        var spData = dialog.findViewById<Spinner>(R.id.spData)
        var tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        var ivIcon = dialog.findViewById<ImageView>(R.id.ivIcon)

        tvTitle.text = msg


        Glide.with(this).load(pic).into(ivIcon)

//        ivIcon.setBackgroundResource(pic)

        tvCancel.setOnClickListener {
            dialog.cancel()
        }

        tvOk.setOnClickListener {

//            Log.d("height",spHeight.selectedItem.toString())

            tv.text = spData.selectedItem.toString()
            dialog.cancel()

        }


        spData.adapter = ArrayAdapter(this@EditProfileActivity, R.layout.spinner, mlist)


        val layoutParams: WindowManager.LayoutParams = dialog.window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT


//        dialog.getWindow()!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()


    }


    override fun setImageUri(imagePath: String?) {
        mImagePath = imagePath!!
//        val bitmap: Bitmap = getBitmapValue(mImagePath, this)
//
//        binding.ivProfile1.setImageBitmap(bitmap)

    }


    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.PICK_IMAGE_CAMERA -> if (resultCode == RESULT_OK) {
                val bitmap = getBitmapValue(mImagePath, this)
                //  ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //  bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//                mSignUpViewModel.profileImage.set(mImagePath)
//                updateImage(bitmap)
                binding.ivProfile1.setImageBitmap(bitmap)

            } else {
                Log.e("", "")
            }

            Constants.PICK_IMAGE_GALLERY -> if (resultCode == RESULT_OK && data!=null) {
                mImagePath = CommonUtils.getPath(this, data.data)
//                mSignUpViewModel.profileImage.set(mImagePath)
                try {
                    val bitmap1 = getBitmapValue(mImagePath, this)
                    val stream1 = ByteArrayOutputStream()
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 70, stream1)
//                    updateImage(bitmap1)
                } catch (e: Exception) {
                    Log.i("DEBUG", "Exception: " + e.message)
                }
            }
        }
//        requestForAllPermissions();
    }

*/


    private fun updateProfileApi() {
        var occupation: String = ""
        var gender: String = ""
        var education: String = ""
        var location: String = ""
        var height: String = ""
        var smoking: String = ""
        var drinking: String = ""
        var pets: String = ""
        var zodiac_sign: String = ""
        var religion = ""
        var description = ""

        if (binding.tvOccupationAdd.text != "Add")
            occupation = binding.tvOccupationAdd.text.toString()

        if (binding.tvGenderAdd.text != "Add")
            gender = binding.tvGenderAdd.text.toString()

        if (binding.tvEducationAdd.text != "Add")
            education = binding.tvEducationAdd.text.toString()

        if (binding.tvLocationAdd.text != "Add")
            location = binding.tvLocationAdd.text.toString()

        if (binding.tvHeightAdd.text != "Add")
            height = binding.tvHeightAdd.text.toString()

        if (binding.tvSmoke.text != "Add")
            smoking = binding.tvSmoke.text.toString()

        if (binding.tvDrinkAdd.text != "Add")
            drinking = binding.tvDrinkAdd.text.toString()

        if (binding.tvPetsAdd.text != "Add")
            pets = binding.tvPetsAdd.text.toString()

        if (binding.tvZodiacAdd.text != "Add")
            zodiac_sign = binding.tvZodiacAdd.text.toString()

        if (binding.tvReligionAdd.text != "Add")
            religion = binding.tvReligionAdd.text.toString()


        if (binding.etEditDescription.editText!!.text.toString() != "")
            description = binding.etEditDescription.editText!!.text.toString()

//        Log.d("description")


        var langList = ArrayList<String>()

        if (langHin) {
            langList.add("Hindi")
        }

        if (langPunjabi) {
            langList.add("Punjabi")
        }

        if (langEng)
            langList.add("English")


        var profileImage: MultipartBody.Part? = null

        if (flagImg1) {
            val file = File(mImagePath)
            profileImage = MultipartBody.Part.createFormData(
                "profile_images",
                file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            )
        } else {

//            if(item.profile_images.isNotEmpty())
//            {
//                profileImage = MultipartBody.Part.createFormData(
//                    "profile_images",
//                    "No image",
//                    RequestBody.create("text/plain".toMediaTypeOrNull(),"")
//                )
//            }


        }


        var profileImage2: MultipartBody.Part? = null
        if (flagImg2) {
            val file = File(mImagePath2)
            profileImage2 = MultipartBody.Part.createFormData(
                "profile_images",
                file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            )
        } else {
//            profileImage2 = MultipartBody.Part.createFormData(
//                "profile_images",
//                "No Image",
//                RequestBody.create("text/plain".toMediaTypeOrNull(),"")
//            )
        }


        var profileImage3: MultipartBody.Part? = null
        if (flagImg3) {
            val file = File(mImagePath3)
            profileImage3 = MultipartBody.Part.createFormData(
                "profile_images",
                file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            )
        } else {
//            profileImage3 = MultipartBody.Part.createFormData(
//                "profile_images",
//                "No image",
//                RequestBody.create("text/plain".toMediaTypeOrNull(),"")
//            )
        }


        var profileImage4: MultipartBody.Part? = null
        if (flagImg4) {
            val file = File(mImagePath4)
            profileImage4 = MultipartBody.Part.createFormData(
                "profile_images",
                file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            )
        } else {
//            profileImage4 = MultipartBody.Part.createFormData(
//                "profile_images",
//                item.profile_images[3],
//                RequestBody.create("text/plain".toMediaTypeOrNull(),"")
//            )
        }


        if (profileImage != null)
            profile_images.add(profileImage)

        if (profileImage2 != null)
            profile_images.add(profileImage2)

        if (profileImage3 != null)
            profile_images.add(profileImage3)

        if (profileImage4 != null)
            profile_images.add(profileImage4)


//        profile_images = listOf(profileImage!!,profileImage2!!,profileImage3!!,profileImage4!!)

        binding.progressBar.visibility = View.VISIBLE


        if (flagImg1 || flagImg2 || flagImg3 || flagImg4)
        {

            ApiClient.getInstance()!!.updateMobileProfile(
                authorization,
                id,
                profile_images,
                RequestBody.create("text/plain".toMediaTypeOrNull(), description),
                RequestBody.create("text/plain".toMediaTypeOrNull(), occupation),
                RequestBody.create("text/plain".toMediaTypeOrNull(), gender),
                RequestBody.create("text/plain".toMediaTypeOrNull(), education),
                RequestBody.create("text/plain".toMediaTypeOrNull(), location),
                RequestBody.create("text/plain".toMediaTypeOrNull(), height),
                RequestBody.create("text/plain".toMediaTypeOrNull(), smoking),
                RequestBody.create("text/plain".toMediaTypeOrNull(), drinking),
                RequestBody.create("text/plain".toMediaTypeOrNull(), pets),
                RequestBody.create("text/plain".toMediaTypeOrNull(), zodiac_sign),
                RequestBody.create("text/plain".toMediaTypeOrNull(), religion),
//            RequestBody.create("text/plain".toMediaTypeOrNull(), langList),


            ).enqueue(object : Callback<ProfileSuccessResponse> {
                override fun onResponse(
                    call: Call<ProfileSuccessResponse>,
                    response: Response<ProfileSuccessResponse>
                ) {

                    binding.progressBar.visibility = View.GONE


                    if (response.isSuccessful && response.body() != null) {
                        Log.d("api", "Profile Response")
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Profile Updated Successfully !!!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()

                    }
                }

                override fun onFailure(call: Call<ProfileSuccessResponse>, t: Throwable) {

                    binding.progressBar.visibility = View.GONE

                    Log.d("api", t.localizedMessage)

                }
            })


        } // end of if

        else {

//            Log.d("profile_image",item.profile_images[0].toString())
            ApiClient.getInstance()!!.updateMobileProfile2(
                authorization,
                id,
                item.profile_images,
                RequestBody.create("text/plain".toMediaTypeOrNull(), description),
                RequestBody.create("text/plain".toMediaTypeOrNull(), occupation),
                RequestBody.create("text/plain".toMediaTypeOrNull(), gender),
                RequestBody.create("text/plain".toMediaTypeOrNull(), education),
                RequestBody.create("text/plain".toMediaTypeOrNull(), location),
                RequestBody.create("text/plain".toMediaTypeOrNull(), height),
                RequestBody.create("text/plain".toMediaTypeOrNull(), smoking),
                RequestBody.create("text/plain".toMediaTypeOrNull(), drinking),
                RequestBody.create("text/plain".toMediaTypeOrNull(), pets),
                RequestBody.create("text/plain".toMediaTypeOrNull(), zodiac_sign),
                RequestBody.create("text/plain".toMediaTypeOrNull(), religion),
//            RequestBody.create("text/plain".toMediaTypeOrNull(), langList),


            ).enqueue(object : Callback<ProfileSuccessResponse> {
                override fun onResponse(
                    call: Call<ProfileSuccessResponse>,
                    response: Response<ProfileSuccessResponse>
                ) {

                    binding.progressBar.visibility = View.GONE


                    if (response.isSuccessful && response.body() != null) {
                        Log.d("api", "Profile Response")
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Profile Updated Successfully !!!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()

                    }
                }

                override fun onFailure(call: Call<ProfileSuccessResponse>, t: Throwable) {

                    binding.progressBar.visibility = View.GONE

                    Log.d("api", t.localizedMessage)

                }
            })
        }

    } // end of else



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
                        binding.ivProfile1.setImageBitmap(bitmap)
                        encoded_img_profile1 = ImageUtil.convert(bitmap!!)

                        val byteBuffer = ByteBuffer.allocate(bitmap.byteCount)
                        bitmap.copyPixelsToBuffer(byteBuffer)

                        val byteArray = byteBuffer.array()
                        mImagePath = CommonUtils.getPath(this, data.data)
                        flagImg1 = true

//                        buffer = byteArray


                    }
                    else if(fromImageView.equals("profile2") )
                    {
                        binding.ivProfile2.setImageBitmap(bitmap)
                        encoded_img_profile2 = ImageUtil.convert(bitmap!!)
                        mImagePath2 = CommonUtils.getPath(this, data.data)
                        flagImg2 = true

                    }
                    else if(fromImageView.equals("profile3") )
                    {
                        binding.ivProfile3.setImageBitmap(bitmap)
                        encoded_img_profile3 = ImageUtil.convert(bitmap!!)
                        mImagePath3 = CommonUtils.getPath(this, data.data)

                        flagImg3 = true

                    }
                    else if(fromImageView.equals("profile4") )
                    {
                        binding.ivProfile4.setImageBitmap(bitmap)
                        encoded_img_profile4 = ImageUtil.convert(bitmap!!)
                        mImagePath4 = CommonUtils.getPath(this, data.data)

                        flagImg4 = true

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //Toast.makeText(HomeActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!["data"] as Bitmap?

            if(fromImageView.equals("profile"))
            {
                binding.ivProfile1.setImageBitmap(thumbnail)
                encoded_img_profile1 = ImageUtil.convert(thumbnail!!)


            }
            else if(fromImageView.equals("profile2") )
            {
                binding.ivProfile2.setImageBitmap(thumbnail)
                encoded_img_profile2 = ImageUtil.convert(thumbnail!!)


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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init()
    {

        sharedPreferences = getSharedPreferences(Constants.SharedPreference , Context.MODE_PRIVATE)

        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()


        Log.d("dob",item.dob)


        for(i in item.language)
        {
            Log.d("Language",i)

            if(i == "English")
                binding.tvEnglish.setBackgroundResource(R.drawable.bg_tv_mydetail_blue_dark)
            if(i == "Hindi")
                binding.tvHindi.setBackgroundResource(R.drawable.bg_tv_mydetail_blue_dark)
            if(i == "Punjabi")
                binding.tvPunjabi.setBackgroundResource(R.drawable.bg_tv_mydetail_blue_dark)

        }

        val dateParts: List<String> = item.dob.split("-")
        val day = dateParts[2]
        val month = dateParts[1]
        val year = dateParts[0]

        var age = getAge(year.toInt(), month.toInt(),day.toInt()).toString()

        binding.tvAbout.text = item.fullName + " , " + age

        binding.tvEmailAdd.text = item.email

        if(!item.description.isNullOrEmpty())
        {
            binding.etEditDescription.editText!!.setText(item.description)
        }

        if(!item.gender.isNullOrEmpty())
        {
            binding.tvGenderAdd.text = item.gender
        }

        if(!item.occupation.isNullOrEmpty())
        {
            binding.tvOccupationAdd.text = item.occupation
        }

        if(!item.education.isNullOrEmpty())
        {
            binding.tvEducationAdd.text = item.education
        }

        if(!item.location.isNullOrEmpty())
        {
            binding.tvLocationAdd.text = item.location
        }

        if(!item.height.isNullOrEmpty())
        {
            binding.tvHeightAdd.text = item.height
        }

        if(!item.smoking.isNullOrEmpty())
        {
            binding.tvSmoke.text = item.smoking
        }

        if(!item.drinking.isNullOrEmpty())
        {
            binding.tvDrinkAdd.text = item.drinking
        }

        if(!item.pets.isNullOrEmpty())
        {
            binding.tvPetsAdd.text = item.pets
        }

        if(!item.children.isNullOrEmpty())
        {
            binding.tvChildrenAdd.text = item.children
        }

        if(!item.zodiac_sign.isNullOrEmpty())
        {
            binding.tvZodiacAdd.text = item.zodiac_sign
        }

        if(!item.religion.isNullOrEmpty())
        {
            binding.tvReligionAdd.text = item.religion
        }

        if(!item.profile_images.isNullOrEmpty())
        {

            if(item.profile_images.size>=4)
            {
                if(!item.profile_images[3].isNullOrEmpty())
                {
                    encoded_img_profile4 = item.profile_images[3]
                    Glide.with(this).load(item.profile_images[3]).into(binding.ivProfile4)
                }
            }


            if(item.profile_images.size>=3)
            {
                if(!item.profile_images[2].isNullOrEmpty())
                {
                    encoded_img_profile3 = item.profile_images[2]
                    Glide.with(this).load(item.profile_images[2]).into(binding.ivProfile3)
                }
            }


            if(item.profile_images.size>=2)
            {
                if(!item.profile_images[1].isNullOrEmpty())
                {
                    encoded_img_profile2 = item.profile_images[1]
                    Glide.with(this).load(item.profile_images[1]).into(binding.ivProfile2)
                }

            }


            if(item.profile_images.isNotEmpty())
            {
            if(!item.profile_images[0].isNullOrEmpty())
            {
                encoded_img_profile1 = item.profile_images[0]
                Glide.with(this).load(item.profile_images[0]).into(binding.ivProfile1)
            }
            }

        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }



}