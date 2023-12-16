package com.dev.frequenc.ui_codes.screens.intro

import android.Manifest
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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityPresentationBinding
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.data.ProfileRes
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.ui_codes.util.CommonUtils
import com.dev.frequenc.ui_codes.util.Constants
import com.dev.frequenc.util.ImageUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer

class PresentationActivity : AppCompatActivity() {

    private val GALLERY = 1
    private var CAMERA = 2
    var encoded_img_1: String = ""
    var encoded_img_2: String = ""
    var fromImageView = ""
    private val PERMISSION_REQUEST_CODE = 200
    lateinit var mImagePath1 : String
    lateinit var mImagePath2 : String
    lateinit var binding : ActivityPresentationBinding

    var flagProfile1 = false
    var flagProfile2 = false

    lateinit var authorization : String
    lateinit var audience_id : String
    private lateinit var sharedPreferences: SharedPreferences

    var profile_images = ArrayList<MultipartBody.Part>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPresentationBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.ivProfile1.setOnClickListener {

            fromImageView = "ivProfile1"

            if(checkPermission())
            {
                showPictureDialog()
            }
            else
            {
                requestPermission()
            }


        }

        binding.ivProfile2.setOnClickListener {
            fromImageView = "ivProfile2"

            if(checkPermission())
            {
                showPictureDialog()
            }
            else
            {
                requestPermission()
            }
        }

        binding.ivNext.setOnClickListener {
            if(flagProfile1 && flagProfile2)
            {
                Toast.makeText(this,"All done",Toast.LENGTH_SHORT).show()

                var profileImage1: MultipartBody.Part? = null

                    val file1 = File(mImagePath1)
                    profileImage1 = MultipartBody.Part.createFormData(
                        "profile_images",
                        file1.name,
                        RequestBody.create("image/*".toMediaTypeOrNull(), file1)
                    )


                var profileImage2: MultipartBody.Part? = null
                val file2 = File(mImagePath2)
                profileImage2 = MultipartBody.Part.createFormData(
                    "profile_images",
                    file2.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), file2)
                )

                profile_images.add(profileImage1)
                profile_images.add(profileImage2)

               callProfileUpdateApi()

            }
            else
            {
                val intent = Intent (this,MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this,"Both images mandatory",Toast.LENGTH_SHORT).show()
            }
        }

        sharedPreferences = getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!
        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()
        audience_id = sharedPreferences.getString(Constants.AudienceId,"-1").toString()

    }



    private fun callProfileUpdateApi()
    {
//        Log.d("img",profile_images[0])
//        Log.d("img",profile_images[1])

        binding.progressBar.visibility = View.VISIBLE


        ApiClient.getInstance()!!.updateConnectProfilePhoto(authorization,audience_id,
            profile_images
        )!!.enqueue(object : Callback<ProfileRes> {
            override fun onResponse(
                call: Call<ProfileRes>,
                response: Response<ProfileRes>
            ) {

                binding.progressBar.visibility = View.GONE

                Log.d("api","connect profile photo update response")

                if(response.isSuccessful)
                    if(response.body()!=null)
                    {
                        Toast.makeText(this@PresentationActivity,"Profile photo Data Updated",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@PresentationActivity,MainActivity::class.java)
                        startActivity(intent)

                        Log.d("api","connect profile photo update success")

                    }
            }

            override fun onFailure(call: Call<ProfileRes>, t: Throwable) {
                Toast.makeText(this@PresentationActivity,"Some error occurred",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE

                Log.d("api","connect profile update failure")

            }
        }  )
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

                    if(fromImageView.equals("ivProfile1"))
                    {
                        binding.ivProfile1.setImageBitmap(bitmap)
                        encoded_img_1 = ImageUtil.convert(bitmap!!)
                        mImagePath1 = CommonUtils.getPath(this, data.data)

                        val byteBuffer = ByteBuffer.allocate(bitmap.byteCount)
                        bitmap.copyPixelsToBuffer(byteBuffer)

                        val byteArray = byteBuffer.array()

//                        buffer = byteArray


                        binding.ivProfile1.scaleType = ImageView.ScaleType.FIT_XY

                        flagProfile1 =true

                        if(flagProfile2)
                        {
                            binding.ivNext.background = resources.getDrawable(R.drawable.bg_img_intro_dark)

                        }


                    }
                    else if(fromImageView.equals("ivProfile2") )
                    {
                        binding.ivProfile2.setImageBitmap(bitmap)
                        encoded_img_2 = ImageUtil.convert(bitmap!!)
                        mImagePath2 = CommonUtils.getPath(this, data.data)

                        binding.ivProfile2.scaleType = ImageView.ScaleType.FIT_XY

                        flagProfile2 =true

                        if(flagProfile1)
                        {
                            binding.ivNext.background = resources.getDrawable(R.drawable.bg_img_intro_dark)
                        }

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //Toast.makeText(HomeActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!["data"] as Bitmap?

            if(fromImageView.equals("ivProfile1"))
            {
                binding.ivProfile1.setImageBitmap(thumbnail)
                encoded_img_1= ImageUtil.convert(thumbnail!!)

                binding.ivProfile1.scaleType = ImageView.ScaleType.FIT_XY

                flagProfile1 = true

                if(flagProfile2)
                {
                    binding.ivNext.background = resources.getDrawable(R.drawable.bg_img_intro_dark)

                }

            }
            else if(fromImageView.equals("ivProfile2") )
            {
                binding.ivProfile2.setImageBitmap(thumbnail)
                encoded_img_2 = ImageUtil.convert(thumbnail!!)

                binding.ivProfile2.scaleType = ImageView.ScaleType.FIT_XY

                flagProfile2 = true

                if(flagProfile1)
                {
                    binding.ivNext.background = resources.getDrawable(R.drawable.bg_img_intro_dark)
                }
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