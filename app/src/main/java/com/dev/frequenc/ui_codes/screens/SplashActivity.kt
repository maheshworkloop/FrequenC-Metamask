package com.dev.frequenc.ui_codes.screens

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.MainActivity
import pl.droidsonroids.gif.GifImageView

class SplashActivity : AppCompatActivity() {

    lateinit var gifImageView :  GifImageView

    lateinit var ivLogoSplash : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        gifImageView = findViewById<GifImageView>(R.id.ivAnimSplash)

        ivLogoSplash = findViewById(R.id.ivLogoSplash)


        ApplylrtocAnimationInSplash()
        ApplylrtocAnimationInSplash2()


        Glide.with(this)
            .asGif()
            .load(R.drawable.frequenc_loader) // Replace with the actual resource ID of your GIF
            .into(gifImageView)



        Handler().postDelayed({

            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
            finish()
        }, 3000)



    }     // end of onCreate



    private fun ApplylrtocAnimationInSplash() {
        val halfWidth = (resources.displayMetrics.widthPixels / 2).toFloat()
        val lTocAnim: ObjectAnimator = ObjectAnimator.ofFloat(
            gifImageView ,
            "translationX",
            -halfWidth,
            0f
        )
        lTocAnim.interpolator = AccelerateInterpolator()
        lTocAnim.duration = 1000
        val rTocAnim: ObjectAnimator = ObjectAnimator.ofFloat(
            gifImageView,
            "translationX",
            halfWidth,
            0f
        )
        rTocAnim.interpolator = AccelerateInterpolator()
        rTocAnim.duration = 1000
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(lTocAnim, rTocAnim)
        animatorSet.start()
    }

    private fun ApplylrtocAnimationInSplash2() {

        val halfWidth = (resources.displayMetrics.widthPixels / 2).toFloat()
        val lTocAnim: ObjectAnimator = ObjectAnimator.ofFloat(
            ivLogoSplash ,
            "translationX",
            -halfWidth,
            0f
        )
        lTocAnim.interpolator = AccelerateInterpolator()
        lTocAnim.duration = 1000
        val rTocAnim: ObjectAnimator = ObjectAnimator.ofFloat(
            ivLogoSplash,
            "translationX",
            halfWidth,
            0f
        )
        rTocAnim.interpolator = AccelerateInterpolator()
        rTocAnim.duration = 1000
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(rTocAnim,lTocAnim)
        animatorSet.start()
    }







}