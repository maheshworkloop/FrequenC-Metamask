package com.dev.frequenc.ui_codes.connect.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.dev.frequenc.R
import com.google.android.material.tabs.TabLayout
import pl.droidsonroids.gif.GifImageView

class ConnectHomeActivity : AppCompatActivity() {

    lateinit var root : View

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager

    lateinit var gifImageView : GifImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_home)
    }
}